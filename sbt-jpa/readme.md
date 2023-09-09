# Transaction 의 커밋, 롤백 범위 테스트

각 RPW 에서 예외가 발생했을 때 롤백이 잘 되는지, chunkSize 와 pageSize 가 다른 경우 어떻게 동작하는지 확인합니다.

### 테스트 결과
- Reader, Processor, Writer 는 하나의 트랜잭션으로 묶이고 예외가 발생하면 각 부분에서 이루어진 모든 것이 롤백이 된다. (Chunk 단위로 커밋/롤백된다.)
- Reader 에서 ChunkSize 만큼 읽는다. (chunkSize 보다 데이터 수가 적다면 데이터 만큼만 읽는다.)
  - pageSize < ChunkSize 인 경우
    - Reader 에서 chunkSize 만큼 모일때까지 페이징 조회를 한다.
    - Processor 와 Writer 는 Chunk 단위로 처리한다.
  - pageSize > ChunkSize 인 경우
    - Reader 에서는 당연히 pageSize 만큼 조회된다. (조회쿼리 한 번 실행)
    - Processor 와 Writer 는 Chunk 단위로 처리한다.
    - 만약 JPA 를 사용한다면 첫 Chunk 만 CUD 가 되고 나머지는 쿼리가 동작하지 않는다.
    ```
    // 로그
    [reader] memberSize: 2
    [processor] start. member: Member(id=1, name='1 test test test')
    [writer] current member: Member(id=1, name='1 test test test')
    /* update
       for kr.disdong.spring.batch.domain.Member */update member
       set
         name=?
       where
        id=?
    [processor] start. member: Member(id=2, name='2 test')
    [writer] current member: Member(id=2, name='2 test')
    
    ...
    
    // 더 이상 update 쿼리가 실행되지 않는다.
    ```
    - 한 번 커밋되고 남은 데이터도 chunk 단위로 process, write 과정을 거치긴 하지만 실제 update 쿼리는 날아가지 않습니다.
    - memberRepository.save(member) 를 사용하면 update 쿼리가 동작한다.
    - 커밋되면 한 트랜잭션이 종료되고 영속성 컨텍스트가 끊겨서 변경감지는 동작하지 않는것같고, 직접 Tx 를 걸고(save) 업데이트를 해주면 정상적으로 동작한다. 
- Processor 는 Reader 에서 읽은 데이터를 **하나씩** Process 한다.
- Processor 에서 chunkSize 만큼 처리가 되면 wrIter 로 **한꺼번에** 넘겨서 write 한다. 
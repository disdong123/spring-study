#
도메인 관련 로직을 작성합니다. JPA 에 대한 의존을 제거합니다.

## 객체 정의
Post 를 기준으로 설명합니다.
- PostDo: 순수한 도메인 객체입니다. 도메인 관련 로직이 작성됩니다.
- PostEntity: JPA Entity 입니다.
- PostDoImpl: JPA Entity 와 PostDo 가 만나는 객체입니다. Service 단에서 사용되고, 컨트롤러로 반환될땐 PostDo 또는 다른 DTO 로 반환됩니다.
# @Transactional 테스트


- 클래스에 @Transactional 이 붙어있으면 프록시 객체가 생성된다
- @Transactional 로 인해 생성된 프록시 객체여도 정상적으로 트랜잭션 처리가 안될수도 있다. 
  - 오직 프록시를 통해 들어오는 외부 메서드 호출만 인터셉트하므로 자기 자신을 호출할 때는 예외가 낫을 때 롤백되지 않는다.
    ```kotlin
    class A {
        fun methodA() {
            methodB();
        }
        @Transactional
        fun methodB() {
        }
    }
    ```
    - 위와 같은 경우 프록시 객체의 methodA() 가 호출되지만 내부의 methodB() 는 프록시 객체를 통해 호출되지 않고, A 객체 자신을 통해 호출되므로 트랜잭션 처리가 되지 않는다.
  - 클래스를 분리하면 해결이 된다.
    ```kotlin
    class A (
        private val b: B
    ){
        fun methodA() {
            b.methodB();
        }
    }
    
    class B {
        @Transactional
        fun methodB() {
        }
    }
    ```
    - 위의 경우 프록 객체의 methodB 가 호출되어 정상적으로 트랜잭션 처리가 된다.
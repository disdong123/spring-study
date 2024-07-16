# Spring reactive test

### 멀티모듈 구조

srt-core: 공통 모듈입니다.
srt-mvc-server: tomcat server 입니다.
srt-webflux-server: netty server 입니다.
srt-server-test: mvc, webflux server 를 테스트하는 모듈입니다.

### 테스트 환경

각 서버는 master, slave server 를 갖고 있고, master server 가 slave server 를 호출하는 구조입니다.
DB Thread pool 은 100, 각 reactor netty thread pool 은 10개입니다.
coroutine, reactor, blocking call 을 테스트합니다.

### 결과

MasterServer 에서 RestTemplate 으로 요청하면 MasterServer 의 thread pool 이 blocking 되므로 2초 이상 걸립니다. (*_WITH_REST_TEMPLATE)
SlaveServer 에서 non-blocking 으로 요청 하더라도 SlaveServer 가 blocking 되어있으면 2초 이상 걸립니다. (THREAD_SLEEP_WITH_WEB_CLIENT_REACTIVE, THREAD_SLEEP_WITH_WEB_CLIENT_COROUTINE)
나머지 경우는 모두 non-blocking call 이므로 1초정도 걸립니다.

### 주의

의미있는 테스트 결과를 얻기 위해서는 서버를 실핼하고 미리 테스트를 한 번 실행하여 서버를 웜업하는 과정이 필요합니다.
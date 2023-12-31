package kr.disdong.spring.basic

import kr.disdong.spring.basic.core.helper.ApplicationContextHelper
import kr.disdong.spring.basic.core.initializer.DispatcherServletInitializer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
class ApplicationConfiguration

fun server1() {
    TomcatServletWebServerFactory()
        // .getWebServer(TestInitializer())
        // .getWebServer(FrontControllerInitializer())
        .getWebServer(DispatcherServletInitializer())
        .start()
}

fun server2() {
    ApplicationContextHelper.doSimpleGenericWeb()
}

fun server3() {
    ApplicationContextHelper.doAnnotationConfigWeb()
}

fun main() {
    server3()
}

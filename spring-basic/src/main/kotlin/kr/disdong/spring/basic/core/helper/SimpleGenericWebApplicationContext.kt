package kr.disdong.spring.basic.core.helper

import kr.disdong.spring.basic.core.initializer.DispatcherServletInitializer
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.context.support.GenericWebApplicationContext

class SimpleGenericWebApplicationContext : GenericWebApplicationContext() {
    override fun onRefresh() {
        super.onRefresh()
        TomcatServletWebServerFactory()
            .getWebServer(DispatcherServletInitializer())
            .start()
    }
}

class SimpleAnnotationConfigWebApplicationContext : AnnotationConfigWebApplicationContext() {
    @Suppress("ACCIDENTAL_OVERRIDE")
    override fun setClassLoader(classLoader: ClassLoader) {
        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        super.onRefresh()
        TomcatServletWebServerFactory()
            .getWebServer(DispatcherServletInitializer())
            .start()
    }
}

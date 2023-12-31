package kr.disdong.spring.basic.core.helper

import kr.disdong.spring.basic.ApplicationConfiguration
import kr.disdong.spring.basic.module.post.PostController
import kr.disdong.spring.basic.module.post.PostService
import org.springframework.context.support.registerBean
import org.springframework.web.context.support.GenericWebApplicationContext

/**
 * BeanFactory 는 Spring Container 의 최상위 인터페이스로 스프링 빈을 관리합니다.
 * ApplicationContext 는 BeanFactory 의 모든 기능을 상속하여 제공하며, ApplicationEventPublisher, EnvironmentCapable 등 다양한 인터페이스를 상속하여 추가 기능을 제공합니다.
 * ApplicationContext 에는 여러 구현체가 있습니다.
 */
object ApplicationContextHelper {
    fun doGenericWeb(): GenericWebApplicationContext {
        val applicationContext = GenericWebApplicationContext()
        applicationContext.registerBean<PostController>()
        applicationContext.registerBean<PostService>()
        applicationContext.refresh()
        return applicationContext
    }

    /**
     * 스프링 컨테이너가 초기화될때 서블릿 컨테이너도 함께 실행되도록 합니다.
     */
    fun doSimpleGenericWeb(): SimpleGenericWebApplicationContext {
        val applicationContext = SimpleGenericWebApplicationContext()
        applicationContext.registerBean<PostController>()
        applicationContext.registerBean<PostService>()
        applicationContext.refresh()
        return applicationContext
    }

    fun doAnnotationConfigWeb(): SimpleAnnotationConfigWebApplicationContext {
        val applicationContext = SimpleAnnotationConfigWebApplicationContext()
        applicationContext.register(ApplicationConfiguration::class.java)
        applicationContext.refresh()
        return applicationContext
    }
}

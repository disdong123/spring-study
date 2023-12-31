package kr.disdong.spring.basic.core.initializer

import jakarta.servlet.ServletContext
import kr.disdong.spring.basic.core.helper.ApplicationContextHelper
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.web.servlet.DispatcherServlet

/**
 * DispatcherServlet 은 front-controller 로, 모든 요청을 가장 먼저 받아 적합한 컨트롤러에 위임해줍니다.
 */
class DispatcherServletInitializer : ServletContextInitializer {
    override fun onStartup(servletContext: ServletContext) {
        val applicationContext = ApplicationContextHelper.doGenericWeb()

        servletContext.addServlet(
            "dispatcherServlet",
            DispatcherServlet(applicationContext)
        )
            .addMapping("/*")
    }
}

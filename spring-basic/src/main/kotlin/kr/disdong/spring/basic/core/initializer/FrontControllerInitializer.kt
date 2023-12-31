package kr.disdong.spring.basic.core.initializer

import jakarta.servlet.ServletContext
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.disdong.spring.basic.core.helper.ApplicationContextHelper
import kr.disdong.spring.basic.module.post.PostController
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class FrontControllerInitializer : ServletContextInitializer {
    override fun onStartup(servletContext: ServletContext) {
        val applicationContext = ApplicationContextHelper.doGenericWeb()

        servletContext.addServlet(
            "frontController",
            object : HttpServlet() {
                override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
                    val method = req.method
                    val path = req.requestURI.substring(req.contextPath.length)

                    if (path == "/posts" && method == HttpMethod.GET.name()) {
                        val postController = applicationContext.getBean(PostController::class.java)
                        resp.contentType = MediaType.TEXT_PLAIN_VALUE
                        resp.writer.println(postController.getPosts())
                    } else {
                        resp.status = HttpStatus.NOT_FOUND.value()
                    }
                }
            }
        )
            .addMapping("/*")
    }
}

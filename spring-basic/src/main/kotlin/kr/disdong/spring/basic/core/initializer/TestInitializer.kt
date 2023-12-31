package kr.disdong.spring.basic.core.initializer

import jakarta.servlet.ServletContext
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class TestInitializer : ServletContextInitializer {
    override fun onStartup(servletContext: ServletContext) {
        servletContext.addServlet(
            "test",
            object : HttpServlet() {
                override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
                    resp.status = 200
                    resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                    resp.writer.println("test, ${req.getParameter("name")}")
                }
            }
        )
            .addMapping("/test")
    }
}

package com.example.tuicodechallenge.controller


import com.example.tuicodechallenge.controllers.UserRepositoriesController
import com.example.tuicodechallenge.services.UserRepositoriesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@WebMvcTest(controllers = [UserRepositoriesController])
@ContextConfiguration
class UserRepositoriesControllerTest extends Specification {

    @Autowired
    MockMvc mvc

    def "should respond with success to request with username provided"() {
        expect: "Status is 200 and the response is '[]'"
        mvc.perform(get("/user/test/repositories"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == "[]"
    }

    def "should respond with error to request with incorrect Accept header value"() {
        expect: "Status is 406"
        mvc.perform(get("/user/test/repositories").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
    }

    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        UserRepositoriesService registrationService() {
            return detachedMockFactory.Stub(UserRepositoriesService)
        }
    }
}

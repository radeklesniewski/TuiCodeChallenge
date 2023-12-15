package com.example.tuicodechallenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TuiCodeChallengeApplication.class})
@WebAppConfiguration
@TestPropertySource(locations = "/application.properties")
@WireMockTest(httpPort = 8081)
class TuiCodeChallengeApplicationIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assert.notNull(servletContext, "object should not be null");
    }

    @Test
    public void givenGreetURI_whenMockMVC_thenVerifyResponse() throws Exception {
        stubFor(get("/users/test/repos").willReturn(aResponse().withBody(objectMapper.readValue(new File("src/test/resources/payloads/listRepositoriesExampleResponse.json"), new ParametrizedType).toString()).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/user/test/repositories"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello World!!!"))
                .andReturn();
    }
}
package com.example.tuicodechallenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TuiCodeChallengeApplication.class})
@WebAppConfiguration
@TestPropertySource(locations = "/application.properties")
@WireMockTest(httpPort = 8081)
class TuiCodeChallengeApplicationIntegrationTest {
    private static final String PAYLOADS_LIST_REPOSITORIES_EXAMPLE_RESPONSE_JSON = "src/test/resources/payloads/listRepositoriesExampleResponse.json";
    private static final String PAYLOADS_LIST_BRANCHES_EXAMPLE_RESPONSE_JSON = "src/test/resources/payloads/listBranchesExampleResponse.json";
    private static final String USER_REPOSITORIES_API_ENDPOINT_PATH = "/user/%s/repositories";
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Value("${github.api.users.repos.path}")
    private String githubApiUsersReposPath;

    @Value("${github.api.repo.branches.path}")
    private String githubApiRepoBranchesPath;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesUserRepositoriesController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        Assert.notNull(servletContext, "object should not be null");
        Assert.isTrue(servletContext instanceof MockServletContext, "servletContext is created incorrectly");
        Assert.notNull(webApplicationContext.getBean("userRepositoriesController"), "userRepositoriesController bean is null");
    }

    @Test
    public void givenUserRepositoriesURI_whenMockMVC_thenVerifyResponse() throws Exception {
        String userName = "octocat";
        String repositoryName = "Hello-World";
        String branchNameMaster = "master";
        String branchNameTest = "test";

        stubFor(get(githubApiUsersReposPath.formatted(userName)).willReturn(aResponse().withBody(objectMapper.readTree(new File(PAYLOADS_LIST_REPOSITORIES_EXAMPLE_RESPONSE_JSON)).toString()).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        stubFor(get(githubApiRepoBranchesPath.formatted(userName, repositoryName)).willReturn(aResponse().withBody(objectMapper.readTree(new File(PAYLOADS_LIST_BRANCHES_EXAMPLE_RESPONSE_JSON)).toString()).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(USER_REPOSITORIES_API_ENDPOINT_PATH.formatted(userName)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].repositoryName").value(repositoryName))
                .andExpect(jsonPath("$[0].ownerLogin").value(userName))
                .andExpect(jsonPath("$[0].branchList[0].branchName").value(branchNameMaster))
                .andExpect(jsonPath("$[0].branchList[1].branchName").value(branchNameTest))
                .andReturn();
    }

    @Test
    public void givenUserRepositoriesURI_whenMockMVC_thenVerifyResponseForBareRepository() throws Exception {
        String userName = "octocat";
        String repositoryName = "Hello-World";

        stubFor(get(githubApiUsersReposPath.formatted(userName)).willReturn(aResponse().withBody(objectMapper.readTree(new File(PAYLOADS_LIST_REPOSITORIES_EXAMPLE_RESPONSE_JSON)).toString()).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        stubFor(get(githubApiRepoBranchesPath.formatted(userName, repositoryName)).willReturn(aResponse().withBody("[]").withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(USER_REPOSITORIES_API_ENDPOINT_PATH.formatted(userName)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].repositoryName").value(repositoryName))
                .andExpect(jsonPath("$[0].ownerLogin").value(userName))
                .andExpect(jsonPath("$[0].branchList").isEmpty())
                .andReturn();
    }

    @Test
    public void givenUserRepositoriesURI_whenMockMVC_thenVerifyResponseWhenGitHubBranchesAPIReturns404() throws Exception {
        String userName = "octocat";
        String repositoryName = "Hello-World";

        stubFor(get(githubApiUsersReposPath.formatted(userName)).willReturn(aResponse().withBody(objectMapper.readTree(new File(PAYLOADS_LIST_REPOSITORIES_EXAMPLE_RESPONSE_JSON)).toString()).withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
        stubFor(get(githubApiRepoBranchesPath.formatted(userName, repositoryName)).willReturn(notFound().withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(USER_REPOSITORIES_API_ENDPOINT_PATH.formatted(userName)))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Resource could not be found."))
                .andReturn();
    }

    @Test
    public void givenUserRepositoriesURI_whenMockMVC_thenVerifyResponseWhenGitHubRepositoriesAPIReturns404() throws Exception {
        String userName = "octocat";

        stubFor(get(githubApiUsersReposPath.formatted(userName)).willReturn(notFound().withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(USER_REPOSITORIES_API_ENDPOINT_PATH.formatted(userName)))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Provided username could not be found."))
                .andReturn();
    }

    @Test
    public void givenUserRepositoriesURI_whenMockMVC_thenVerifyResponseForIncorrectAcceptHeader() throws Exception {
        String userName = "octocat";

        this.mockMvc.perform(MockMvcRequestBuilders.get(USER_REPOSITORIES_API_ENDPOINT_PATH.formatted(userName)).accept(APPLICATION_XML))
                .andDo(print()).andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.status").value(406))
                .andExpect(jsonPath("$.message").value("No acceptable representation"))
                .andReturn();
    }

    @Test
    public void givenUserRepositoriesURI_whenMockMVC_thenVerifyResponseForUnexpectedError() throws Exception {
        String userName = "octocat";

        stubFor(get(githubApiUsersReposPath.formatted(userName)).willReturn(serverError().withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(USER_REPOSITORIES_API_ENDPOINT_PATH.formatted(userName)))
                .andDo(print()).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("500 Server Error: [no body]"))
                .andReturn();
    }
}
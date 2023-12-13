package com.example.tuicodechallenge.clients.github

import com.example.tuicodechallenge.TuiCodeChallengeApplication
import com.example.tuicodechallenge.http.client.github.GitHubClient
import com.example.tuicodechallenge.http.client.github.model.Branch
import com.example.tuicodechallenge.http.client.github.model.Repository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

@ContextConfiguration(classes = TuiCodeChallengeApplication)
@RestClientTest(GitHubClient.class)
class GitHubClientTest extends Specification {

    @Autowired (required = true)
    private GitHubClient githubClient

    @Autowired
    private MockRestServiceServer mockRestServiceServer

    private username = "testUser"
    private repository = "testRepo"

    def "when context is loaded then all expected beans are created"() {
        expect: "the GithubClient is created"
        githubClient
        githubClient.getRestClient()
    }

    def "should resolve all properties"() {
        expect:
        githubClient.getGithubApiHost() == "https://test.hostname"
        githubClient.getGithubApiUsersReposPath() == "/users/%s/repos"
        githubClient.getGithubApiRepoBranchesPath() == "/repos/%s/%s/branches"
    }

    def "should handle empty repositories list returned from API"() {
        given:
        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiUsersReposPath().formatted(username)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON))

        when:
        def repositories = githubClient.listUserRepositories(username)

        then:
        repositories.isEmpty()
    }

    def "should handle non empty repositories list returned from API"() {
        given:
        def responsePayloadFile = new File("src/test/resources/payloads/listRepositoriesExampleResponse.json")

        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiUsersReposPath().formatted(username)))
                .andRespond(withSuccess(responsePayloadFile.text, MediaType.APPLICATION_JSON))

        when:
        def repositories = githubClient.listUserRepositories(username)

        then:
        repositories == [new Repository("Hello-World", new Repository.Owner("octocat"), false)]
    }

    def "should handle empty branches list returned from API"() {
        given:
        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiRepoBranchesPath().formatted(username, repository)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON))

        when:
        def branches = githubClient.listRepositoryBranches(username, repository)

        then:
        branches.isEmpty()
    }

    def "should handle non empty branches list returned from API"() {
        given:
        def responsePayloadFile = new File("src/test/resources/payloads/listBranchesExampleResponse.json")

        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiRepoBranchesPath().formatted(username, repository)))
                .andRespond(withSuccess(responsePayloadFile.text, MediaType.APPLICATION_JSON))

        when:
        def branches = githubClient.listRepositoryBranches(username, repository)

        then:
        branches == [new Branch("master", new Branch.Commit("c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc")), new Branch("test", new Branch.Commit("f4de75cddec89b621bd8416dafcc33e4cf0cda82"))]
    }

    def "should fail when null parameter in method listRepositoryBranches is provided"() {
        when:
        githubClient.listRepositoryBranches(username, repository)

        then:
        thrown(IllegalArgumentException)

        where:
        username | repository
        "test"   | null
        null     | "test"
        null     | null
    }

    def "should fail when null parameter in method listUserRepositories is provided"() {
        when:
        githubClient.listUserRepositories(null)

        then:
        thrown(IllegalArgumentException)
    }

}
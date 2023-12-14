package com.example.tuicodechallenge.services.github.clients

import com.example.tuicodechallenge.TuiCodeChallengeApplication
import com.example.tuicodechallenge.exceptions.NotFoundException
import com.example.tuicodechallenge.services.github.client.GitHubClient
import com.example.tuicodechallenge.services.github.model.GitHubBranch
import com.example.tuicodechallenge.services.github.model.GithubRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import spock.lang.Specification

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound
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

    def "listUserRepositories should handle empty repositories list returned from API"() {
        given:
        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiUsersReposPath().formatted(username)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON))

        when:
        def repositories = githubClient.listUserRepositories(username)

        then:
        repositories.isEmpty()
    }

    def "listUserRepositories should handle non empty repositories list returned from API"() {
        given:
        def responsePayloadFile = new File("src/test/resources/payloads/listRepositoriesExampleResponse.json")

        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiUsersReposPath().formatted(username)))
                .andRespond(withSuccess(responsePayloadFile.text, MediaType.APPLICATION_JSON))

        when:
        def repositories = githubClient.listUserRepositories(username)

        then:
        repositories == [new GithubRepository("Hello-World", new GithubRepository.Owner("octocat"), false)]
    }

    def "listRepositoryBranches should handle empty branches list returned from API"() {
        given:
        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiRepoBranchesPath().formatted(username, repository)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON))

        when:
        def branches = githubClient.listRepositoryBranches(username, repository)

        then:
        branches.isEmpty()
    }

    def "listRepositoryBranches should handle non empty branches list returned from API"() {
        given:
        def responsePayloadFile = new File("src/test/resources/payloads/listBranchesExampleResponse.json")

        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiRepoBranchesPath().formatted(username, repository)))
                .andRespond(withSuccess(responsePayloadFile.text, MediaType.APPLICATION_JSON))

        when:
        def branches = githubClient.listRepositoryBranches(username, repository)

        then:
        branches == [new GitHubBranch("master", new GitHubBranch.Commit("c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc")), new GitHubBranch("test", new GitHubBranch.Commit("f4de75cddec89b621bd8416dafcc33e4cf0cda82"))]
    }

    def "should throw IllegalArgumentException when null parameter in method listRepositoryBranches is provided"() {
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

    def "should throw IllegalArgumentException when null parameter in method listUserRepositories is provided"() {
        when:
        githubClient.listUserRepositories(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "listRepositoryBranches should throw NotFoundException when API returns http 404 code"() {
        given:
        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiRepoBranchesPath().formatted(username, repository)))
                .andRespond(withResourceNotFound())

        when:
        githubClient.listRepositoryBranches(username, repository)

        then:
        thrown(NotFoundException)
    }

    def "listUserRepositories should throw NotFoundException when API returns http 404 code"() {
        given:
        mockRestServiceServer
                .expect(requestTo(githubClient.getGithubApiHost() + githubClient.getGithubApiUsersReposPath().formatted(username)))
                .andRespond(withResourceNotFound())

        when:
        githubClient.listUserRepositories(username)

        then:
        thrown(NotFoundException)
    }

}
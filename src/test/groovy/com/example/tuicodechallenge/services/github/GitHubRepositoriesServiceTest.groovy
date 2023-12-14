package com.example.tuicodechallenge.services.github

import com.example.tuicodechallenge.exceptions.NotFoundException
import com.example.tuicodechallenge.model.Repository
import com.example.tuicodechallenge.services.github.client.GitHubClient
import com.example.tuicodechallenge.services.github.model.GitHubBranch
import com.example.tuicodechallenge.services.github.model.GithubRepository
import spock.lang.Specification

class GitHubRepositoriesServiceTest extends Specification {

    def gitHubClient = Mock(GitHubClient)
    def gitHubRepositoriesService = new GitHubRepositoriesService(gitHubClient)

    def username = "testUser"

    def "should throw IllegalArgumentException when null parameter in method getUserNonForkedRepositories is provided"() {
        when:
        gitHubRepositoriesService.getUserNonForkedRepositories(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "getUserNonForkedRepositories should return empty repositories list for username without repositories"() {
        given:
        gitHubClient.listUserRepositories(username) >> []

        when:
        def repositories = gitHubRepositoriesService.getUserNonForkedRepositories(username)

        then:
        repositories.isEmpty()
    }

    def "getUserNonForkedRepositories should return empty repositories list for username with only forked repositories"() {
        given:
        gitHubClient.listUserRepositories(username) >> [new GithubRepository("testRepo", new GithubRepository.Owner(username), true)]

        when:
        def repositories = gitHubRepositoriesService.getUserNonForkedRepositories(username)

        then:
        repositories.isEmpty()
    }

    def "getUserNonForkedRepositories should return repositories list with empty branches for username with empty repository"() {
        given:
        def repoName = "testRepo"
        gitHubClient.listUserRepositories(username) >> [new GithubRepository(repoName, new GithubRepository.Owner(username), false)]
        gitHubClient.listRepositoryBranches(username, repoName) >> []

        when:
        def repositories = gitHubRepositoriesService.getUserNonForkedRepositories(username)

        then:
        repositories.containsAll([new Repository(repoName, username, [])])
    }

    def "getUserNonForkedRepositories should return non forked repositories for provided username"() {
        given:
        def repoName1 = "testRepo1"
        def repoName2 = "testRepo2"
        def repoName3 = "testRepo3"
        def commitSha1 = "testSha1"
        def commitSha2 = "testSha2"
        gitHubClient.listUserRepositories(username) >> [new GithubRepository(repoName1, new GithubRepository.Owner(username), false), new GithubRepository(repoName2, new GithubRepository.Owner(username), true), new GithubRepository(repoName3, new GithubRepository.Owner(username), false)]
        gitHubClient.listRepositoryBranches(username, repoName1) >> [new GitHubBranch("master", new GitHubBranch.Commit(commitSha1))]
        gitHubClient.listRepositoryBranches(username, repoName3) >> [new GitHubBranch("master", new GitHubBranch.Commit(commitSha2))]

        when:
        def repositories = gitHubRepositoriesService.getUserNonForkedRepositories(username)

        then:
        repositories.containsAll([new Repository(repoName1, username, [new Repository.Branch("master", commitSha1)]), new Repository(repoName3, username, [new Repository.Branch("master", commitSha2)])])
    }

    def "getUserNonForkedRepositories should rethrow exception when GitHubClient throws error"() {
        given:
        gitHubClient.listUserRepositories(username) >> {throw new NotFoundException("NotFound")}

        when:
        gitHubRepositoriesService.getUserNonForkedRepositories(username)

        then:
        thrown(NotFoundException)
    }

    def "should throw IllegalArgumentException when repository with null name in method listRepositoryBranches is provided"() {
        given:
        gitHubClient.listUserRepositories(username) >> [new GithubRepository(null, new GithubRepository.Owner(username), false)]

        when:
        gitHubRepositoriesService.getUserNonForkedRepositories(username)

        then:
        thrown(IllegalArgumentException)
    }
}

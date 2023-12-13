package com.example.tuicodechallenge.clients.github

import com.example.tuicodechallenge.TuiCodeChallengeApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration(classes = TuiCodeChallengeApplication)
class GitHubClientTest extends Specification {

    @Autowired (required = true)
    private GitHubClient githubClient


//    @Autowired (required = false)
//    private ApiController webController
//
//    def "when context is loaded then all expected beans are created1"() {
//        expect: "the WebController is created"
//        webController
//    }

    def "when context is loaded then all expected beans are created"() {
        expect: "the GithubClient is created"
        githubClient
    }

    def "should get property"() {
        expect:
        githubClient.getGithubApiHost() == "https://test.hostname"
    }

}
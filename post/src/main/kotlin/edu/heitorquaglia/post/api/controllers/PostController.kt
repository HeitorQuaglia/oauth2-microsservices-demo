package edu.heitorquaglia.post.api.controllers

import edu.heitorquaglia.post.api.exceptions.ResourceNotFoundException
import edu.heitorquaglia.post.api.requests.PostRequest
import edu.heitorquaglia.post.api.responses.EditorResponse
import edu.heitorquaglia.post.api.responses.PostDetailedResponse
import edu.heitorquaglia.post.api.responses.PostSummaryResponse
import edu.heitorquaglia.post.client.UserClient
import edu.heitorquaglia.post.client.UserResponse
import edu.heitorquaglia.post.domain.Post
import edu.heitorquaglia.post.domain.PostRepository
import edu.heitorquaglia.post.security.SecurityService
import edu.heitorquaglia.post.security.annotations.CanWritePosts
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/posts")
class PostController(
    val postRepository: PostRepository,
    val securityService: SecurityService,
    val userClient: UserClient
) {

    @GetMapping
    fun findAll(pageable: Pageable): Page<PostSummaryResponse?>? {
        val postPage: Page<Post> = postRepository.findAll(pageable)

        val postResponses = postPage.content
            .stream()
            .map(PostSummaryResponse::of)
            .collect(Collectors.toList())
        return PageImpl(postResponses, pageable, postPage.totalElements)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanWritePosts
    //@CanCreatePost
    fun create(@RequestBody postRequest: PostRequest): PostDetailedResponse? {
        val post = Post(editorId = securityService.userId, title = postRequest.title, content = postRequest.content)
        postRepository.save<Post>(post)
        return PostDetailedResponse.of(post)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): PostDetailedResponse? {
        val post: Post = postRepository.findById(id).orElseThrow { ResourceNotFoundException() }

        return userClient.findById(post.editorId)
            .map { userResponse: UserResponse? ->
                PostDetailedResponse.of(
                    post, EditorResponse.of(
                        userResponse!!
                    )
                )
            }
            .orElseGet { PostDetailedResponse.of(post) }
    }
}
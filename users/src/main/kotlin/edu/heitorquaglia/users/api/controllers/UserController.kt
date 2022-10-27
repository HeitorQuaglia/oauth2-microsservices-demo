package edu.heitorquaglia.users.api.controllers

import edu.heitorquaglia.users.api.exceptions.ResourceNotFoundException
import edu.heitorquaglia.users.api.requests.UserRequest
import edu.heitorquaglia.users.api.requests.UserUpdateRequest
import edu.heitorquaglia.users.api.responses.UserResponse
import edu.heitorquaglia.users.domain.UserEntity
import edu.heitorquaglia.users.domain.repository.UserRepository
import edu.heitorquaglia.users.security.annotations.CanReadUsers
import edu.heitorquaglia.users.security.annotations.CanWriteUsers
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors


@RestController
@RequestMapping("/users")
class UserController(val userRepository: UserRepository) {
    @GetMapping
    @CanReadUsers
    fun findAll(pageable: Pageable): Page<UserResponse> {
        val userEntityPage: Page<UserEntity> = userRepository.findAll(pageable)
        val userResponses = userEntityPage.content
            .stream()
            .map(UserResponse::of)
            .collect(Collectors.toList())
        return PageImpl(userResponses, pageable, userEntityPage.totalElements)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CanWriteUsers
    fun create(@RequestBody userRequest: UserRequest): UserResponse {
        return UserResponse.of(userRepository.save(userRequest.toEntity()))
    }

    @GetMapping("/{id}")
    @CanReadUsers
    fun findById(@PathVariable id: Long): UserResponse {
        return userRepository
            .findById(id)
            .map(UserResponse::of)
            .orElseThrow { throw ResourceNotFoundException() }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CanWriteUsers
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UserUpdateRequest
    ): UserResponse {
        val user: UserEntity = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado.") }
        request.update(user)
        userRepository.save(user)
        return UserResponse.of(user)
    }
}
package edu.heitorquaglia.users.api.controllersResourceNotFoundException

import edu.heitorquaglia.users.api.exceptions.ResourceNotFoundException
import edu.heitorquaglia.users.api.requests.MyUserRegisterRequest
import edu.heitorquaglia.users.api.requests.MyUserUpdatePasswordRequest
import edu.heitorquaglia.users.api.requests.MyUserUpdateRequest
import edu.heitorquaglia.users.api.responses.UserResponse
import edu.heitorquaglia.users.domain.UserEntity
import edu.heitorquaglia.users.domain.repository.UserRepository
import edu.heitorquaglia.users.security.annotations.CanEditMyUser
import edu.heitorquaglia.users.security.annotations.CanReadMyUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/user")
class MyUserController(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) {

    @CanReadMyUser
    @GetMapping
    fun me(@AuthenticationPrincipal userDetails: UserDetails): UserResponse? {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        return userRepository.findByEmail(userDetails.username)
            .map(UserResponse::of)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado.") }
    }

    @CanEditMyUser
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody request: MyUserUpdateRequest
    ) {
        val user: UserEntity = userRepository.findByEmail(userDetails.username)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado.") }
        request.update(user)
        userRepository.save(user)
    }

    @CanEditMyUser
    @PutMapping("/update-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updatePassword(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody request: MyUserUpdatePasswordRequest
    ) {
        val user: UserEntity = userRepository.findByEmail(userDetails.username)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado.") }
        user.password = passwordEncoder.encode(request.password)
        userRepository.save(user)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: MyUserRegisterRequest): UserResponse? {
        var user: UserEntity = request.toEntity()
        user.password = passwordEncoder.encode(request.password)
        user = userRepository.save(user)
        return UserResponse.of(user)
    }
}
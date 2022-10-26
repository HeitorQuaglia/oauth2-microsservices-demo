package edu.heitorquaglia.users.security.annotations

import org.springframework.security.access.prepost.PreAuthorize

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
annotation class CanEditMyUser
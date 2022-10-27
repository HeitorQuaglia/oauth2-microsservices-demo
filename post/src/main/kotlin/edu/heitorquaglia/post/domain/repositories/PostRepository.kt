package edu.heitorquaglia.post.domain.repositories

import edu.heitorquaglia.post.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>
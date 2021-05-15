package com.dataoct.technologies.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dataoct.technologies.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}

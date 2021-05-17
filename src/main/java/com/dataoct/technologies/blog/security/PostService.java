package com.dataoct.technologies.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dataoct.technologies.blog.dto.PostDto;
import com.dataoct.technologies.blog.exception.PostNotFoundException;
import com.dataoct.technologies.blog.model.Post;
import com.dataoct.technologies.blog.repository.PostRepository;
import com.dataoct.technologies.blog.service.AuthService;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PostService {

    @Autowired
    private AuthService authService;
    @Autowired
    private PostRepository postRepository;

    @Transactional
    public List<PostDto> showAllPosts() {
    	System.out.println(" PostService.java | public List<PostDto> showAllPosts() ");
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapFromPostToDto).collect(toList());
    }

    @Transactional
    public void createPost(PostDto postDto) {
    	System.out.println(" Start | PostService.java | public void createPost(PostDto postDto) ");
        Post post = mapFromDtoToPost(postDto);
    	System.out.println(" End   | PostService.java | public void createPost(PostDto postDto) ");
        postRepository.save(post);
    }

    @Transactional
    public PostDto readSinglePost(Long id) {
    	System.out.println(" PostService.java | public PostDto readSinglePost(Long id) ");
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("For id " + id));
        return mapFromPostToDto(post);
    }

    private PostDto mapFromPostToDto(Post post) {
    	System.out.println(" Start | PostService.java | private PostDto mapFromPostToDto(Post post) ");
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUsername());
        postDto.setSubject(post.getSubject());
        System.out.println((post.getSubject()));
        
        postDto.setCreatedOn(Instant.now());
        System.out.println(Instant.now());
    	
        System.out.println(" End   | PostService.java | private PostDto mapFromPostToDto(Post post) ");
        return postDto;
    }

    private Post mapFromDtoToPost(PostDto postDto) {
    	System.out.println(" Start | PostService.java | private Post mapFromDtoToPost(PostDto postDto) ");
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setSubject(postDto.getSubject());
        
        System.out.println((postDto.getSubject()));
        
        User loggedInUser = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        post.setCreatedOn(Instant.now());
        post.setUsername(loggedInUser.getUsername());
        post.setUpdatedOn(Instant.now());
    	System.out.println(" End   | PostService.java | private Post mapFromDtoToPost(PostDto postDto) ");
        return post;
    }
}

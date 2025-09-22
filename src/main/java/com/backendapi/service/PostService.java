package com.backendapi.service;

import com.backendapi.entity.Post;
import com.backendapi.entity.User;
import com.backendapi.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Post Service
 * 
 * Provides business logic for post management operations
 * Handles post CRUD operations and content management
 */
@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;

    /**
     * Get all published posts with pagination
     * @param pageable pagination information
     * @return page of published posts
     */
    @Transactional(readOnly = true)
    public Page<Post> getAllPublishedPosts(Pageable pageable) {
        return postRepository.findByIsPublishedTrueOrderByCreatedAtDesc(pageable);
    }

    /**
     * Get all posts with pagination (including unpublished)
     * @param pageable pagination information
     * @return page of all posts
     */
    @Transactional(readOnly = true)
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * Get post by ID
     * @param id post ID
     * @return post if found
     */
    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * Get posts by user with pagination
     * @param user the user
     * @param pageable pagination information
     * @return page of posts by user
     */
    @Transactional(readOnly = true)
    public Page<Post> getPostsByUser(User user, Pageable pageable) {
        return postRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    /**
     * Get published posts by user with pagination
     * @param user the user
     * @param pageable pagination information
     * @return page of published posts by user
     */
    @Transactional(readOnly = true)
    public Page<Post> getPublishedPostsByUser(User user, Pageable pageable) {
        return postRepository.findByUserAndIsPublishedTrueOrderByCreatedAtDesc(user, pageable);
    }

    /**
     * Create new post
     * @param post post entity
     * @return saved post
     */
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    /**
     * Update existing post
     * @param id post ID
     * @param postDetails updated post details
     * @return updated post
     */
    public Post updatePost(Long id, Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setIsPublished(postDetails.getIsPublished());

        return postRepository.save(post);
    }

    /**
     * Delete post by ID
     * @param id post ID
     */
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        postRepository.delete(post);
    }

    /**
     * Publish post
     * @param id post ID
     * @return updated post
     */
    public Post publishPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        post.setIsPublished(true);
        return postRepository.save(post);
    }

    /**
     * Unpublish post
     * @param id post ID
     * @return updated post
     */
    public Post unpublishPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        post.setIsPublished(false);
        return postRepository.save(post);
    }

    /**
     * Search published posts by title or content
     * @param searchTerm search term
     * @param pageable pagination information
     * @return page of matching posts
     */
    @Transactional(readOnly = true)
    public Page<Post> searchPublishedPosts(String searchTerm, Pageable pageable) {
        return postRepository.searchPublishedPosts(searchTerm, pageable);
    }

    /**
     * Get recent published posts
     * @param pageable pagination information
     * @return list of recent posts
     */
    @Transactional(readOnly = true)
    public List<Post> getRecentPublishedPosts(Pageable pageable) {
        return postRepository.findRecentPublishedPosts(pageable);
    }

    /**
     * Count posts by user
     * @param user the user
     * @return number of posts by user
     */
    @Transactional(readOnly = true)
    public long countPostsByUser(User user) {
        return postRepository.countByUser(user);
    }

    /**
     * Count published posts by user
     * @param user the user
     * @return number of published posts by user
     */
    @Transactional(readOnly = true)
    public long countPublishedPostsByUser(User user) {
        return postRepository.countByUserAndIsPublishedTrue(user);
    }
}

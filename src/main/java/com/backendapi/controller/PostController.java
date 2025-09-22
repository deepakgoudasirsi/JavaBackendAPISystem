package com.backendapi.controller;

import com.backendapi.dto.PostRequest;
import com.backendapi.entity.Post;
import com.backendapi.entity.User;
import com.backendapi.service.AuthService;
import com.backendapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Post Controller
 * 
 * Handles post management endpoints
 * Provides CRUD operations for post entities
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Management", description = "Post management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    /**
     * Get all published posts with pagination
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @param sortBy sort field (default: createdAt)
     * @param sortDir sort direction (default: desc)
     * @return page of published posts
     */
    @GetMapping
    @Operation(summary = "Get published posts", description = "Retrieve all published posts with pagination")
    public ResponseEntity<Page<Post>> getAllPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Post> posts = postService.getAllPublishedPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    /**
     * Get all posts with pagination (including unpublished)
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @param sortBy sort field (default: createdAt)
     * @param sortDir sort direction (default: desc)
     * @return page of all posts
     */
    @GetMapping("/all")
    @Operation(summary = "Get all posts", description = "Retrieve all posts including unpublished")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Post>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Post> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    /**
     * Get post by ID
     * @param id post ID
     * @return post if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieve post by ID")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get posts by current user
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @param publishedOnly show only published posts (default: false)
     * @return page of user's posts
     */
    @GetMapping("/my-posts")
    @Operation(summary = "Get my posts", description = "Retrieve current user's posts")
    public ResponseEntity<Page<Post>> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean publishedOnly) {
        
        User currentUser = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Post> posts = publishedOnly ? 
            postService.getPublishedPostsByUser(currentUser, pageable) :
            postService.getPostsByUser(currentUser, pageable);
        
        return ResponseEntity.ok(posts);
    }

    /**
     * Create new post
     * @param postRequest post data
     * @return created post
     */
    @PostMapping
    @Operation(summary = "Create post", description = "Create a new post")
    public ResponseEntity<Post> createPost(@Valid @RequestBody PostRequest postRequest) {
        User currentUser = authService.getCurrentUser();
        
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setIsPublished(postRequest.getIsPublished());
        post.setUser(currentUser);
        
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    /**
     * Update post
     * @param id post ID
     * @param postRequest updated post data
     * @return updated post
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Update post information")
    @PreAuthorize("hasRole('ADMIN') or @postService.getPostById(#id).get().user.username == authentication.name")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequest postRequest) {
        try {
            Post postDetails = new Post();
            postDetails.setTitle(postRequest.getTitle());
            postDetails.setContent(postRequest.getContent());
            postDetails.setIsPublished(postRequest.getIsPublished());
            
            Post updatedPost = postService.updatePost(id, postDetails);
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Update failed", "message", e.getMessage()));
        }
    }

    /**
     * Delete post
     * @param id post ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Delete post by ID")
    @PreAuthorize("hasRole('ADMIN') or @postService.getPostById(#id).get().user.username == authentication.name")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Delete failed", "message", e.getMessage()));
        }
    }

    /**
     * Publish post
     * @param id post ID
     * @return updated post
     */
    @PutMapping("/{id}/publish")
    @Operation(summary = "Publish post", description = "Publish a post")
    @PreAuthorize("hasRole('ADMIN') or @postService.getPostById(#id).get().user.username == authentication.name")
    public ResponseEntity<?> publishPost(@PathVariable Long id) {
        try {
            Post post = postService.publishPost(id);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Publish failed", "message", e.getMessage()));
        }
    }

    /**
     * Unpublish post
     * @param id post ID
     * @return updated post
     */
    @PutMapping("/{id}/unpublish")
    @Operation(summary = "Unpublish post", description = "Unpublish a post")
    @PreAuthorize("hasRole('ADMIN') or @postService.getPostById(#id).get().user.username == authentication.name")
    public ResponseEntity<?> unpublishPost(@PathVariable Long id) {
        try {
            Post post = postService.unpublishPost(id);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unpublish failed", "message", e.getMessage()));
        }
    }

    /**
     * Search published posts
     * @param searchTerm search term
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @return page of matching posts
     */
    @GetMapping("/search")
    @Operation(summary = "Search posts", description = "Search published posts by title or content")
    public ResponseEntity<Page<Post>> searchPosts(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postService.searchPublishedPosts(searchTerm, pageable);
        return ResponseEntity.ok(posts);
    }

    /**
     * Get recent posts
     * @param limit maximum number of posts (default: 5)
     * @return list of recent posts
     */
    @GetMapping("/recent")
    @Operation(summary = "Get recent posts", description = "Get recent published posts")
    public ResponseEntity<List<Post>> getRecentPosts(@RequestParam(defaultValue = "5") int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        List<Post> posts = postService.getRecentPublishedPosts(pageable);
        return ResponseEntity.ok(posts);
    }
}

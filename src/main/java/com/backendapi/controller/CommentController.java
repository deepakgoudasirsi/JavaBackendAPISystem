package com.backendapi.controller;

import com.backendapi.dto.CommentRequest;
import com.backendapi.entity.Comment;
import com.backendapi.entity.Post;
import com.backendapi.entity.User;
import com.backendapi.service.AuthService;
import com.backendapi.service.CommentService;
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
 * Comment Controller
 * 
 * Handles comment management endpoints
 * Provides CRUD operations for comment entities
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment Management", description = "Comment management APIs")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostService postService;

    /**
     * Get all comments with pagination
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @param sortBy sort field (default: createdAt)
     * @param sortDir sort direction (default: desc)
     * @return page of comments
     */
    @GetMapping
    @Operation(summary = "Get all comments", description = "Retrieve all comments with pagination")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Comment>> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Comment> comments = commentService.getAllComments(pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Get comment by ID
     * @param id comment ID
     * @return comment if found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get comment by ID", description = "Retrieve comment by ID")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            return ResponseEntity.ok(comment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get comments by post
     * @param postId post ID
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @return page of comments for the post
     */
    @GetMapping("/post/{postId}")
    @Operation(summary = "Get comments by post", description = "Retrieve comments for a specific post")
    public ResponseEntity<Page<Comment>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Optional<Post> post = postService.getPostById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.getCommentsByPost(post.get(), pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Get comments by current user
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @return page of user's comments
     */
    @GetMapping("/my-comments")
    @Operation(summary = "Get my comments", description = "Retrieve current user's comments")
    public ResponseEntity<Page<Comment>> getMyComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        User currentUser = authService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.getCommentsByUser(currentUser, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Create new comment
     * @param postId post ID
     * @param commentRequest comment data
     * @return created comment
     */
    @PostMapping("/post/{postId}")
    @Operation(summary = "Create comment", description = "Create a new comment on a post")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @Valid @RequestBody CommentRequest commentRequest) {
        try {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Post not found"));
            }
            
            User currentUser = authService.getCurrentUser();
            
            Comment comment = new Comment();
            comment.setContent(commentRequest.getContent());
            comment.setUser(currentUser);
            comment.setPost(post.get());
            
            Comment createdComment = commentService.createComment(comment);
            return ResponseEntity.ok(createdComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Comment creation failed", "message", e.getMessage()));
        }
    }

    /**
     * Update comment
     * @param id comment ID
     * @param commentRequest updated comment data
     * @return updated comment
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update comment", description = "Update comment information")
    @PreAuthorize("hasRole('ADMIN') or @commentService.getCommentById(#id).get().user.username == authentication.name")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @Valid @RequestBody CommentRequest commentRequest) {
        try {
            Comment commentDetails = new Comment();
            commentDetails.setContent(commentRequest.getContent());
            
            Comment updatedComment = commentService.updateComment(id, commentDetails);
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Update failed", "message", e.getMessage()));
        }
    }

    /**
     * Delete comment
     * @param id comment ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment", description = "Delete comment by ID")
    @PreAuthorize("hasRole('ADMIN') or @commentService.getCommentById(#id).get().user.username == authentication.name")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(Map.of("message", "Comment deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Delete failed", "message", e.getMessage()));
        }
    }

    /**
     * Search comments
     * @param searchTerm search term
     * @param page page number (default: 0)
     * @param size page size (default: 10)
     * @return page of matching comments
     */
    @GetMapping("/search")
    @Operation(summary = "Search comments", description = "Search comments by content")
    public ResponseEntity<Page<Comment>> searchComments(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.searchComments(searchTerm, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * Get recent comments
     * @param limit maximum number of comments (default: 10)
     * @return list of recent comments
     */
    @GetMapping("/recent")
    @Operation(summary = "Get recent comments", description = "Get recent comments")
    public ResponseEntity<List<Comment>> getRecentComments(@RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        Page<Comment> comments = commentService.getRecentComments(pageable);
        return ResponseEntity.ok(comments.getContent());
    }
}

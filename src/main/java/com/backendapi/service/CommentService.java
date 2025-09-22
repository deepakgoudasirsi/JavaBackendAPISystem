package com.backendapi.service;

import com.backendapi.entity.Comment;
import com.backendapi.entity.Post;
import com.backendapi.entity.User;
import com.backendapi.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Comment Service
 * 
 * Provides business logic for comment management operations
 * Handles comment CRUD operations and content management
 */
@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Get all comments with pagination
     * @param pageable pagination information
     * @return page of comments
     */
    @Transactional(readOnly = true)
    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    /**
     * Get comment by ID
     * @param id comment ID
     * @return comment if found
     */
    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    /**
     * Get comments by post with pagination
     * @param post the post
     * @param pageable pagination information
     * @return page of comments for the post
     */
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByPost(Post post, Pageable pageable) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable);
    }

    /**
     * Get comments by user with pagination
     * @param user the user
     * @param pageable pagination information
     * @return page of comments by user
     */
    @Transactional(readOnly = true)
    public Page<Comment> getCommentsByUser(User user, Pageable pageable) {
        return commentRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    /**
     * Get recent comments
     * @param pageable pagination information
     * @return page of recent comments
     */
    @Transactional(readOnly = true)
    public Page<Comment> getRecentComments(Pageable pageable) {
        return commentRepository.findRecentComments(pageable);
    }

    /**
     * Create new comment
     * @param comment comment entity
     * @return saved comment
     */
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * Update existing comment
     * @param id comment ID
     * @param commentDetails updated comment details
     * @return updated comment
     */
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));

        comment.setContent(commentDetails.getContent());

        return commentRepository.save(comment);
    }

    /**
     * Delete comment by ID
     * @param id comment ID
     */
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
        commentRepository.delete(comment);
    }

    /**
     * Search comments by content
     * @param searchTerm search term
     * @param pageable pagination information
     * @return page of matching comments
     */
    @Transactional(readOnly = true)
    public Page<Comment> searchComments(String searchTerm, Pageable pageable) {
        return commentRepository.searchComments(searchTerm, pageable);
    }

    /**
     * Count comments by post
     * @param post the post
     * @return number of comments for the post
     */
    @Transactional(readOnly = true)
    public long countCommentsByPost(Post post) {
        return commentRepository.countByPost(post);
    }

    /**
     * Count comments by user
     * @param user the user
     * @return number of comments by user
     */
    @Transactional(readOnly = true)
    public long countCommentsByUser(User user) {
        return commentRepository.countByUser(user);
    }
}

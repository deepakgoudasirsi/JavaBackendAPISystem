package com.backendapi.repository;

import com.backendapi.entity.Comment;
import com.backendapi.entity.Post;
import com.backendapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity
 * 
 * Provides data access methods for Comment operations including:
 * - Basic CRUD operations
 * - Pagination support
 * - Post-specific comment retrieval
 * - User-specific comment retrieval
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find comments by post with pagination
     * @param post the post to filter by
     * @param pageable pagination information
     * @return page of comments for the post
     */
    Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);

    /**
     * Find comments by user with pagination
     * @param user the user to filter by
     * @param pageable pagination information
     * @return page of comments by the user
     */
    Page<Comment> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Find recent comments
     * @param pageable pagination information
     * @return page of recent comments
     */
    @Query("SELECT c FROM Comment c ORDER BY c.createdAt DESC")
    Page<Comment> findRecentComments(Pageable pageable);

    /**
     * Count comments by post
     * @param post the post to count comments for
     * @return number of comments for the post
     */
    long countByPost(Post post);

    /**
     * Count comments by user
     * @param user the user to count comments for
     * @return number of comments by the user
     */
    long countByUser(User user);

    /**
     * Find comments containing specific text
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return page of comments containing the search term
     */
    @Query("SELECT c FROM Comment c WHERE " +
           "LOWER(c.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Comment> searchComments(@Param("searchTerm") String searchTerm, Pageable pageable);
}

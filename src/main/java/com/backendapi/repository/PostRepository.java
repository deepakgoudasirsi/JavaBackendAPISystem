package com.backendapi.repository;

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
 * Repository interface for Post entity
 * 
 * Provides data access methods for Post operations including:
 * - Basic CRUD operations
 * - Pagination support
 * - Custom queries for content search
 * - User-specific post retrieval
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all published posts with pagination
     * @param pageable pagination information
     * @return page of published posts
     */
    Page<Post> findByIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find posts by user with pagination
     * @param user the user to filter by
     * @param pageable pagination information
     * @return page of posts by the user
     */
    Page<Post> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Find published posts by user with pagination
     * @param user the user to filter by
     * @param pageable pagination information
     * @return page of published posts by the user
     */
    Page<Post> findByUserAndIsPublishedTrueOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Search posts by title or content
     * @param searchTerm the term to search for
     * @param pageable pagination information
     * @return page of posts matching the search term
     */
    @Query("SELECT p FROM Post p WHERE " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "p.isPublished = true")
    Page<Post> searchPublishedPosts(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find recent posts
     * @param limit maximum number of posts to return
     * @return list of recent published posts
     */
    @Query("SELECT p FROM Post p WHERE p.isPublished = true ORDER BY p.createdAt DESC")
    List<Post> findRecentPublishedPosts(Pageable pageable);

    /**
     * Count posts by user
     * @param user the user to count posts for
     * @return number of posts by the user
     */
    long countByUser(User user);

    /**
     * Count published posts by user
     * @param user the user to count published posts for
     * @return number of published posts by the user
     */
    long countByUserAndIsPublishedTrue(User user);
}

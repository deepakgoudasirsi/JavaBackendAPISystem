package com.backendapi.service;

import com.backendapi.entity.User;
import com.backendapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service
 * 
 * Provides business logic for user management operations
 * Handles user CRUD operations and business rules
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users with pagination
     * @param pageable pagination information
     * @return page of users
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Get user by ID
     * @param id user ID
     * @return user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get user by username
     * @param username username
     * @return user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get user by email
     * @param email email address
     * @return user if found
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Create new user
     * @param user user entity
     * @return saved user
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Update existing user
     * @param id user ID
     * @param userDetails updated user details
     * @return updated user
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setIsActive(userDetails.getIsActive());

        return userRepository.save(user);
    }

    /**
     * Delete user by ID
     * @param id user ID
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    /**
     * Deactivate user account
     * @param id user ID
     * @return updated user
     */
    public User deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(false);
        return userRepository.save(user);
    }

    /**
     * Activate user account
     * @param id user ID
     * @return updated user
     */
    public User activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(true);
        return userRepository.save(user);
    }

    /**
     * Search users by name
     * @param name partial name to search for
     * @return list of matching users
     */
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String name) {
        return userRepository.findUsersByNameContaining(name);
    }

    /**
     * Get active users by role
     * @param role user role
     * @return list of active users with specified role
     */
    @Transactional(readOnly = true)
    public List<User> getActiveUsersByRole(User.Role role) {
        return userRepository.findActiveUsersByRole(role);
    }

    /**
     * Check if username exists
     * @param username username to check
     * @return true if exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if email exists
     * @param email email to check
     * @return true if exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

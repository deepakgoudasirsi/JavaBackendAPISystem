package com.backendapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Comment Request DTO
 * 
 * Data transfer object for comment creation and update requests
 */
@Data
public class CommentRequest {
    
    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content must not exceed 1000 characters")
    private String content;
}

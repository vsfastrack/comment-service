package com.tech.bee.postservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "comments")
public class CommentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment_id")
    private String commentId;
    private String identifier = UUID.randomUUID().toString();
    @Type(type = "text")
    @Column(columnDefinition = "text")
    private String content;
    private String postId;
    @OneToMany(mappedBy = "parentComment" , fetch = FetchType.LAZY)
    private List<ReplyEntity> replies = new ArrayList<>();
    private String createdBy;
    @CreationTimestamp
    private LocalDateTime createdWhen;
    @UpdateTimestamp
    private LocalDateTime lastModifiedWhen;
}

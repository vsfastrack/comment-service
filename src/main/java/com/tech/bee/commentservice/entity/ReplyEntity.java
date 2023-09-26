package com.tech.bee.commentservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Table(name="replies")
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reply_id")
    private String replyId;
    private String identifier = UUID.randomUUID().toString();
    @Type(type = "text")
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;
    @ManyToOne
    @JoinColumn(name = "parent_reply_id")
    private ReplyEntity parentReply;
    @OneToMany(mappedBy = "parentReply" , fetch = FetchType.LAZY)
    private List<ReplyEntity> replies = new ArrayList<>();
    private String createdBy;
    @CreationTimestamp
    private LocalDateTime createdWhen;
    @UpdateTimestamp
    private LocalDateTime lastModifiedWhen;
}

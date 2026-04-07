package org.example.gradeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appeal_messages")
@Getter
@Setter
public class AppealMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appeal_id")
    private Appeal appeal;

    @Column(nullable = false)
    private Long senderId;


    @Column(nullable = false)
    private String senderRole;


    private String message;
}
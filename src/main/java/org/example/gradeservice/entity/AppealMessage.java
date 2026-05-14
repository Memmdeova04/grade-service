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
    Appeal appeal;

    @Column(nullable = false)
    Long senderId;


    @Column(nullable = false)
    String senderRole;


    String message;
}
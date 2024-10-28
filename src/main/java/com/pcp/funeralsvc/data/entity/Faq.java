package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@Table(name = "faq")
@NoArgsConstructor
@AllArgsConstructor
public class Faq extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long faqId;

    private String question;

    @Lob
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private FaqCategory category;
}

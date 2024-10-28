package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reservation_res_history")
public class ReservationResHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_history_id")
    private Long serviceHistoryId;

    private String translatedCertificateNo;
    private String funeralCertificateNo;;

    private Long uid;

    @Column(name = "funeralDate")
    String funeralDate;

    @Column(name = "funeralPlace")
    String funeralPlace;

    @Column(name = "reservation_type")
    Integer reservationType;

    @Column(name = "reservation_state")
    String reservationState;

    @CreationTimestamp
    //@CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

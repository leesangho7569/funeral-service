package com.pcp.funeralsvc.data.entity;

import com.pcp.funeralsvc.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "reservation_history")
public class ReservationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_history_id")
    private Long reservationHistoryId;

    @Column(name = "translated_certificate_no")
    private String translatedCertificateNo;

    private Long uid;

    @Column(name = "funeral_certificate_no")
    private String funeralCertificateNo;

    @Convert(converter = StringListConverter.class)
    @Column(name = "favorite_place_names")
    private List<String> favoritePlaceNames;

    @Column(name = "vehicle_svc_support")
    private Boolean vehicleSvcSupport;

    @CreationTimestamp
    //@CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "reservation_type")
    private String actionType;


}

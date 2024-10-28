package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reservation_res")
public class ReservationRes extends BaseEntity{

    @Id
    @Column(name = "translated_certificate_no")
    private String translatedCertificateNo;
    Long uid;
    @Column(name = "funeral_certificate_no")
    String funeralCertificateNo;

    @Column(name = "funeralDate")
    String funeralDate;

    @Column(name = "funeralPlace")
    String funeralPlace;

    @Builder.Default
    @Column(name = "reservation_type")
    Integer reservationType = 0;

    @Builder.Default
    @Column(name = "reservation_state")
    String reservationState = "처음 상태";
}

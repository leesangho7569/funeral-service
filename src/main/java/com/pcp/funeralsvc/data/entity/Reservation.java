package com.pcp.funeralsvc.data.entity;

import com.pcp.funeralsvc.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "reservation")
public class Reservation extends BaseEntity{
    @Id
    @Column(name = "translated_certificate_no")
    private String translatedCertificateNo;

    @Column(name = "favorite_place_names")
    private String favoritePlaceNames;

    private Long uid;

    @Column(name = "funeral_certificate_no")
    private String funeralCertificateNo;

    @Column(name = "vehicle_svc_support")
    private Boolean vehicleSvcSupport;

    public Reservation() {
    }

}

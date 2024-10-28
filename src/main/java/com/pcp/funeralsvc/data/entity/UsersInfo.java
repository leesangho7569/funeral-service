package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_info")
public class UsersInfo extends BaseEntity{
    @Id
    private Long uid;

//    @Column(name = "translated_certificate_no")
//    private String translatedCertificateNo;

    @Column(name = "customer_name")
    String customerName;
    String mobile;

//    @Column(name = "funeral_certificate_no")
//    String funeralCertificateNo;

    @Column(name = "pass_app_id")
    String passAppId;
}
package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.dto.request.UserInfoServiceDto;

import com.pcp.funeralsvc.data.entity.ReservationRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationResRepository extends JpaRepository<ReservationRes, String> {
    /*
    @Query( value = "SELECT new com.pcp.funeralsvc.data.dto.request.UserInfoServiceDto(u.uid, u.customerName, u.mobile, u.passAppId, r.translatedCertificateNo, r.funeralCertificateNo)" +
            "FROM users_info u " +
            "JoIN service s ON u.uid = s.uid " +
            "WHERE u.uid = :uid AND s.translated_certificate_no = :translatedCertificateNo", nativeQuery = true)
     */

    @Query( "SELECT new com.pcp.funeralsvc.data.dto.request.UserInfoServiceDto(u.uid, u.customerName, u.mobile, u.passAppId, s.translatedCertificateNo, s.funeralCertificateNo)" +
            "FROM ReservationRes s " +
            "JoIN UsersInfo u ON u.uid = s.uid " +
            "WHERE u.uid = :uid AND s.translatedCertificateNo = :translatedCertificateNo")
    List<UserInfoServiceDto> findActionsByUid(@Param("uid") Long uid, @Param("translatedCertificateNo") String translatedCertificateNo);
}

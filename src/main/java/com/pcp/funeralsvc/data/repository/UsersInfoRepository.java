package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.Place;
import com.pcp.funeralsvc.data.entity.Region;
import com.pcp.funeralsvc.data.entity.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersInfoRepository extends JpaRepository<UsersInfo, Long> {

//    Optional<UsersInfo> findByUid(Long uid);
    Optional<UsersInfo> findByPassAppId(String passAppId);
}

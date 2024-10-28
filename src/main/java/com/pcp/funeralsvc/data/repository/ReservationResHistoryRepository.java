package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.ReservationResHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationResHistoryRepository extends JpaRepository<ReservationResHistory, String> {
}

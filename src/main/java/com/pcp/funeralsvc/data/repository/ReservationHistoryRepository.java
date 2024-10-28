package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.Reservation;
import com.pcp.funeralsvc.data.entity.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
}

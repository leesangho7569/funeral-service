package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.Region;
import com.pcp.funeralsvc.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}

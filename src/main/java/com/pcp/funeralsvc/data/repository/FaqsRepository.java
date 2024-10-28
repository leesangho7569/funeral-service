package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqsRepository extends JpaRepository<Faq, Long> {

}

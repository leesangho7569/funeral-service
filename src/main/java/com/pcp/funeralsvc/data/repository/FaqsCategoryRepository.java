package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.FaqCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqsCategoryRepository extends JpaRepository<FaqCategory, Long> {
}

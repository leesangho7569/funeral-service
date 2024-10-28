package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByRegionName(String regionName);

//    @Query("select u from FuneralRegion u where u.createdAt > :since")
//    Page<FuneralRegion> findRecentFuneralRegions(@Param("since")LocalDateTime since, Pageable pageable);

    //Page<FuneralRegion> findByRegion_nameContaining(String regionName, Pageable pageable);

    /*
    Optional<User> findById(String email);

    User save(User user);

    void delete(User user);

    List<User> findByNameLike(String keyword);

    List<User> findByNameLikeOrderByNameDesc(String keyword);

    List<User> findByNameLikeOrderByNameAscEmailDesc(String keyword);

    List<User> findByNameLike(String keyword, Sort sort);

    List<User> findByNameLike(String keyword, Pageable pageable);

    Page<User> findByEmailLike(String keyword, Pageable pageable);

    @Query("select u from User u where u.createDate > :since order by u.createDate desc")
    List<User> findRecentUsers(@Param("since") LocalDateTime since);

    @Query("select u from User u where u.createDate > :since")
    List<User> findRecentUsers(@Param("since") LocalDateTime since, Sort sort);

    @Query("select u from User u where u.createDate > :since")
    Page<User> findRecentUsers(@Param("since") LocalDateTime since, Pageable pageable);
     */
}

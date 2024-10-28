package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.dto.response.PlaceDetailResponseDto;
import com.pcp.funeralsvc.data.dto.response.PlaceWithFavoriteResponseDto;
import com.pcp.funeralsvc.data.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByPlaceName(String placeName);

    @Query("SELECT new com.pcp.funeralsvc.data.dto.response.PlaceDetailResponseDto(p.placeId, p.placeName, p.address, r.regionName) " +
            "FROM Place p LEFT JOIN p.region r")
    List<PlaceDetailResponseDto> findAllPlacesWithRegion();

    // 모든 place와 region을 검색 하면서, uid에 해당하는 즐겨찾기 정보가 있으면 함께 조회
    @Query("SELECT new com.pcp.funeralsvc.data.dto.response.PlaceWithFavoriteResponseDto(p.placeId, p.placeName, p.address, r.regionName, " +
            "CASE WHEN fp.favorite IS NOT NULL THEN fp.favorite ELSE false END) " +
            "FROM Place p " +
            "JOIN p.region r " +
            "LEFT JOIN FavoritePlace fp ON p.placeId = fp.place.placeId AND fp.id.uid = :uid")
    List<PlaceWithFavoriteResponseDto> findAllPlacesWithFavoriteByUid(@Param("uid") Long uid);


}

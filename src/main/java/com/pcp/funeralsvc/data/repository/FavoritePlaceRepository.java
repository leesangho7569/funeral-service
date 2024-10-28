package com.pcp.funeralsvc.data.repository;

import com.pcp.funeralsvc.data.dto.response.FavoritePlaceResponseDto;
import com.pcp.funeralsvc.data.entity.FavoritePlace;
import com.pcp.funeralsvc.data.entity.FavoritePlacePkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritePlaceRepository extends JpaRepository<FavoritePlace, FavoritePlacePkId> {

    @Query("SELECT new com.pcp.funeralsvc.data.dto.response.FavoritePlaceResponseDto(p.placeId, p.placeName, p.address, r.regionName, fp.favorite )" +
            "FROM FavoritePlace fp " +
            "JOIN fp.place p " +
            "LEFT JOIN p.region r " +
            "WHERE fp.id.uid = :uid")
    List<FavoritePlaceResponseDto> findAllFavoritePlacesByUid(@Param("uid") Long uid);

    @Query("SELECT p.placeName " +
            "FROM FavoritePlace  fp " +
            "JOIN fp.place p " +
            "WHERE fp.id.uid = :uid AND fp.favorite = true")
    List<String> findFavoritePlaceByUid(@Param("uid") Long uid);

}

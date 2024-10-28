package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "favorite_place")
public class FavoritePlace extends BaseEntity{

    @EmbeddedId
    private FavoritePlacePkId id;

    @ManyToOne
    @MapsId("placeId")
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private  Boolean favorite;

    public FavoritePlace(FavoritePlacePkId id, Place place, Boolean favorite) {
        this.id = id;
        this.place = place;
        this.favorite = favorite;
    }
}

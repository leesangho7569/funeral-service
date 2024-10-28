package com.pcp.funeralsvc.data.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class FavoritePlacePkId implements Serializable {

    private  Long uid;

    private Long placeId;

    public FavoritePlacePkId() {
    }

    public FavoritePlacePkId(Long uid, Long placeId) {
        this.uid = uid;
        this.placeId = placeId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritePlacePkId that = (FavoritePlacePkId) o;
        return Objects.equals(uid, that.uid) && Objects.equals(placeId, that.placeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, placeId);
    }
}

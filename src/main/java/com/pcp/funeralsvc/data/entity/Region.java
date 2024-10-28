package com.pcp.funeralsvc.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "region")
public class Region extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long regionId;

    @NotNull
    @Size(min=2, max=30, message = "지역 명은 2자 이상 30자 이하 입니다.")
    @Column(name = "region_name")
    private String regionName;

//    @JsonIgnore
//    @OneToMany(mappedBy = "placeId", fetch = FetchType.LAZY)
//    private List<Place> funeralPlaces = new ArrayList<>();;

    public Region() {
    }
    public Region(String regionName) {
        this.regionName = regionName;
    }

    /*
    수도권ㅣ
    강원도
    충청도
    전라도
    경상도
    그 외 지역
    제주도
    서울 특별시
    부산 광역시
    대구 광역시
    인천 광역시
    광주 광역시
    대전 광역시
    울산 광역시
    세종 특별자치시
     */
}

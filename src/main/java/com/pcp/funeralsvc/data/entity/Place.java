package com.pcp.funeralsvc.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pcp.funeralsvc.data.dto.request.PlaceDetailRequestDto;
import com.pcp.funeralsvc.data.dto.request.PlaceRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "place")
@AllArgsConstructor
public class Place extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long placeId;
    @Column(name = "place_name")
    private String placeName;
    private String address;
    private String introduction;
    @Column(name = "image_file")
    private String imageFile;
    @Column(name = "image_data")
//    @Lob
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "region_id")
    @JsonIgnore
    private Region region;

    public Place() {
    }

    public void updatePlace(PlaceDetailRequestDto placeDetailRequestDto, Region region, MultipartFile file) throws IOException {

        this.placeName =  StringUtils.isBlank(placeDetailRequestDto.getPlaceName()) ? this.placeName : placeDetailRequestDto.getPlaceName();
        this.address =  StringUtils.isBlank(placeDetailRequestDto.getAddress()) ? this.address : placeDetailRequestDto.getAddress();
        this.introduction =  StringUtils.isBlank(placeDetailRequestDto.getIntroduction()) ? this.introduction : placeDetailRequestDto.getIntroduction();
        this.region =  (region == null) ? this.region : region;

        if(file != null && !file.isEmpty()){
            this.imageData = file.getBytes();
            this.imageFile = file.getOriginalFilename();
        }

    }


    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FuneralPlace funeralPlace = (FuneralPlace) o;
        return Objects.equals(placeId, funeralPlace.placeId) && Objects.equals(placeName, funeralPlace.placeName)
                && Objects.equals(address, funeralPlace.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, placeName, address);
    }

     */
}

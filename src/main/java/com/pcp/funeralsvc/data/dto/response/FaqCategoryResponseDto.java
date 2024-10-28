package com.pcp.funeralsvc.data.dto.response;


import com.pcp.funeralsvc.data.entity.FaqCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqCategoryResponseDto {

    private Long categoryId;
    private String categoryName;

    public FaqCategoryResponseDto(FaqCategory faqCategory) {
        this.categoryId = faqCategory.getCategoryId();
        this.categoryName = faqCategory.getCategoryName();
    }
}

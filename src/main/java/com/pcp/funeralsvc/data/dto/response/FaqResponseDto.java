package com.pcp.funeralsvc.data.dto.response;

import com.pcp.funeralsvc.data.entity.Faq;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqResponseDto {

    private Long faqId;
    private String question;
    private String answer;
    private Long categoryId;
    private String categoryName;

    public FaqResponseDto(Faq faq) {
        this.faqId = faq.getFaqId();
        this.question = faq.getQuestion();
        this.answer = faq.getAnswer();
        this.categoryId = faq.getCategory().getCategoryId();
        this.categoryName = faq.getCategory().getCategoryName();
    }
}

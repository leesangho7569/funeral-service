package com.pcp.funeralsvc.data.dto.response;

public enum ResponseCode {
	S00000("성공"),
	F00001("필수값 누락"),
	F00002("파라미터 잘못된 형식"),
	F00003("중복 등록된 값 요청"),
	F02001("이미 존재 하는 지역"),
	F02002("이미 존재 하는 장묘지"),

	F02006("증서 번호 없음"),

	F02007("User-service 연동 실패"),

	F02101("이미 요정한 장묘지 예약 상담"),
	F02404("존재 하지 않는 정보"),
	F02409("존재 하지 않는 지역"),
	F02410("존재 하지 않는 선호 장묘지"),
	F02411("존재 하지 않는 장묘 정보"),
	F02414("존재 하지 않는 FAQ 카테 고리"),
	F02415("존재 하지 않는 공지 사항"),
	F02416("존재 하지 않는 FAQ"),

	F02500("상조 시스템 연동 문제"),

	F02501("상조 시스템 연동 문제-funeralCertificateNo 없음"),

	E00001("시스템 에러"),
	T00000("테스트");
	
	private String msg;
	
	ResponseCode(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}

//	public String getMessage(Object... args){
//		return String.format(msg, args);
//	}
}

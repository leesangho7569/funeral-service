package com.pcp.funeralsvc.ipc.logAndEvent.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class Security {
	private String target;
	private String type;
	private List<PersonalInfo> personalInfoList;
}

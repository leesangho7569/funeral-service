package com.pcp.funeralsvc.ipc.logAndEvent.message.log;

import com.pcp.funeralsvc.ipc.logAndEvent.message.PetcareMessage;
import com.pcp.funeralsvc.ipc.logAndEvent.message.Header;
import java.time.LocalDateTime;

public class GeneralLogBuilder {
	
	private GeneralLog generalLog;
	
	public GeneralLogBuilder(Header header) {
		generalLog = new GeneralLog();
		generalLog.setHeader(header);
	}
	
	public GeneralLog debug(String key, String msg) {
		this.generalLog.setLevel(Level.DBG);
		return buildInternal(key, msg);
	}
	
	public GeneralLog info(String key, String msg) {
		this.generalLog.setLevel(Level.INF);
		return buildInternal(key, msg);
	}
	
	public GeneralLog warn(String key, String msg) {
		this.generalLog.setLevel(Level.WRN);
		return buildInternal(key, msg);
	}
	
	public GeneralLog error(String key, String msg) {
		this.generalLog.setLevel(Level.ERR);
		return buildInternal(key, msg);
	}
	
	public GeneralLog crit(String key, String msg) {
		this.generalLog.setLevel(Level.CRI);
		return buildInternal(key, msg);
	}
	
	private GeneralLog buildInternal(String key, String msg) {
//		Header templateHeader = this.generalLog.getHeader();
//		templateHeader.setTimestamp(LocalDateTime.now().format(PetcareMessage.FORMATTER));
//		GeneralLog generalLog = new GeneralLog();
//		generalLog.setHeader(templateHeader);
//		generalLog.setKey(key);
//		generalLog.setMsg(msg);
		
		generalLog.getHeader().setTimestamp(LocalDateTime.now().format(PetcareMessage.FORMATTER));
		generalLog.setKey(key);
		generalLog.setMsg(msg);
		
		return generalLog;
	}
	
}

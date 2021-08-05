package be.test.genesis.exception;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class ErrorDetail {
	
	private String title;
	private int status;
	private String detail;
	private long timastamp;
	private String path;
	private String developperMessage;
	private Map<String,List<ValidationError>> errors=new HashMap<String,List<ValidationError>>(); 

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public long getTimastamp() {
		return timastamp;
	}

	public void setTimastamp(long timastamp) {
		this.timastamp = timastamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDevelopperMessage() {
		return developperMessage;
	}

	public void setDevelopperMessage(String developperMessage) {
		this.developperMessage = developperMessage;
	}

	public Map<String, List<ValidationError>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<ValidationError>> errors) {
		this.errors = errors;
	}

}

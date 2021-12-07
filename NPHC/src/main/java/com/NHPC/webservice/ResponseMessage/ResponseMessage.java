package com.NHPC.webservice.ResponseMessage;

public class ResponseMessage {
	private String message;
	private String fileDownloadUrl;

	public ResponseMessage(String message, String fileDownloadUrl) {
	    this.message = message;
	    this.fileDownloadUrl = fileDownloadUrl;
	}

	public String getMessage() {
	    return message;
	}

	public void setMessage(String message) {
	    this.message = message;
	}

	public String getFileDownloadUri() {
		return fileDownloadUrl;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUrl = fileDownloadUri;
	}
}

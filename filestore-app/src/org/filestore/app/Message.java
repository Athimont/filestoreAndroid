package org.filestore.app;

import java.io.File;
import java.util.List;

public class Message {
	
	private String sender;
	private List<String> receivers;
	private String message;
	private File file;
	private String file_name;

	public Message(String sender, List<String> receivers, String message, File file, String file_name) {
		this.sender = sender;
		this.receivers = receivers;
		this.message = message;
		this.file = file;
		this.file_name = file_name;
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public List<String> getReceivers() {
		return receivers;
	}
	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
}

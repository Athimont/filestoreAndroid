package model;

import java.io.InputStream;
import java.util.List;

/**
 * Cette classe contient l'ensemble des information nécessaire
 * lors de l'uplaod d'un fichier.
 */
public class Message {

	private String sender;
	private List<String> receivers;
	private String message;
	private InputStream file;
	private String file_name;

	public Message(String sender, List<String> receivers, String message, InputStream file, String file_name) {
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

	public InputStream getFile() {
		return file;
	}
	public void setFile(InputStream file) {
		this.file = file;
	}

	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
}


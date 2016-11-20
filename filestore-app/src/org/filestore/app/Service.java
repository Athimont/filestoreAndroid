package org.filestore.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.filestore.api.FileService;
import org.filestore.api.FileServiceException;

public class Service {
	
	private static final Logger LOGGER = Logger.getLogger(Service.class.getName());
	
	FileService service;
	private static boolean isInAppclient;
	private String host;
	
	public Service (String host){
		this.host = host;
	}

	public FileService getFileServiceRemote() throws NamingException {
		if (!Boolean.TRUE.equals(isInAppclient) && service == null) {
			LOGGER.log(Level.INFO, "getting FileSerive using remote-naming");
			final Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
			env.put(Context.PROVIDER_URL, "http-remoting://" + this.host + ":8080");
			InitialContext context = new InitialContext(env);
			this.service = (FileService) context.lookup("filestore-ear/filestore-ejb/fileservice!org.filestore.ejb.file.FileService");
			context.close();
		}
		return service;
	}
	
	public boolean postFile(String owner, List<String> receivers, String message,String filename, Path file){
		try{
			if ( Boolean.TRUE.equals(isInAppclient) ) {
				LOGGER.log(Level.INFO, "We ARE in a client container");
			}
			byte[] content = Files.readAllBytes(file);
			getFileServiceRemote().postFile(owner, receivers, message, filename, content);
			return true;
		}
		catch(FileServiceException f){
			return false;
		}
		catch(IOException i){
			return false;
		}
		catch(NamingException n){
			return false;
		}
	}
	
}

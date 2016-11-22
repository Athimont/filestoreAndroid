package org.filestore.app;

import java.io.File;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;


public class Service  extends AsyncTask<Message, Integer, Boolean>{
	
	private String namespace;
	private String url;
	private String soap_action;
	private String method_name;
	
	private Activity activity;

	public Service(String namespace, String url, String soap_action, Activity activity) {
		this.namespace = namespace;
		this.url = url;
		this.soap_action = soap_action;
		this.method_name = soap_action;
		this.activity = activity;
	}
	
	@Override
	protected Boolean doInBackground(Message... params) {
		File file = params[0].getFile();
		byte[] bytes = new byte[(int) file.length()];
		try{
			for(String receiver :  params[0].getReceivers()){
				SoapObject request =new SoapObject(namespace, method_name);
				
				request.addProperty("owner",  params[0].getSender());
				request.addProperty("receiver", receiver);
				request.addProperty("message",  params[0].getMessage());
				request.addProperty("filename",  params[0].getFile_name());
				request.addProperty("filecontent", bytes);
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				MarshalBase64 marshal = new MarshalBase64();
				marshal.register(envelope);
			    envelope.setOutputSoapObject(request);
			    
			    HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
			    androidHttpTransport.debug = true; 
			    androidHttpTransport.call(soap_action, envelope);
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	protected void onPostExecute(Boolean result) {
		if(result){
			Toast.makeText(this.activity, "Mail envoyé", Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this.activity, "L'envoie à échoué", Toast.LENGTH_SHORT).show();
		}
    }
}

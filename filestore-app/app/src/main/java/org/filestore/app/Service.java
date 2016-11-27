package org.filestore.app;

import android.app.Activity;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class Service extends AsyncHttpResponseHandler{

	private Activity activity;

	public Service(Activity activity) {
		this.activity = activity;
	}

	public void onSuccess(String response) {
		try {
			JSONObject obj = new JSONObject(response);
			if(obj.getBoolean("status")){
				Toast.makeText(this.activity, "Mail Send! " + obj.getString("error_msg") , Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(this.activity, obj.getString("error_msg"), Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			Toast.makeText(this.activity, "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(int statusCode, Throwable error, String content) {
		if(statusCode == 404){
			Toast.makeText(this.activity, "Requested resource not found", Toast.LENGTH_LONG).show();
		}
		else if(statusCode == 500){
			Toast.makeText(this.activity, "Something went wrong at server end", Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(this.activity, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
		}
	}
}

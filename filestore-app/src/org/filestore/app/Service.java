package org.filestore.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public class Service  extends AsyncTask<Message, Integer, Boolean>{
	
	private Activity activity;

	public Service(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	protected Boolean doInBackground(Message... params) {
		return true;
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

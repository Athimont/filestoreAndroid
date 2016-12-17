package services.access;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

/**
 * Cette classe permet l'accès à la méthode du web service qui permet
 * le téléchargement d'un fichier
 */
public class ServiceDownload extends AsyncTask<String,String,Boolean> {

    private final String protocol = "http://";
    private final String host = "fs-6846e6a6-1.ada20f9b.cont.dockerapp.io";
    private final String port = "32776";
    private final String requestPath1 = "/api/files/";
    private final String requestPath2 = "/download";

    Activity activity;

    public ServiceDownload(Activity a){
        this.activity = a;
    }

    @Override
    protected Boolean doInBackground(String... fileIds) {
        try {
            String key = fileIds[0];
            HttpClient clientDetails = new DefaultHttpClient();
            String urlDetails = this.protocol + this.host + ":" + this.port + this.requestPath1 + key;
            HttpGet deleteRequest = new HttpGet(urlDetails);
            HttpResponse reponseDetails = clientDetails.execute(deleteRequest);
            InputStream isDetails =reponseDetails.getEntity().getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(isDetails, writer, "UTF-8");
            String theString = writer.toString();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map;
            map = mapper.readValue(theString, new TypeReference<Map<String, String>>(){});
            String file_name = map.get("name").toString();


            HttpClient client = new DefaultHttpClient();
            String url = this.protocol + this.host + ":" + this.port + this.requestPath1 + key +this.requestPath2;
            HttpGet getRequest = new HttpGet(url);
            HttpResponse reponse = client.execute(getRequest);
            InputStream is =reponse.getEntity().getContent();

            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/download/filestore");
            dir.mkdirs();
            File file = new File(dir, file_name);

            FileOutputStream f = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    protected void onPostExecute(Boolean b) {
        if(b){
            Toast.makeText(this.activity, "Your file has been Downloaded", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this.activity, "Failed to download your file", Toast.LENGTH_LONG).show();
        }

    }
}

package org.filestore.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    EditText sender, receiver, edittext, message, fileIdDownload, fileIdDetails;
    TextView owner, type, name, id, stream, lastDownload, creation, messageDetails, downloads, length;
    TabHost tabHost;
    private static final int REQUEST_PATH = 1;

	String currentPath;



	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.sender = (EditText)findViewById(R.id.sender);
        this.receiver = (EditText)findViewById(R.id.receiver);
        this.edittext = (EditText)findViewById(R.id.editText);
        this.message = (EditText)findViewById(R.id.message);
        this.fileIdDownload =(EditText)findViewById(R.id.fileIdDownload);
        this.fileIdDetails =(EditText)findViewById(R.id.fileIdDetails);
        this.tabHost = (TabHost)findViewById(R.id.tabHost);
        this.tabHost.setup();

        TabHost.TabSpec spec = this.tabHost.newTabSpec("upload");
        spec.setContent(R.id.upload);
        spec.setIndicator("upload");
        this.tabHost.addTab(spec);

        spec = this.tabHost.newTabSpec("download");
        spec.setContent(R.id.download);
        spec.setIndicator("download");
        this.tabHost.addTab(spec);

        spec = this.tabHost.newTabSpec("details");
        spec.setContent(R.id.details);
        spec.setIndicator("details");
        this.tabHost.addTab(spec);

        this.owner = (TextView)findViewById(R.id.owner);
        this.type = (TextView)findViewById(R.id.type);
        this.name = (TextView)findViewById(R.id.name);
        this.id = (TextView)findViewById(R.id.id);
        this.stream = (TextView)findViewById(R.id.stream);
        this.lastDownload = (TextView)findViewById(R.id.lastDownload);
        this.creation = (TextView)findViewById(R.id.creation);
        this.messageDetails = (TextView)findViewById(R.id.messageDetails);
        this.downloads = (TextView)findViewById(R.id.downloads);
        this.length = (TextView)findViewById(R.id.length);

        Intent receivedIntent = getIntent();
        String receveidAction = receivedIntent.getAction();

        //l'appli lancé en mode partage de fichier
        if(receveidAction.equals(Intent.ACTION_SEND)){
            Uri receivedUri = (Uri)receivedIntent.getParcelableExtra(Intent.EXTRA_STREAM);
            if(null != receivedUri){
                String pathFile = getFilePathFromUri(receivedUri);
                Log.v("PathFile Receive", receivedUri.toString());
                Log.v("PathFile", pathFile);

                String tabPathUri[] = pathFile.toString().split("/");
                String nomFichier = tabPathUri[tabPathUri.length - 1];
                currentPath = pathFile;
                //Affiche le nom de l'image au lieu du path
                edittext.setText(nomFichier);

            }
        }
        //else{
            //lancé en mode non partage de fichier(normal)
        //}


    }

    //recupere le chemin du du fichier format /sdcard0/...
    private String getFilePathFromUri(Uri fileUri){

        if(fileUri.getScheme().startsWith("file")) {
            //supprime les caractères bizarre dans le nom
            String decodedUri = fileUri.decode(fileUri.toString());
            return  fileUri.getPath();
        }

        Cursor cursor =
                getContentResolver().query(fileUri, null, null, null, null);

        if(null == cursor){
            return null;
        }
        int pathIndex = cursor.getColumnIndex("_data");
        if(null != cursor && cursor.moveToFirst()){
            Log.v("columns",Arrays.toString(cursor.getColumnNames()));

            return cursor.getString(pathIndex);
        }

        return null;
    }

    public void getfile(View view){
    	Intent intent1 = new Intent(this, ChooseActivity.class);
        startActivityForResult(intent1,REQUEST_PATH);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode == REQUEST_PATH){
    		if (resultCode == RESULT_OK) {
    			this.currentPath = data.getStringExtra("GetPath") + "/" + data.getStringExtra("GetFileName");
            	this.edittext.setText(data.getStringExtra("GetFileName"));
                return;
    		}
        }
    }

    private boolean testMail(String mail){
    	Pattern p_mail = Pattern.compile("[a-zA-Z][[a-zA-Z0-9]*\\.|\\-|\\_]*[a-zA-Z0-9]*\\@[a-zA-Z][[a-zA-Z0-9]*\\.|\\-|\\_]*[a-zA-Z0-9]*\\.[a-zA-Z]{2,3}");
        Matcher matcher = p_mail.matcher(mail);
    	if(!matcher.matches()){
            return false;
        }
    	return true;
    }

    private boolean testReceivers(List<String> mails){
    	for(String mail : mails){
    		if(!testMail(mail)){
    			return false;
    		}
    	}
    	return true;
    }

    public void send(View view){
        String sender_mail = this.sender.getText().toString();
        String receiver_mail = this.receiver.getText().toString();
        List <String> receivers;
        receivers = Arrays.asList(receiver_mail.split(" "));

        String file =this.edittext.getText().toString();

        if(!testMail(sender_mail)){
            Toast.makeText(MainActivity.this, "The sender e-mail is wrong", Toast.LENGTH_LONG).show();
            return;
        }
        if(!testReceivers(receivers)){
            Toast.makeText(MainActivity.this, "The receiver e-mail is wrong", Toast.LENGTH_LONG).show();
            return;
        }

        if(file.equals("") || file == null){
            Toast.makeText(MainActivity.this, "You must filled the EditText Feild", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Message m =new Message(sender_mail, receivers, this.message.getText().toString(), new FileInputStream(this.currentPath),this.edittext.getText().toString());
            ServiceUpload service = new ServiceUpload(this);
            service.execute(m);
        }
        catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this, "File not Found", Toast.LENGTH_SHORT).show();
        }

    }

    public void download(View view){
        String key = this.fileIdDownload.getText().toString();
        ServiceDownload service = new ServiceDownload(this);
        service.execute(key);
    }

    public void seeDetails(View view){
        String key = this.fileIdDetails.getText().toString();
        ServiceDetails service = new ServiceDetails(this);
        service.execute(key);
    }

    public void delete(View view){
        String key = this.fileIdDetails.getText().toString();
        ServiceDelete service = new ServiceDelete(this);
        service.execute(key);
    }
}

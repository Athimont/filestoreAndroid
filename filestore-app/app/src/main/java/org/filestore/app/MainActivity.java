package org.filestore.app;
  
import android.os.Bundle;
import android.app.Activity; 
import android.content.Intent; 
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    EditText sender, receiver, edittext, message;
    final int SEND_DONE = 0;
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
            Toast.makeText(MainActivity.this, "The sender e-mail is wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!testReceivers(receivers)){
            Toast.makeText(MainActivity.this, "The receiver e-mail is wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        if(file.equals("") || file == null){
            Toast.makeText(MainActivity.this, "You must filled the EditText Feild", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            RequestParams params = new RequestParams();
            params.put("owner",sender_mail);
            params.put("receivers", receivers);
            params.put("filename",file);
            params.put("file", new FileInputStream(this.currentPath));
            params.put("message",this.message.getText().toString());
            post(params);
        } catch (FileNotFoundException e) {
            Toast.makeText(MainActivity.this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void post(RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://10.0.2.2/api/files/dopostfile", params, new Service(this));
    }

}

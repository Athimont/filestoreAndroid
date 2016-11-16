package org.filestore.app;
  
import android.os.Bundle; 
import android.app.Activity; 
import android.content.Intent; 
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.filestore.app.SendActivity.PUBLIC_STATIC_STRING;

public class MainActivity extends Activity {

    EditText sender, receiver, edittext, message;
    final int SEND_DONE = 0;
    private static final int REQUEST_PATH = 1;
 
	String curFileName;

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
 // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
    	if (requestCode == REQUEST_PATH){
    		if (resultCode == RESULT_OK) { 
    			curFileName = data.getStringExtra("GetPath") + "/" + data.getStringExtra("GetFileName");
            	edittext.setText(data.getStringExtra("GetFileName"));
                return;
    		}
        }
        if(requestCode == SEND_DONE){
            if (resultCode == SendActivity.RESULT_OK) {
                String s =data.getStringExtra(PUBLIC_STATIC_STRING);
                this.sender.setText(s);
                this.receiver.setText(s);
                this.edittext.setText(s);
                this.message.setText(s);
                return;
            }
        }
    }

    public void send(View view){
        String sender_mail = this.sender.getText().toString();
        String receiver_mail = this.receiver.getText().toString();
        String file =this.edittext.getText().toString();
        Pattern p_mail = Pattern.compile("[a-zA-Z][[a-zA-Z0-9]*\\.|\\-|\\_]*[a-zA-Z0-9]*\\@[a-zA-Z][[a-zA-Z0-9]*\\.|\\-|\\_]*[a-zA-Z0-9]*\\.[a-zA-Z]{2,3}");
        Matcher matcher_sender = p_mail.matcher(sender_mail);
        Matcher matcher_receiver = p_mail.matcher(receiver_mail);
        if(!matcher_sender.matches()){
            Toast.makeText(MainActivity.this, "The sender e-mail is wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!matcher_receiver.matches()){
            Toast.makeText(MainActivity.this, "The receiver e-mail is wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        if(file.equals("") || file == null){
            Toast.makeText(MainActivity.this, "You must filled the EditText Feild", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, SendActivity.class);
        //TODO : méthode permettant l'envoie grace à maven
        startActivityForResult(intent,SEND_DONE);
    }
}

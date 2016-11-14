package org.filestore.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.filestore.app.SearchActivity.PUBLIC_STATIC_URL;
import static org.filestore.app.SendActivity.PUBLIC_STATIC_STRING;

public class MainActivity extends AppCompatActivity {

    final int SEND_DONE = 0;
    final int SEARCH_DONE = 1;

    EditText sender, receiver, file, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.sender =(EditText) findViewById(R.id.sender);
        this.receiver =(EditText) findViewById(R.id.receiver);
        this.file =(EditText) findViewById(R.id.file);
        this.message =(EditText) findViewById(R.id.message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SEND_DONE) : {
                if (resultCode == SendActivity.RESULT_OK) {
                    String s =data.getStringExtra(PUBLIC_STATIC_STRING);
                    this.sender.setText(s);
                    this.receiver.setText(s);
                    this.file.setText(s);
                    this.message.setText(s);
                }
                break;
            }
            case (SEARCH_DONE) : {
                if (resultCode == SendActivity.RESULT_OK) {
                    String url = data.getStringExtra(PUBLIC_STATIC_URL);
                    file.setText(url);
                }
                break;
            }
        }
    }


    public void pickFile(View view){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(intent,SEARCH_DONE);
        System.out.println("pickFile");
    }

    public void sendFile(View view){
        String sender_mail = this.sender.getText().toString();
        String receiver_mail = this.receiver.getText().toString();
        String file = this.file.getText().toString();
        //TODO : faire une bonne regex pour un mail
        String regexMail = "([A-Za-z][a-zA-Z_0-9]*\\.)*[a-zA-Z_0-9]+\\@[A-Za-z]+\\.[A-Za-z]{2,3}";
        String regexFile = "([a-zA-Z_0-9]+/)*[a-zA-Z_0-9]+\\.[a-zA-Z]+[0-9]{0,1}";
        Pattern pMail = Pattern.compile(regexMail);
        Pattern pFile = Pattern.compile(regexFile);
        Matcher matcher_sender = pMail.matcher(sender_mail);
        Matcher matcher_receiver = pMail.matcher(receiver_mail);
        Matcher matcher_file = pFile.matcher(file);
        if(!matcher_sender.matches()){
            Toast.makeText(MainActivity.this, "The sender e-mail is wrong", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!matcher_receiver.matches()) {
            Toast.makeText(MainActivity.this, "The receiver e-mail is wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!matcher_file.matches()) {
            Toast.makeText(MainActivity.this, "The file field should be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, SendActivity.class);
        //TODO : méthode permettant l'envoie grace à maven
        startActivityForResult(intent,SEND_DONE);
    }
}

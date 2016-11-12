package org.filestore.app;

import android.app.AliasActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends AliasActivity {

    public static String PUBLIC_STATIC_URL;
    public EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.url =(EditText) findViewById(R.id.url);
    }

    public void returnFile(View view){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(PUBLIC_STATIC_URL,this.url.getText().toString());
        setResult(SendActivity.RESULT_OK, resultIntent);
        finish();
        //TODO : méthode qui reprend le string de l'url du fichier
    }
}

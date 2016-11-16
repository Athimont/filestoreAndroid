package org.filestore.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SendActivity  extends Activity {

    final static String PUBLIC_STATIC_STRING = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
    }

    public void back(View view){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(PUBLIC_STATIC_STRING,"");
        setResult(SendActivity.RESULT_OK, resultIntent);
        finish();
    }
}


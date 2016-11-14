package org.filestore.app;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends ListActivity {

    public static String PUBLIC_STATIC_URL;
    private File currentDir;
    private FileArrayAdapter adapter;
    public EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/storage/");
        fill(currentDir);
    }

    private void fill(File f) {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<Item>dir = new ArrayList<Item>();
        List<Item>fls = new ArrayList<Item>();
        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()){


                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    }
                    else buf = 0;
                    String num_item = String.valueOf(buf);
                    if(buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                }
                else
                {
                    fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                }
            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));
        adapter = new FileArrayAdapter(SearchActivity.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
    }

    private void onFileClick(Item o) {
        this.url.setText(currentDir.toString());
    }

    public void returnFile(View view){
        String file = this.url.getText().toString();
        String regexFile = "([a-zA-Z_0-9]+/)*[a-zA-Z_0-9]+\\.[a-zA-Z_0-9]+[0-9]{0,1}";
        Pattern pFile = Pattern.compile(regexFile);
        Matcher matcher_file = pFile.matcher(file);
        if (!matcher_file.matches()) {
            Toast.makeText(SearchActivity.this, "The file field should be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra(PUBLIC_STATIC_URL,this.url.getText().toString());
        setResult(SearchActivity.RESULT_OK, resultIntent);
        finish();
        //TODO : méthode qui reprend le string de l'url du fichier
    }
}

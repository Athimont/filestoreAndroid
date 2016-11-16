package org.filestore.app;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList; 
import java.util.Collections;
import java.util.List;
import java.text.DateFormat; 
import android.os.Bundle; 
import android.app.ListActivity;
import android.content.Intent; 
import android.view.View;
import android.widget.ListView; 

public class ChooseActivity extends ListActivity {

	private String currentPath = "";
	private File currentDir;
    private FileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        currentDir = new File("/sdcard/");
		currentPath = "/sdcard";
        fill(currentDir); 
    }
    private void fill(File f)
	{
    	File[]dirs = f.listFiles();
		 this.setTitle("Current Dir: "+this.currentPath);
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
		 adapter = new FileArrayAdapter(ChooseActivity.this,R.layout.file_view,dir);
		 this.setListAdapter(adapter); 
    }
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Item o = adapter.getItem(position);
		if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
			if(!o.getName().equals("..")) {
				this.currentPath = this.currentPath + "/" + o.getName();
			}
			else{
				String[] splittedPath = this.currentPath.split("/");
				this.currentPath = "";
				for(int i = 1; i < splittedPath.length-1 ; i++){
					this.currentPath = this.currentPath+ "/" + splittedPath[i];
				}
				System.out.println(this.currentPath);
			}
			currentDir = new File(o.getPath());
			fill(currentDir);
		}
		else
		{
			onFileClick(o);
		}
	}
    private void onFileClick(Item o)
    {
    	//Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent();
        intent.putExtra("GetPath",this.currentPath);
        intent.putExtra("GetFileName",o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
}

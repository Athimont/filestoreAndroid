package org.filestore.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class Dialog extends ProgressDialog {

    Handler handler = new Handler();
    int status = 0;
    public Dialog(Context context) {
        super(context);
    }


    public void createProgressDialog(){

        setIndeterminate(false);
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setCancelable(false);
        setMessage("Sending message... " + status + " %");
        setTitle("Sharing");

        setMax(100);
        setProgressNumberFormat(null);
        show();
    }

    public void showProgressDialog()
    {
        status = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status < 100){
                    status += 1;
                    try{
                        Thread.sleep(200);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setProgress(status);
                            setMessage("Sending message... " + status + " %");

                            if(status == 100){
                                dismiss();
                            }
                        }
                    });
                }
            }
        }).start();

    }
}

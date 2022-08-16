package com.dicoding.picodiploma.treasurehunt_kotlin.java.util;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.widget.TextView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;

public class LoadingDialogBar {
    Context context;
    Dialog dialog;

    public LoadingDialogBar(Context context){
        this.context = context;
    }

/*    public void showLoading(String title){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.pop_up_positive);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleLoading = dialog.findViewById(R.id.titlepositive);
        titleLoading.setText(title);
        dialog.create();
        dialog.show();
    }*/

    public void registrationFailed(){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.registration_failed_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.create();
        dialog.show();
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                dialog.cancel();
                dialog.dismiss();
            }
        }, 3000);
    }
    public void registrationSuccess(){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.registration_success_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.create();
        dialog.show();
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                dialog.cancel();
                dialog.dismiss();
            }
        }, 3000);
    }
    /*public void showWarn(String message){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.pop_up_warning);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleLoading = dialog.findViewById(R.id.titlewarning);
        titleLoading.setText(message);
        dialog.create();
        dialog.show();
        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                dialog.cancel();
                dialog.dismiss();
            }
        }, 2200);
    }*/


   /* public void cekLoading(){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.pop_up_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView titleLoading = dialog.findViewById(R.id.titlesuccess);


        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){

            }
        }, 2200);
    }
    public void hideLoading(){
        dialog.dismiss();
    }*/

    public void hideLoading(){
        dialog.dismiss();
    }
}

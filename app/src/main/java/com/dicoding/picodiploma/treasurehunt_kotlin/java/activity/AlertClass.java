package com.dicoding.picodiploma.treasurehunt_kotlin.java.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dicoding.picodiploma.treasurehunt_kotlin.R;

public class AlertClass {
    Context context;
    Dialog dialog;

    public AlertClass(Context context) {
        this.context = context;
    }

    public void showAlertCheckIn(String title, String subtitle, String flowId){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_regis_failed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtSubtitle = dialog.findViewById(R.id.txtSubtitle);
        Button ok = dialog.findViewById(R.id.ok_forgot);
        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent checkinIntent = new Intent(context, ActivityScan.class);
                checkinIntent.putExtra("FLOW_ID", flowId);
                context.startActivity(checkinIntent);
            }
        });

        dialog.create();
        dialog.show();

    }
    public void showAlertCarCheck(String title, String subtitle, String flowId){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_regis_failed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTitle = dialog.findViewById(R.id.textView4);
        TextView txtSubtitle = dialog.findViewById(R.id.textView6);
        Button ok = dialog.findViewById(R.id.ok_forgot);
        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent checkinIntent = new Intent(context, ActivityScanCar.class);
                checkinIntent.putExtra("FLOW_ID", flowId);
                context.startActivity(checkinIntent);
            }
        });

        dialog.create();
        dialog.show();

    }
    public void showAlerCheckOut(String title, String subtitle, String flowId){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_regis_failed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTitle = dialog.findViewById(R.id.textView4);
        TextView txtSubtitle = dialog.findViewById(R.id.textView6);
        Button ok = dialog.findViewById(R.id.ok_forgot);
        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, ActivityScanCheckOut.class);
                intent.putExtra("FLOW_ID", flowId);
                context.startActivity(intent);
            }
        });

        dialog.create();
        dialog.show();
    }
    public void showAlertScanOVJ(String title, String subtitle, String flowId){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_regis_failed);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtTitle = dialog.findViewById(R.id.textView4);
        TextView txtSubtitle = dialog.findViewById(R.id.textView6);
        Button ok = dialog.findViewById(R.id.ok_forgot);
        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent ovjIntent = new Intent(context, ActivityScanOVJ.class);
                ovjIntent.putExtra("FLOW_ID", flowId);
                context.startActivity(ovjIntent);
            }
        });

        dialog.create();
        dialog.show();
    }

}

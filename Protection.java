package com.example.lenovo.safetyhelper;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public  class Protection extends AppCompatActivity
    implements View.OnClickListener,DialogInterface.OnClickListener,
        TimePickerDialog.OnTimeSetListener {
    AlertDialog alertDialog;
    Calendar c = Calendar.getInstance();
    TextView txTime;
    TextView txv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protection);
        //生成结束时的对话框
        AlertDialog.Builder bdr = new AlertDialog.Builder(this);
        bdr.setMessage("是否安全抵达？");
        bdr.setNegativeButton("一键报警", this);
        bdr.setPositiveButton("是", this);
        bdr.setCancelable(true);
        alertDialog = bdr.create();

        txv1 = (TextView) findViewById(R.id.shiyan);
        txTime =  findViewById(R.id.inputtime);
        txTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
         TimePickerDialog t= new  TimePickerDialog(this,this,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), true);
                
                t.show();

    }

    public void onTimeSet(TimePicker view, int h, int m) {
        if (m < 10) {
            txTime.setText(h + ":0" + m);
        } else {
            txTime.setText(h + ":" + m);
        }
    }

    //需插入路线返回的路程
    public void walk(View v) {
        TextView txv = (TextView) findViewById(R.id.outputtime);
        //时间=路程/速度 步行默认速度为5.5km/h 路程从路线中获取 先默认为2km
        double t = 0;
        t = 2 * 60 / 5.5;
        if (t >= 60) {
            int h = (int) Math.floor(t / 60);
            int m = (int) Math.floor(t % 60);
            txv.setText(h + "时" + m + "分钟");
        } else {
            int m = (int) t;
            txv.setText(m + "分钟");
        }
    }

    public void bycar(View v) {
        TextView txv = (TextView) findViewById(R.id.outputtime);
        //时间=路程/速度 步行默认速度为36km/h 路程从路线中获取 先默认为20km
        double t = 0;
        t = 20 * 60 / 36;
        if (t >= 60) {
            int h = (int) Math.floor(t / 60);
            int m = (int) Math.floor(t % 60);
            txv.setText(h + "时" + m + "分钟");
        } else {
            int m = (int) Math.floor(t);
            txv.setText(t + "分钟");
        }
    }

    public void protect_start(View v) {
        //计算路程总用时

        Calendar c = Calendar.getInstance();
        TextView txv = (TextView) findViewById(R.id.inputtime);
        String str = txv.getText().toString().trim();
        String[] b = str.split(":");
        int h = Integer.parseInt(b[0]);
        int m = Integer.parseInt(b[1]);
        int minutes = (h - c.get(Calendar.HOUR_OF_DAY)) * 60 + m - c.get(Calendar.MINUTE);
        if (minutes < 0) {
            minutes = minutes + 24 * 60;
        }

        CountDownTimer timer = new CountDownTimer(minutes * 60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                txv1.setText("预计" + l / 1000 + "秒后抵达目的地");
            }

            @Override
            public void onFinish() {
                alertDialog.show();
                txv1.setText("结束！");

            }
        }.start();


    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == DialogInterface.BUTTON_NEGATIVE) {
            Intent it = new Intent(this, Police.class);
            startActivity(it);
        }
        if (i == DialogInterface.BUTTON_POSITIVE) {
            Intent it = new Intent(this, Menu.class);
            startActivity(it);
        }
    }
}


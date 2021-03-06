package com.example.lenovo.safetyhelper;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Locale;

public class Record extends AppCompatActivity implements View.OnClickListener{

    private ImageButton buttonStart;
    private ImageButton buttonPlay;
    private TextView time;
    private ProgressBar progressBar;
    // private ImageView volume;
    private Button buttonFinish;
    private Button buttonSaveOK;
    private Button buttonSaveCancel;
    private EditText editTextFileName;
    private Button buttonFinishOK;
    private Button buttonFinishCancel;
    private Button buttonUploadOk;
    private Button buttonUploadCancel;

    private MediaRecorder mediaRecorder=new MediaRecorder();  //用于录音
    private MediaPlayer mediaPlayer=new MediaPlayer();  //用于播放录音
    private File filetem=new File( Environment.getExternalStorageDirectory().getPath() + "//RecordTest"+"/new.amr");  //创建一个临时的音频文件
    private long currenttime;  //用于确定当前录音时间
    private boolean isrecording=false;  //用于判断当前是否在录音
    private boolean Isplaying=false;  //用于判断是否正在处于播放录音状态

    //定义Handler
    private Handler volumehandler=new Handler();  //显示音量
    private Handler timehandler=new Handler();  //显示录音时间
    private Handler playhandler=new Handler();  //显示播放时间

    private File file;//最终录音文件

    private String getTime(int timeMs) {
        int total=timeMs/1000;
        StringBuilder stringBuilder=new StringBuilder();
        Formatter formatter=new Formatter(stringBuilder, Locale.getDefault());
        int seconds=total%60;
        int minutes=(total/60)%60;
        int hours=total/3600;
        stringBuilder.setLength(0);
        if (hours>0){
            return formatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }
        else{
            return formatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }

    private Runnable runnable_1=new Runnable() {  //音量
        @Override
        public void run() {
            int volume=0;
            int ratio;
            ratio = mediaRecorder.getMaxAmplitude() / 600;
            if(ratio>1){
                volume= (int) (20 * Math.log10(ratio));
            } //将getMaxAmplitude()的返回值转换为分贝
            progressBar.setMax(15);//将最大音量设置为60分贝
            progressBar.setProgress(volume/4);//显示录音音量
            volumehandler.postDelayed(runnable_1, 100);
        }
    };

    private Runnable runnable_2=new Runnable() {//录音时间
        @Override
        public void run() {
            time.setText(getTime((int) (System.currentTimeMillis()-currenttime)));
            //调用前先获取当前时间，再通过System.currentTimeMillis()-currenttime获得录音时间并转换成文本
            timehandler.postDelayed(runnable_2,1000);
        }
    };

    private Runnable runnable_3=new Runnable() {//播放时间
        @Override
        public void run() {
            time.setText(getTime(mediaPlayer.getCurrentPosition()));
            //MediaPlayer类的getCurrentPosition()方法可以获得音频的当前播放时间
            playhandler.postDelayed(runnable_3,1000);
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        buttonStart = (ImageButton) findViewById(R.id.buttonStart);
        buttonPlay=(ImageButton)findViewById(R.id.buttonPlay);
        time=(TextView)findViewById(R.id.textViewTime);
        progressBar=(ProgressBar)findViewById(R.id.progressTimeBar);
        buttonFinish = (Button)findViewById(R.id.buttonFinish);
        //volume=(ImageView)findViewById(R.id.Volume);
        MediaPlayer.OnCompletionListener playerListener = new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                buttonPlay.setImageResource(R.drawable.ic_play);
                playhandler.removeCallbacks(runnable_3);
                time.setText("00:00");
                Isplaying=false;
                mediaPlayer.reset();
            }
        };
        mediaPlayer.setOnCompletionListener(playerListener);
        buttonStart.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        buttonFinish.setOnClickListener(this);


    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonStart:
                if (isrecording) {
                    finishrecord();
                } else {
                    record();
                }
                break;
            case R.id.buttonPlay:
                if(mediaPlayer.isPlaying()){
                    playpause();
                }
                else{
                    play();
                }
                break;
            case R.id.buttonFinish:
                finishwork();
                break;
            default:
                break;

        }
    }

    private void record(){
        if(filetem.exists()) {
            filetem.delete();
        }
        mediaRecorder.reset();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置音频来源为麦克风
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置默认的输出文件的格式
        mediaRecorder.setOutputFile(filetem.getAbsolutePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//默认的编码方式
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();//开始录音
        buttonStart.setImageResource(R.drawable.ic_pause);
        isrecording=true;
        currenttime=System.currentTimeMillis();
        volumehandler.postDelayed(runnable_1,100);
        timehandler.postDelayed(runnable_2,1000);
    }

    private void finishrecord(){
        mediaRecorder.stop();
        buttonStart.setImageResource(R.drawable.ic_mic);
        volumehandler.removeCallbacks(runnable_1);
        timehandler.removeCallbacks(runnable_2);//停止相关线程
        time.setText("00:00");
        isrecording=false;
    }

    private void finishwork(){
        AlertDialog.Builder alertdialogbuilderFinish=new AlertDialog.Builder(this);
        final AlertDialog alertdialogFinish=alertdialogbuilderFinish.create();
        View view=View.inflate(Record.this,R.layout.alertdialogfinish,null);
        alertdialogFinish.setView(view);
        alertdialogFinish.show();
        buttonFinishOK = (Button)view.findViewById(R.id.buttonFinishOK);
        buttonFinishCancel = (Button)view.findViewById(R.id. buttonFinishCancel);
        buttonFinishOK.setOnClickListener(this);
        buttonFinishOK.setOnClickListener(new View.OnClickListener() {    //设置Button的监听器
            @Override
            public void onClick(View v) {
                save();
                alertdialogFinish.dismiss();
            }
        });
        buttonFinishCancel.setOnClickListener(new View.OnClickListener() {    //设置Button的监听器
            @Override
            public void onClick(View v) {
                alertdialogFinish.dismiss();
            }
        });

    }

    private void save(){
        AlertDialog.Builder alertdialogbuilderSave=new AlertDialog.Builder(this);
        final AlertDialog alertdialogSave=alertdialogbuilderSave.create();
        View view=View.inflate(Record.this,R.layout.alertdialogsave,null);
        alertdialogSave.setView(view);
        alertdialogSave.show();
        buttonSaveOK = (Button)view.findViewById(R.id. buttonSaveOK);
        buttonSaveCancel = (Button)view.findViewById(R.id. buttonSaveCancel);
        editTextFileName = (EditText)view.findViewById(R.id.editTextFileName);
        buttonSaveOK.setOnClickListener(this);
        buttonSaveOK.setOnClickListener(new View.OnClickListener() {    //设置Button的监听器
            @Override
            public void onClick(View v) {
                filesave();
                alertdialogSave.dismiss();
            }
        });

        buttonSaveCancel.setOnClickListener(new View.OnClickListener() {    //设置Button的监听器
            @Override
            public void onClick(View v) {
                alertdialogSave.dismiss();
            }
        });

    }

    private int filesave() {
        String strFileName;
        strFileName = editTextFileName.getText().toString();
        if (strFileName == null) {
            Toast.makeText(this, "录音文件名不能为空", Toast.LENGTH_SHORT).show();
            return 0;
        } else {
            file = new File(Environment.getExternalStorageDirectory().getPath() + "//RecordTest", strFileName+""+".amr");  //最终录音文件实例化
            if (file.exists()) {
                Toast.makeText(this, "录音文件已存在，请重命名", Toast.LENGTH_SHORT).show();
                return 0;
            }
            //执行重命名
            filetem.renameTo(file);
            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        }
        AlertDialog.Builder alertdialogbuilderUpload=new AlertDialog.Builder(this);
        final AlertDialog alertdialogUpload=alertdialogbuilderUpload.create();
        View view=View.inflate(Record.this,R.layout.alertdialogupload,null);
        alertdialogUpload.setView(view);
        alertdialogUpload.show();
        buttonUploadOk = (Button)view.findViewById(R.id.buttonUploadOk);
        buttonUploadCancel = (Button)view.findViewById(R.id.buttonUploadCancel);
        buttonUploadOk.setOnClickListener(this);
        buttonUploadOk.setOnClickListener(new View.OnClickListener() {    //设置Button的监听器
            @Override
            public void onClick(View v) {
                alertdialogUpload.dismiss();
            }
        });
        buttonUploadCancel.setOnClickListener(new View.OnClickListener() {    //设置Button的监听器
            @Override
            public void onClick(View v) {
                alertdialogUpload.dismiss();
            }
        });
        return 1;

    }

    private void play(){
        if(!Isplaying) {
            if (file != null && file.exists()) {
                try {
                    mediaPlayer.setDataSource(file.getAbsolutePath());//设置音频来源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(filetem != null && filetem.exists()){
                try {
                    mediaPlayer.setDataSource(filetem.getAbsolutePath());//设置音频来源
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "未发现录音文件", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Isplaying = true;
        }
        mediaPlayer.start();//开始播放
        buttonPlay.setImageResource(R.drawable.ic_pause);
        playhandler.postDelayed(runnable_3,1000);

    }

    private void playpause(){
        mediaPlayer.pause();
        playhandler.removeCallbacks(runnable_3);
        buttonPlay.setImageResource(R.drawable.ic_play);

    }

}
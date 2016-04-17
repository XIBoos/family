package com.will.bean;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;

import com.will.User.UserManager;

import java.io.File;
import java.io.FileInputStream;

import cn.bmob.im.BmobDownloadManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.inteface.DownloadListener;
import cn.bmob.im.util.BmobUtils;

/**
 * Created by will on 2016/2/18.
 */
public class RecordPlayListener implements View.OnClickListener{

    Context context;

    private boolean isPlaying=false;
    private BmobMsg message;
    private String currentUserID;
    private DownloadListener downloadListener;
    MediaPlayer mediaPlayer;
    UserManager manager;
    private boolean isUseSpeaker=true;


    public RecordPlayListener(Context con,BmobMsg msg,DownloadListener listener){
        context=con;
        message=msg;
        currentUserID=manager.getInstance(con).getCurrentUserObjectId();
        downloadListener=listener;

    }

    @Override
    public void onClick(View v) {
        //第二次点击停止播放录音
        if(isPlaying){
            StopPlaying();

        }
        if(message.getBelongId().equals(currentUserID)){//发送的消息
            String Path=message.getContent().split("&")[0];
            StartPlay(Path);
        }else {//接收的消息
            String Path=getDownablePath(message);
        }

    }

    public void StopPlaying(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying=false;
    }

    public void StartPlay(String FilePath){
        File voice=new File(FilePath);
        if(!voice.exists())
            return;
        AudioManager audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer=new MediaPlayer();
        //开启扬声器
        if(isUseSpeaker){
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        }else {
            audioManager.setSpeakerphoneOn(false);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try{
            mediaPlayer.reset();
            FileInputStream Inputstream=new FileInputStream(voice);
            mediaPlayer.setDataSource(Inputstream.getFD());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPlaying=true;
                    mp.start();
                }
            });
        }catch (Exception e){

        }
    }

    public String getDownablePath(BmobMsg msg){

        String accountDir= BmobUtils.string2MD5(currentUserID);
        File dir=new File(BmobConfig.BMOB_VOICE_DIR+
                File.separator+accountDir+File.separator+message.getBelongId());
        if(!dir.exists()){
            dir.mkdirs();
        }
        File audiofile=new File(dir.getAbsolutePath()+File.separator+message.getMsgTime()+".amr");

        try {
            if (!audiofile.exists()){
                DownableVoiceByNet(msg);
            }
        }catch (Exception e){

        }
        return audiofile.getAbsolutePath();
    }

    //网络下载录音内容
    public void DownableVoiceByNet(BmobMsg msg){
        Boolean isExist=BmobDownloadManager.checkTargetPathExist(currentUserID, msg);
        if(!isExist){
            String netURL=msg.getContent().split("&")[0];
            BmobDownloadManager downloadTask=new BmobDownloadManager(context, msg,downloadListener);
            downloadTask.execute(netURL);
        }
    }
}

package browser.green.org.bona.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public  class MyBinder extends Binder{
        public void play_music(int music){
            playMusic(music);
        }

        public void playmusic(String path){ playMusic(path);}

        public void pause_music(){
            pauseAndReplay();
        }

        public void stop_music(){
            stopMusic();
        }

        public void reset_music(){
            resetMusic();
        }

        public boolean is_playing(){
            return isPlaying();
        }
        public void start(){
            play();
        }

        public void set_loop(boolean b){
            setLoop(b);
        }
    }

    public void play(){
        mediaPlayer.start();
    }
    public void setLoop(boolean b){
        mediaPlayer.setLooping(b);
    }

    public void resetMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }
    }
    public void playMusic(int music){

        mediaPlayer=MediaPlayer.create(getBaseContext(),music);
        try {
            mediaPlayer.prepare();
        } catch (IllegalStateException |IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void playMusic(String path){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void pauseAndReplay(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else{
            mediaPlayer.start();
        }
    }

    public void stopMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    public boolean isPlaying(){
        if(mediaPlayer!=null)
            return mediaPlayer.isPlaying();
        return false;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
}

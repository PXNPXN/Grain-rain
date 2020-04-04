package browser.green.org.bona.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import browser.green.org.bona.Database.DBHelper;
import browser.green.org.bona.Notes;
import browser.green.org.bona.R;

public class ClockActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "extra.event";

    private MediaPlayer mMediaPlayer;

    private Vibrator mVibrator;
    private Notes event;
    private  AlertDialog.Builder RemindClock;
    private  AlertDialog mDialog;
    private DBHelper mDBHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBHelper=new DBHelper(this,"Login.db",null,1);
        db=mDBHelper.getWritableDatabase();
        initData();
        clock();
    }



    private  void clock(){
        mMediaPlayer.start();
        long[] pattern=new long[]{1500,1000};
        mVibrator.vibrate(pattern,0);
        RemindClock=new AlertDialog.Builder(this);
        RemindClock.setView(R.layout.activity_clock);
        mDialog=RemindClock.show();
        Button btnConfirm=mDialog.findViewById(R.id.btn_confirm);
        TextView textView=mDialog.findViewById(R.id.tv_event);
        textView.setText(event.mContext);
        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                mVibrator.cancel();
                mDialog.dismiss();
                String sql="update notes set RemindDate=null,Is_Clocked=0 WHERE id="+event.mID;
                event.RemindDate=null;
                event.mIsClocked=0;
                db.execSQL(sql);
                mDBHelper.close();
                db.close();
                finish();
            }
        });

    }



    protected void initData() {
        mMediaPlayer=MediaPlayer.create(getApplicationContext(), R.raw.remind);
        mVibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Intent intent=getIntent();
        event=(Notes) getIntent().getSerializableExtra(EXTRA_EVENT);
        if(event==null){
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        clock();
    }

}

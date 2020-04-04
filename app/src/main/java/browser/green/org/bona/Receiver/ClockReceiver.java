package browser.green.org.bona.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import browser.green.org.bona.Activity.ClockActivity;
import browser.green.org.bona.Database.DBHelper;
import browser.green.org.bona.Notes;
import browser.green.org.bona.util.WakeLockUtil;


/**
 *
 */
public class ClockReceiver extends BroadcastReceiver {
    private static final String TAG = "ClockReceiver";
    public static final String EXTRA_EVENT_ID = "extra.event.id";
    public static final String EXTRA_EVENT_REMIND_TIME = "extra.event.remind.time";
    public static final String EXTRA_EVENT = "extra.event";
    private  DBHelper mDBHelper;
    private SQLiteDatabase db;
    private Cursor mCursor;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent.getAction());
        WakeLockUtil.wakeUpAndUnlock();
        postToClockActivity(context, intent);
    }

    private void postToClockActivity(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClass(context, ClockActivity.class);
        i.putExtra(EXTRA_EVENT_ID, intent.getIntExtra(EXTRA_EVENT_ID, -1));
        mDBHelper=new DBHelper(context,"Login.db",null,1);
        db=mDBHelper.getWritableDatabase();
        String sql="SELECT* FROM notes WHERE id="+intent.getIntExtra(EXTRA_EVENT_ID,-1);
        mCursor=db.rawQuery(sql,new String[]{});
        Notes event=new Notes();
        event.mID=mCursor.getInt(mCursor.getColumnIndex("id"));
        event.mContext=mCursor.getString(mCursor.getColumnIndex("noteContext"));
        event.EditDate=mCursor.getString(mCursor.getColumnIndex("EditDate"));
        event.RemindDate=mCursor.getString(mCursor.getColumnIndex("RemindDate"));
        event.mIsClocked=mCursor.getInt(mCursor.getColumnIndex("Is_Clocked"));
        mCursor.close();
        db.close();
        mDBHelper.close();
        if (event == null) {
            return;
        }
        i.putExtra(EXTRA_EVENT_REMIND_TIME, intent.getStringExtra(EXTRA_EVENT_REMIND_TIME));
        i.putExtra(EXTRA_EVENT, event);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public ClockReceiver() {
        super();
        Log.d(TAG, "ClockReceiver: Constructor");
    }
}

package browser.green.org.bona.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import browser.green.org.bona.Database.DBHelper;
import browser.green.org.bona.Manager.ClockManager;
import browser.green.org.bona.Notes;
import browser.green.org.bona.R;
import browser.green.org.bona.Receiver.ClockReceiver;
import browser.green.org.bona.Service.ClockService;
import browser.green.org.bona.Station;
import browser.green.org.bona.util.DateTimeUtil;

public class NoteRecycleAdapter extends RecyclerView.Adapter<NoteRecycleAdapter.myViewHodler> {
    private Context Context;
    private ArrayList<Notes> mNotesArrayList;
    private  DBHelper mDBHelper;
    private SQLiteDatabase db;
    private Cursor mCursor;
    private String Date;
    private Calendar calendar;
    private String Time;
    private AlertDialog.Builder clockDialog;
    private AlertDialog mDialog;
    private final Timer mTimer=new Timer();
    private TimerTask mTask;

    public NoteRecycleAdapter(Context context, ArrayList<Notes> mNotesArrayList){
        this.Context=context;
        this.mNotesArrayList=mNotesArrayList;
    }




    @NonNull
    @Override
    public NoteRecycleAdapter.myViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView =View.inflate(Context, R.layout.recyler_item,null);
        mDBHelper=new DBHelper(Context,"Login.db",null,1);
        db=mDBHelper.getWritableDatabase();
        return new myViewHodler(itemView);
    }

    public void onBindViewHolder(final myViewHodler holder, final int position){
        Notes data=mNotesArrayList.get(position);
        holder.mItemNoteContext.setText(data.mContext);
        holder.mItemNoteDate.setText(data.EditDate);
        holder.mItemNoteRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(position);
            }
        });

        holder.mItemNoteNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ClockDialog(position);
            }
        });

        holder.mItemNoteEdite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetNoteContext(position);
            }
        });

//            final Timer mTimer1=new Timer();
//            TimerTask mTask1;
//            final Handler handler1=new Handler(){
//                @Override
//                public void handleMessage(@NonNull Message msg) {
//                    super.handleMessage(msg);
//                    Message message=new Message();
//                    if(!mNotesArrayList.isEmpty()){
//                        if(mNotesArrayList.get(position).mIsClocked==0){
//                            holder.mItemNoteNoti.setImageResource(R.drawable.noti);
//                            notifyItemChanged(position);
//                        }else{
//                            holder.mItemNoteNoti.setImageResource(R.drawable.noti1);
//                            notifyItemChanged(position);
//                        }
//                    }
//                };
//            };
//            mTask1=new TimerTask() {
//                @Override
//                public void run() {
//                    Message message=new Message();
//                    message.what=1;
//                    handler1.sendMessage(message);
//                }
//            };
//            mTimer1.schedule(mTask1,3000,3000);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.clockdialog,null);

        return view;
    }

    /**
     *
     * @param position
     * 弹出设置闹铃对话框
     */
    public void ClockDialog(final int position){
        clockDialog=new AlertDialog.Builder(Context);
        clockDialog.setView(R.layout.clockdialog);
        mDialog=clockDialog.show();
        Button mButton1=mDialog.findViewById(R.id.bt_setClock);
        Button mButton2=mDialog.findViewById(R.id.bt_RemoveClock);
        Button mButton3=mDialog.findViewById(R.id.bt_quite);
        final TextView ClockDate=mDialog.findViewById(R.id.tv_clockDate);
        ClockDate.setText(mNotesArrayList.get(position).RemindDate);
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRemindDate(position);
                ClockDate.setText(mNotesArrayList.get(position).RemindDate);
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockManager clockManager=ClockManager.getInstance();
                clockManager.cancelAlarm(buildIntent(mNotesArrayList.get(position).mID));
                mNotesArrayList.get(position).mIsClocked=0;
                mNotesArrayList.get(position).RemindDate=null;
                notifyItemChanged(position);
                String sql="update notes set Is_Clocked=0,RemindDate=null WHERE id="+mNotesArrayList.get(position).mID;
                db.execSQL(sql);
            }
        });
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
               if(mDialog.isShowing()){
                   ClockDate.setText(mNotesArrayList.get(position).RemindDate);
               }else{
                   return;
               }

            };
        };

        mTask=new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
        };
        mTimer.schedule(mTask,500,500);
    }

    /**
     * remove notes(item-recyclerview)
     * @param position
     */
    public  void removeData(int position){
        ClockManager clockManager=ClockManager.getInstance();
        clockManager.cancelAlarm(buildIntent(mNotesArrayList.get(position).mID));
        Notes data=mNotesArrayList.get(position);
        String sql="DELETE FROM notes WHERE EditDate='"+data.EditDate+"'";
        db.execSQL(sql);
        mNotesArrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return  mNotesArrayList.size();
    }

    private PendingIntent buildIntent(int id){
        Intent intent=new Intent();
        intent.putExtra(ClockReceiver.EXTRA_EVENT_ID,id);
        intent.setClass(Context, ClockService.class);
        return PendingIntent.getService(Context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 弹出DatePickerDialog
     * @param position
     */
    public void setRemindDate(final int position){
        calendar=Calendar.getInstance();
       DatePickerDialog datePickerDialog=new DatePickerDialog(Context, new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               String time = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
               Time=time;
               ShowTime(position);
           }
       },
               calendar.get(Calendar.YEAR),
               calendar.get(Calendar.MONTH),
               calendar.get(Calendar.DAY_OF_MONTH));
       datePickerDialog.show();
       datePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     * 弹出TimePickerDialog
     * @param position
     */
    private void ShowTime(final int position){
        TimePickerDialog timePickerDialog=new TimePickerDialog(Context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour=Integer.toString(hourOfDay);
                String min=Integer.toString(minute);
                String time=hour+":"+min;
                Time=Time+" "+time;
                try{

                    if (new Date().getTime()<DateTimeUtil.str2Date(Time).getTime()){
                        mNotesArrayList.get(position).RemindDate=Time;
                        mNotesArrayList.get(position).mIsClocked=1;
                        notifyItemChanged(position);
                        String sql="update notes set RemindDate='"+Time+"',Is_Clocked=1 WHERE id="+mNotesArrayList.get(position).mID;
                        db.execSQL(sql);
                        ClockManager mClockManager=ClockManager.getInstance();
                        mClockManager.addAlarm(buildIntent(mNotesArrayList.get(position).getID()), DateTimeUtil.str2Date(mNotesArrayList.get(position).getRemindDate()));
                    }
                    else{
                        Toast toast=Toast.makeText(Context,"设置闹铃时间无效!",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },    calendar.get(Calendar.HOUR_OF_DAY),
              calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
        timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * set text of note
     * @param position
     */
    private void SetNoteContext(final int position) {
            final int temp=position+1;
            final EditText editText = new EditText(Context);
            new AlertDialog.Builder(Context).setTitle("编辑备忘录")
                    .setIcon(android.R.drawable.sym_def_app_icon)
                    .setView(editText)
                    .setPositiveButton("confirm", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date curDate = new Date(System.currentTimeMillis());
                            String Date = mFormat.format(curDate);
                         try{
                                 Notes data=mNotesArrayList.get(position);
                                 String sql="update notes set noteContext='"+editText.getText()+"',EditDate='"+Date+"' WHERE id="+data.mID;
                                 db.execSQL(sql);
                             mNotesArrayList.get(position).mContext=editText.getText().toString();
                             mNotesArrayList.get(position).EditDate=Date;
                             notifyItemChanged(position);
                         }catch (SQLException e){
                             e.printStackTrace();
                         }

                        }
                    }).setNegativeButton("quite", null).show();
    }

    /**
     * 自定义myViewHodler
     */
    class myViewHodler extends RecyclerView.ViewHolder {
        private TextView mItemNoteContext;
        private TextView mItemNoteDate;
        private ImageView mItemNoteRemove;
        private ImageView mItemNoteEdite;
        private ImageView mItemNoteNoti;

        public myViewHodler(View itemView) {
            super(itemView);
            mItemNoteContext = (TextView) itemView.findViewById(R.id.tv_note_edit);
            mItemNoteDate = (TextView) itemView.findViewById(R.id.tv_note_editdate);
            mItemNoteRemove = (ImageView) itemView.findViewById(R.id.iv_note_remove);
            mItemNoteEdite = (ImageView) itemView.findViewById(R.id.iv_note_edit);
            mItemNoteNoti = (ImageView) itemView.findViewById(R.id.iv_note_noti);



            itemView.setOnClickListener(new View.OnClickListener() {
                int flag=0;
                @Override
                public void onClick(View v) {
                    if(flag%2==0){
                        mItemNoteEdite.setVisibility(View.VISIBLE);
                        mItemNoteNoti.setVisibility(View.VISIBLE);
                        mItemNoteRemove.setVisibility(View.VISIBLE);
                        flag++;
                    }else{
                        mItemNoteEdite.setVisibility(View.INVISIBLE);
                        mItemNoteNoti.setVisibility(View.INVISIBLE);
                        mItemNoteRemove.setVisibility(View.INVISIBLE);
                        flag--;
                    }

                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(v, mNotesArrayList.get(getLayoutPosition()));

                    }
                }
            });

        }
   }

    public  interface  OnItemClickListener{
         public  void OnItemClick(View view, Notes data);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

}

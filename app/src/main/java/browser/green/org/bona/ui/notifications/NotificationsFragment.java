package browser.green.org.bona.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import browser.green.org.bona.Activity.MainActivity;
import browser.green.org.bona.Adapter.NoteRecycleAdapter;
import browser.green.org.bona.Database.DBHelper;
import browser.green.org.bona.Notes;
import browser.green.org.bona.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private View view;
    public RecyclerView mRecyclerView;
    private NoteRecycleAdapter mNoteRecycleAdapter;
    private Button mButton;
    private DBHelper mDBHelper;
    private  ArrayList<Notes> noteList=new ArrayList<Notes>();
    private SQLiteDatabase db;
    private Cursor mCursor;
    private String Date;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
                view=inflater.inflate(R.layout.fragment_notifications,container,false);
                mButton=(Button) view.findViewById(R.id.bt_note_add);
                mDBHelper=new DBHelper(getActivity(),"Login.db",null,1);
                db=mDBHelper.getWritableDatabase();
                initData();

                mButton.setOnClickListener(new View.OnClickListener() {
                    String LastDate=null;
                    @Override
                    public void onClick(View v) {
                        final EditText editText=new EditText(getActivity());
                        new AlertDialog.Builder(getActivity()).setTitle("请输入备忘录内容")
                                .setIcon(android.R.drawable.sym_def_app_icon)
                                .setView(editText)
                                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try{
                                            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            Date curDate=new Date(System.currentTimeMillis());
                                            Date=formatter.format(curDate);
                                            String text=editText.getText().toString();
                                            if(Date.equals(LastDate)){
                                                LastDate=Date;
                                                return;
                                            }
                                            if(text.isEmpty()||text==""){
                                                Toast toast=Toast.makeText(getActivity(),"请编辑备忘录内容!",Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER,0,0);
                                                toast.show();
                                                return;
                                            }
                                            db.execSQL("INSERT INTO notes VALUES(NULL,?,?,NULL,?)",new Object[]{
                                                    text, Date,0});
                                            initData();
                                            Toast toast=Toast.makeText(getActivity(),"记事本添加成功!",Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            toast.show();
                                            LastDate=Date;
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }).setNegativeButton("quite",null).show();
                    }
                });
        return view;
    }

    private void initData() {
        ArrayList<Notes> List=new ArrayList<Notes>();
        String sql="SELECT* FROM notes";
        mCursor=db.rawQuery(sql,new String[]{});
        if (mCursor.getCount()==0){
            return;
        }
        while(mCursor.moveToNext()){
            Notes note=new Notes();
            String EditContext= mCursor.getString(mCursor.getColumnIndex("noteContext"));
            String EditDate=mCursor.getString(mCursor.getColumnIndex("EditDate"));
            String RemindDate=mCursor.getString(mCursor.getColumnIndex("RemindDate"));
            Integer ID=mCursor.getInt(mCursor.getColumnIndex("id"));
            Integer Is_Clocked=mCursor.getInt(mCursor.getColumnIndex("Is_Clocked"));
            note.setID(ID);
            note.setIsClocked(Is_Clocked);
            note.setContext(EditContext);
            note.setRemindDate(RemindDate);
            note.setEditDate(EditDate);
            List.add(note);
            System.out.println("----------note内容为："+EditContext+"------创建时间为："+EditDate+"--------ID"+ID+"----Is_Clocked----"+Is_Clocked+"-------remindDate----"+RemindDate);
        }
        noteList=List;
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView=(RecyclerView)view.findViewById(R.id.rv_note);
        //create adapter
        mNoteRecycleAdapter=new NoteRecycleAdapter(getActivity(),noteList);

        mRecyclerView.setAdapter(mNoteRecycleAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));


    }


}

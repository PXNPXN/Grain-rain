package browser.green.org.bona.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import browser.green.org.bona.Database.DBHelper;
import browser.green.org.bona.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="LoginActivity" ;
    private EditText account;
    private  EditText password;
    private Button login;
    private DBHelper mDBHelper;
    private SQLiteDatabase db;
    private TextView AddNewUser;
    private Cursor mCursor;
    private Button mButton1;
    private Button mButton2;
    private TextView Updatepwd;
    private AlertDialog.Builder editDialog;
    private AlertDialog.Builder updateDialog;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //第一步，找到控件
        initview();
        initlistner();
    }

    public void initlistner(){
        AddNewUser.setOnClickListener(this);
        Updatepwd.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    private void handlerLoginEvent() {
        String accoutnTest=account.getText().toString();
        String passwordTest=password.getText().toString();
        saveUserInfoDB(accoutnTest, passwordTest);

    }

    private void saveUserInfoDB(String accoutnTest, String passwordTest){
        try{
            String sql="SELECT* FROM user WHERE username="+accoutnTest+" and password="+passwordTest;
            mCursor=db.rawQuery(sql,new String[]{});
            if(mCursor.getCount()!=0){
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
            else {
                sql="SELECT* FROM user WHERE username="+accoutnTest;
                mCursor=db.rawQuery(sql,new String[]{});
                if (mCursor.getCount()!=0){
                    Toast.makeText(LoginActivity.this,"用户密码不匹配！", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this,"该用户不存在", Toast.LENGTH_LONG).show();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public  void initview(){
        Updatepwd=(TextView)findViewById(R.id.tv_updatepwd);
        account=(EditText)findViewById(R.id.et_account);
        password=(EditText)findViewById(R.id.et_password);
        AddNewUser=(TextView)findViewById(R.id.tv_addNewUser);
        login=(Button)findViewById(R.id.bt_login);
        mDBHelper=new DBHelper(this,"Login.db",null,1);
        db=mDBHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                handlerLoginEvent();
                break;
            case R.id.tv_addNewUser:
                CreateNewUser();
                break;
            case R.id.quite:
                dialog.dismiss();
                break;
            case R.id.regist:
                AddNewUserDB();
                break;
            case R.id.tv_updatepwd:
                UpdateDialog();
                break;
            case R.id.bt_updatequite:
                dialog.dismiss();
                break;
            case R.id.bt_update:
                updatepwd();
                break;
        }
    }

    private void updatepwd() {
        EditText mEditText1=(EditText)dialog.findViewById(R.id.et_email);
        EditText mEditText2=(EditText)dialog.findViewById(R.id.et_pwd1);
        EditText mEditText3=(EditText)dialog.findViewById(R.id.et_pwd2);
        String pwd1=mEditText3.getText().toString();
        String pwd2=mEditText2.getText().toString();
        String email=mEditText1.getText().toString();
        if (email==""||pwd1==""||pwd2==""||email.isEmpty()||pwd1.isEmpty()||pwd2.isEmpty()){
            Toast toast=Toast.makeText(LoginActivity.this,"输入信息不完整!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        else{
            String sql="SELECT* FROM user WHERE username='"+email+"'";
            mCursor=db.rawQuery(sql,new String[]{});
            if(mCursor.getCount()!=0){
                if(pwd1.equals(pwd2)){
                    sql="update user set password="+pwd1+" where username="+email;
                   db.execSQL(sql);
                    Toast toast=Toast.makeText(LoginActivity.this,"密码修改成功!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    dialog.dismiss();
                }else{
                    Toast toast=Toast.makeText(LoginActivity.this,"前后密码不相符!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }

            else{
                Toast toast=Toast.makeText(LoginActivity.this,"账号未注册!",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }

    }


    private void AddNewUserDB() {
        EditText mEditText1=(EditText)dialog.findViewById(R.id.et_email);
        EditText mEditText2=(EditText)dialog.findViewById(R.id.et_pwd1);
        EditText mEditText3=(EditText)dialog.findViewById(R.id.et_pwd2);
        String pwd1=mEditText3.getText().toString();
        String pwd2=mEditText2.getText().toString();
        String email=mEditText1.getText().toString();
        if (email==""||pwd1==""||pwd2==""||email.isEmpty()||pwd1.isEmpty()||pwd2.isEmpty()){
            Toast toast=Toast.makeText(LoginActivity.this,"输入信息不完整!",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else{
            String sql="SELECT* FROM user WHERE username='"+email+"'";
            mCursor=db.rawQuery(sql,new String[]{});
            if(mCursor.getCount()==0){
                if(pwd1.equals(pwd2)){
                    db.execSQL("INSERT INTO user VALUES(NULL,?,?)",new  Object[]{
                            email,pwd1});
                    Toast toast=Toast.makeText(LoginActivity.this,"注册成功!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    dialog.dismiss();
                }
                else{
                    Toast toast=Toast.makeText(LoginActivity.this,"前后密码不相符!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }else{
                Toast toast=Toast.makeText(LoginActivity.this,"账号已存在!",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        }

    }

    private void CreateNewUser() {
        editDialog=new AlertDialog.Builder(this);
        editDialog.setIcon(R.mipmap.guyu);
        editDialog.setView(R.layout.regist);
        dialog = editDialog.show();
        mButton1=(Button)dialog.findViewById(R.id.quite);
        mButton2=(Button)dialog.findViewById(R.id.regist);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
    }

    private void UpdateDialog() {
        updateDialog=new AlertDialog.Builder(this);
        updateDialog.setIcon(R.mipmap.guyu);
        updateDialog.setView(R.layout.update_pwd);
        dialog =updateDialog.show();
        mButton1=(Button)dialog.findViewById(R.id.bt_updatequite);
        mButton2=(Button)dialog.findViewById(R.id.bt_update);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.close();
        db.close();
        mCursor.close();
    }
}

package browser.green.org.bona.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int vision){
        super(context,name,factory,vision);

    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table if not exists user(id integer " +
                "primary key autoincrement,"+"username text not null unique,"+
                "password text not null)");

        db.execSQL("create table if not exists notes(id integer " +
                "primary key autoincrement,"+"noteContext text,"+
                "EditDate text not null unique,"+
                "RemindDate text,"+"Is_Clocked integer not null)");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVision,int newVision){

    }
}

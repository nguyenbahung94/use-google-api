package Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Adam on 8/7/2016.
 */
public class ConnectSQL extends SQLiteOpenHelper {
    public ArrayList<String> listNameHostpital=new ArrayList<String>();
    public static final String DBNAME = "MyAdata.sqlite";
    public static final String DBLOCATION = "/data/data/com.example.adam.mapapi2/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;
    public ConnectSQL(Context context){

        super(context,DBNAME,null,1);
        this.mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public  void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        Log.i("DBpath",dbPath);
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }
    public ArrayList<String> testSQL(String makhoa){

        openDatabase();
        String query="SELECT * FROM LK_khoa,BenhVien WHERE BenhVien.Mabv=LK_khoa.Mabv and LK_khoa.makhoa='"+makhoa+"'";
        Cursor cursor = mDatabase.rawQuery(query, null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                listNameHostpital.add(cursor.getString(4));

                cursor.moveToNext();
            }
        }
        cursor.close();
        closeDatabase();
        Log.e("lisnamehospital",listNameHostpital.toString());
return listNameHostpital ;
    }

}

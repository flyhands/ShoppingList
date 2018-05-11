package android.vogella.de.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 7/5/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ShopList.db";
    public static final String TABLE_NAME = "list_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void deleteData(int id,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL_1 + " = '" + id + "'" +
                " AND " + COL_2 + " = '" + name + "'";
        Log.d("msg", "deleteName: query: " + query);
        Log.d("msg", "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }

//    public void deleteData(ArrayList<Integer> selectedID, ArrayList<String> selectedName){
//        SQLiteDatabase db = this.getWritableDatabase();
//        for(int i = 0;i< selectedID.size();i++) {
//            String query = "DELETE FROM " + TABLE_NAME + " WHERE "
//                    + COL_1 + " = '" + selectedID.get(i) + "'" +
//                    " AND " + COL_2 + " = '" + selectedName.get(i) + "'";
//            Log.d("msg", "deleteName: query: " + query);
//            Log.d("msg", "deleteName: Deleting " + selectedName.get(i) + " from database.");
//            db.execSQL(query);
//        }
//    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return res;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_1 + " FROM " + TABLE_NAME +
                " WHERE " + COL_2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    public void removeAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, null,null);

    }
}
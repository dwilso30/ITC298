package com.murach.tipcalculator;

/**
 * Created by wilso49 on 6/26/17.
 */
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TipDB {

    public static final String DB_NAME = "tip.db";
    public static final int DB_VERSION = 1;

    public static final String TIP_TABLE = "tip";

    public static final String TIP_ID = "_id";
    public static final int TIP_ID_COL = 0;

    public static final String BILL_DATE = "bill_date";
    public static final int BILL_DATE_COL = 1;

    public static final String BILL_AMOUNT = "bill_amount";
    public static final double BILL_AMOUNT_COL = 2;

    public static final String TIP_PERCENT = "tip_percent";
    public static final double TIP_PERCENT_COL = 3;


    public static final String CREATE_TIP_TABLE =
            "CREATE TABLE " + TIP_TABLE + " (" + TIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BILL_DATE + " INTEGER, " +
                    BILL_AMOUNT + " REAL, " +
                    TIP_PERCENT + "REAL);";

    public static final String DROP_TIP_TABLE =
            "DROP TABLE IF EXISTS " + TIP_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }
       @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TIP_TABLE);

            db.execSQL("INSERT INTO tip VALUES (1, 0, 42.50, .15)");
            db.execSQL("INSERT INTO tip VALUES (2, 0, 25.00, .20)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

            Log.d("Tip list", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(TipDB.DROP_TIP_TABLE);
            onCreate(db);
        }
    }

    //database object and database object helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //constructor
    public TipDB(Context context){
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);

    }

    //private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }
   
    public ArrayList<Tip> getTip(int id){
        String where =
                TIP_ID + "=?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(TIP_TABLE, null,
                where, whereArgs, null, null, null);
        cursor.moveToFrist();
        Tip tip = getTipFromCursor(cursor);
        if (cursor !=null)
            cursor.close();

        return tip;
    }

    private static Tip getTipFromCursor(Cursor cursor){
        if(cursor == null || cursor.getCount()==0){
            return null;
        }
        else{
            try{
                Tip tip = new Tip(
                        cursor.getInt(TIP_ID_COL),
                        cursor.getInt(BILL_DATE_COL),
                        cursor.getFloat(BILL_AMOUNT_COL),
                        cursor.getFloat(TIP_PERCENT_COL));

                return tip;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
}

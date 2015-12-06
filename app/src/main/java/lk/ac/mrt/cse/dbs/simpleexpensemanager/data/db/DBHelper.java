package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

/**
 * Created by manesh on 12/4/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "130376J.db";
    public static final int DATABASE_VERSION = 1;

    public static final String ACCOUNT_TABLE_NAME = "Account";
    public static final String TRANSACTION_TABLE_NAME = "Transactions";

    public static final String accountNo = "accountNo";
    public static final String bankName = "bankName";
    public static final String accountHolder = "accountHolder";
    public static final String balance = "balance";
    public static final String expenseType = "expenseType";
    public static final String amount = "amount";
    public static final String date = "date";
    public static final String transactionID = "transactionID";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlLiteDatabase){
        sqlLiteDatabase.execSQL("CREATE TABLE "+ACCOUNT_TABLE_NAME+"(" +
                accountNo+" varchar(15) PRIMARY KEY," +
                bankName+" varchar(30),"+
                accountHolder+" varchar(30),"+
                balance+" double"+")");

        sqlLiteDatabase.execSQL("CREATE TABLE "+TRANSACTION_TABLE_NAME+"(" +
                accountNo+" varchar(15)," +
                transactionID+" integer PRIMARY KEY AUTOINCREMENT,"+
                date+" date,"+
                expenseType+" integer,"+
                amount+" decimal(10,2)"+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Account");
        db.execSQL("DROP TABLE IF EXISTS Transactions");
        onCreate(db);
    }



}

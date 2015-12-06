package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by manesh on 12/6/15.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DBHelper dbHelper = null;
    SQLiteDatabase db = null;

    public PersistentTransactionDAO(Context context){
        if (dbHelper == null){
            dbHelper = new DBHelper(context);
        }
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = sdf.format(date);
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        ContentValues values = new ContentValues();
        values.put("date",strDate);
        if(expenseType == ExpenseType.EXPENSE){
            values.put("expenseType",1);
        }else{
            values.put("expenseType",0);
        }
        values.put("amount",amount);
        values.put("accountNo", accountNo);
        db.insert(DBHelper.TRANSACTION_TABLE_NAME, null, values);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM Transactions", null);
        res.moveToFirst();
        while(res.moveToNext()){
            DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = formatter.parse(res.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ExpenseType expenseType =ExpenseType.EXPENSE;
            if(res.getInt(3) == 0 ){
                expenseType = ExpenseType.INCOME;
            }
            Transaction transaction = new Transaction(date,res.getString(0),expenseType,res.getDouble(4));
            transactionList.add(transaction);
        }
        if(!res.isClosed()){
            res.close();
        }

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM Transactions LIMIT "+limit, null);
        res.moveToFirst();
        while(res.moveToNext()){
            DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = formatter.parse(res.getString(2));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ExpenseType expenseType =ExpenseType.EXPENSE;
            if(res.getInt(3) == 0 ){
                expenseType = ExpenseType.INCOME;
            }
            Transaction transaction = new Transaction(date, res.getString(0),expenseType, res.getDouble(4));
            transactionList.add(transaction);
        }
        if(!res.isClosed()){
            res.close();
        }
        return transactionList;
    }
}

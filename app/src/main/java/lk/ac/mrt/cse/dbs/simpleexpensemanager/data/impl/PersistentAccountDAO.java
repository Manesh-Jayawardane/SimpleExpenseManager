package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by manesh on 12/6/15.
 */
public class PersistentAccountDAO implements AccountDAO {
    SQLiteDatabase db = null;
    private DBHelper dbHelper = null;

    public PersistentAccountDAO(Context context){
        if (dbHelper == null){
            dbHelper = new DBHelper(context);
        }
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor res = db.rawQuery("SELECT accountNo FROM Account",null);
        res.moveToFirst();
        List<String> accNoList = new ArrayList<String>();
        while (res.moveToNext()){
            accNoList.add(res.getString(res.getColumnIndex(DBHelper.accountNo)));

        }

        if(!res.isClosed()){
            res.close();
        }

        return accNoList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        Cursor res = db.rawQuery("SELECT * FROM Account WHERE accountNo = '"+accountNo+"'",null);
        res.moveToFirst();
        String bank = res.getString(res.getColumnIndex(DBHelper.bankName));
        String accHolder = res.getString(res.getColumnIndex(DBHelper.accountHolder));
        double balance = res.getDouble(res.getColumnIndex(DBHelper.balance));

        if(!res.isClosed()){
            res.close();
        }

        return new Account(accountNo, bank, accHolder, balance);
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor res = db.rawQuery("SELECT * FROM Account", null);
        res.moveToFirst();
        List<Account> accountList = new ArrayList<Account>();
        while (res.moveToNext()){
            String accountNo = res.getString(res.getColumnIndex(DBHelper.accountNo));
            String accountHolder = res.getString(res.getColumnIndex(DBHelper.accountHolder));
            String bank = res.getString(res.getColumnIndex(DBHelper.bankName));
            double balance = res.getDouble(res.getColumnIndex(DBHelper.balance));
            accountList.add(new Account(accountNo, bank, accountHolder, balance));
        }

        if(!res.isClosed()){
            res.close();
        }

        return accountList;
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.accountNo, account.getAccountNo());
        values.put(DBHelper.bankName, account.getBankName());
        values.put(DBHelper.accountHolder,account.getAccountHolderName());
        values.put(DBHelper.balance, account.getBalance());
        db.insert(DBHelper.ACCOUNT_TABLE_NAME, null, values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.delete(DBHelper.ACCOUNT_TABLE_NAME, DBHelper.accountNo + "="+accountNo, null);

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        ContentValues values = new ContentValues();
        values.put(DBHelper.accountNo, accountNo);
        switch (expenseType) {
            case EXPENSE:
                values.put(DBHelper.balance, account.getBalance()-amount);
                break;
            case INCOME:
                values.put(DBHelper.balance, account.getBalance() + amount);
                break;
        }
        db.update(DBHelper.ACCOUNT_TABLE_NAME, values, "accountNo = '"+ accountNo+"'",null);
    }
}

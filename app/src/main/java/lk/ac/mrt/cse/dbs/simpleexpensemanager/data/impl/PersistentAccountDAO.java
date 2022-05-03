package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private DBHelper dbHelper;

    public PersistentAccountDAO(){
        dbHelper = DBHelper.getInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT account_num FROM accounts;", null);
        List<String> account_numbers = new ArrayList<>();
        while(cursor.moveToNext()) {
            String item = cursor.getString(
                    cursor.getColumnIndexOrThrow("account_num"));
            account_numbers.add(item);
        }
        cursor.close();
        return account_numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM accounts;", null);
        List<Account> accounts = new ArrayList<>();
        while(cursor.moveToNext()) {
            String account_num = cursor.getString(
                    cursor.getColumnIndexOrThrow("account_num"));
            String bank_name = cursor.getString(
                    cursor.getColumnIndexOrThrow("bank_name"));
            String account_name = cursor.getString(
                    cursor.getColumnIndexOrThrow("account_name"));
            double balance = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("balance"));
            Account account = new Account(account_num, bank_name, account_name, balance);
            accounts.add(account);
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM accounts WHERE account_num = " + accountNo, null);

        cursor.moveToFirst();
            String account_num = cursor.getString(
                    cursor.getColumnIndexOrThrow("account_num"));
            String bank_name = cursor.getString(
                    cursor.getColumnIndexOrThrow("bank_name"));
            String account_name = cursor.getString(
                    cursor.getColumnIndexOrThrow("account_name"));
            double balance = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("balance"));
            Account account = new Account(account_num, bank_name, account_name, balance);
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("account_num",account.getAccountNo());
        contentValues.put("bank_name",account.getBankName());
        contentValues.put("account_name",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());

        sqLiteDatabase.insert("accounts", null, contentValues);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] args = {accountNo};
        sqLiteDatabase.delete("accounts", "account_num = ?", args);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        double initial_amount = account.getBalance();
        double newAmount = 0;
        if (expenseType == ExpenseType.EXPENSE){
            newAmount = initial_amount -amount;
        }else {
            newAmount = initial_amount + amount;
        }
//        System.out.println("update balance line 109-------/  " + newAmount+ expenseType + accountNo);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("balance", newAmount);
        int result = sqLiteDatabase.update("accounts", contentValues, "account_num = ?", new String[]{accountNo});

    }
}

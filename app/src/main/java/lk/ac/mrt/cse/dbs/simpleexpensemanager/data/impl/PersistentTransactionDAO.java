package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DBHelper dbHelper;

    public PersistentTransactionDAO(){
        dbHelper = DBHelper.getInstance();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_num",accountNo);
        contentValues.put("date", new  SimpleDateFormat("dd-MM-yyyy").format(date));
        contentValues.put("type", String.valueOf(expenseType));
        contentValues.put("amount",amount);
        long id = sqLiteDatabase.insert("transactions",null,contentValues);
        System.out.println("line 40 :: "+id);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions;", null);
        List<Transaction> transactions = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        while(cursor.moveToNext()) {

            Date date = dateFormat.parse(cursor.getString(
                    cursor.getColumnIndexOrThrow("date")));

            String account_num = cursor.getString(
                    cursor.getColumnIndexOrThrow("account_num"));
            String type = cursor.getString(
                    cursor.getColumnIndexOrThrow("type"));
            double amount = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("amount"));
            Transaction transaction = new Transaction(date,account_num, ExpenseType.valueOf(type),amount);
            transactions.add(transaction);

        }
        cursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> transactions = getAllTransactionLogs();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}

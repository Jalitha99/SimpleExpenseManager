package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    public static void createInstance(Context context){
        instance = new DBHelper(context);
    }

    public static DBHelper getInstance(){
        return instance;
    }

    public DBHelper(@Nullable Context context) {
        super(context, "expenseManager", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String TABLE_ACCOUNTS = "accounts";
        String TABLE_TRANSACTIONS = "transactions";

        String CREATE_TABLE_ACCOUNTS = "CREATE TABLE IF NOT EXISTS "+ TABLE_ACCOUNTS +
                                         " (account_num TEXT PRIMARY KEY, bank_name TEXT, account_name TEXT, balance REAL);";
        String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE IF NOT EXISTS "+TABLE_TRANSACTIONS +
                                       " ( id INTEGER PRIMARY KEY, account_num TEXT," +
                                            " date DATE, type TEXT, amount REAL , FOREIGN KEY(account_num) REFERENCES accounts (account_num));";

        sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS accounts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(sqLiteDatabase);
    }
}

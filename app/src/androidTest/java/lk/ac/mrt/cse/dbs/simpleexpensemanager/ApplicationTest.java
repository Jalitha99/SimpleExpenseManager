/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DB.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest{
    private static ExpenseManager expenseManager;
    @BeforeClass
    public static void testAddAccount() throws ExpenseManagerException {
        Context context = ApplicationProvider.getApplicationContext();
        DBHelper.createInstance(context);
        expenseManager = new PersistentExpenseManager();
        expenseManager.addAccount("5555","BOC", "maina", 8000);



    }
    @Test
    public void checkTransaction(){
        double beforeTransactions = 0;
        double afterTransactions = 0;
        try {
            beforeTransactions = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
            expenseManager.updateAccountBalance("5555",11, 5, 2022, ExpenseType.valueOf("EXPENSE"), "400");
            afterTransactions = expenseManager.getTransactionsDAO().getAllTransactionLogs().size();
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            assertTrue(afterTransactions-beforeTransactions == 1);
        }


    }

    @Test
    public void checkAddAccount(){
        try {
            assertTrue(expenseManager.getAccountsDAO().getAccount("5555").getAccountNo().equals("5555") );
        } catch (InvalidAccountException e) {
            fail();
        }
    }
}
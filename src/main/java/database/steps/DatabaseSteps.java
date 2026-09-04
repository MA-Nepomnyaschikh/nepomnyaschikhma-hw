package database.steps;

import database.core.DatabaseClient;
import database.conditions.Condition;
import database.tables.Accounts;
import database.tables.Customers;
import database.tables.Transactions;
import database.models.Account;
import database.models.Customer;
import database.models.Transaction;

import java.util.List;

import static database.tables.Accounts.ACCOUNTS;
import static database.tables.Accounts.CUSTOMER_ID;
import static database.tables.Customers.CUSTOMERS;
import static database.tables.Customers.USERNAME;
import static database.tables.Transactions.ACCOUNT_ID;
import static database.tables.Transactions.TRANSACTIONS;

public class DatabaseSteps {

    private final DatabaseClient db;

    public DatabaseSteps(DatabaseClient db) {
        this.db = db;
    }

    public Customer getCustomerById(long customerId) {
        return db.select("*")
                .from(CUSTOMERS)
                .where(Condition.eq(Customers.ID, customerId))
                .build()
                .executeAndGet(Customer.class);
    }

    public Customer getCustomerByUsername(String username) {
        return db.select("*")
                .from(CUSTOMERS)
                .where(Condition.eq(USERNAME, username))
                .build()
                .executeAndGet(Customer.class);
    }

    public List<Customer> getAllCustomers() {
        return db.select("*")
                .from(CUSTOMERS)
                .orderBy("id ASC")
                .build()
                .executeAndGetList(Customer.class);
    }

    public Account getCustomerAccount(long customerId, long accountId) {
        return db.select("*")
                .from(ACCOUNTS)
                .where(Condition.eq(CUSTOMER_ID, customerId)
                        .and(Condition.eq(Accounts.ID, accountId)))
                .build()
                .executeAndGet(Account.class);
    }

    public List<Account> getCustomerAccounts(long customerId) {
        return db.select("*")
                .from(ACCOUNTS)
                .where(Condition.eq(CUSTOMER_ID, customerId))
                .build()
                .executeAndGetList(Account.class);
    }

    public Transaction getAccountTransaction(long accountId, long transactionId) {
        return db.select("*")
                .from(TRANSACTIONS)
                .where(Condition.eq(ACCOUNT_ID, accountId)
                        .and(Condition.eq(Transactions.ID, transactionId)))
                .build()
                .executeAndGet(Transaction.class);
    }

    public List<Transaction> getAccountTransactions(long accountId) {
        return db.select("*")
                .from(TRANSACTIONS)
                .where(Condition.eq(ACCOUNT_ID, accountId))
                .orderBy("id ASC")
                .build()
                .executeAndGetList(Transaction.class);
    }
}
package steps;

import dao.DatabaseClient;
import dao.conditions.Condition;
import dao.tables.Accounts;
import dao.tables.Customers;
import dao.tables.Transactions;
import models.db.Account;
import models.db.Customer;
import models.db.Transaction;

import java.util.List;

import static dao.tables.Accounts.ACCOUNTS;
import static dao.tables.Accounts.CUSTOMER_ID;
import static dao.tables.Customers.CUSTOMERS;
import static dao.tables.Customers.USERNAME;
import static dao.tables.Transactions.ACCOUNT_ID;
import static dao.tables.Transactions.TRANSACTIONS;

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
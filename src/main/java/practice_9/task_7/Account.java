package practice_9.task_7;

import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private static int counter = 0;
    private int balance;
    private final int accountNumber;
    private final ReentrantLock lock = new ReentrantLock();

    public Account(int balance) {
        this.balance = balance;
        this.accountNumber = ++counter;
    }

    public int getBalance() {
        return balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    public void withdraw(int amount) {
        this.balance -= amount;
    }

    public void transfer(Account targetAccount, int amount) {

        Account first = this.getAccountNumber() < targetAccount.getAccountNumber() ? this : targetAccount;
        Account second = this.getAccountNumber() < targetAccount.getAccountNumber() ? targetAccount : this;

        first.lock.lock();
        try {
            second.lock.lock();
            try {
                if (this.balance > 0 && this.balance >= amount) {
                    this.withdraw(amount);
                    targetAccount.deposit(amount);
                    System.out.println("Перевод на счет " + targetAccount.getAccountNumber() + " со счета " + this.getAccountNumber() + " выполнен успешно. Текущий баланс: " + this.balance);
                } else {
                    System.out.println("Недостаточно средств на счёте");
                }
            } finally {
                second.lock.unlock();
            }
        } finally {
            first.lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Account number: " + this.accountNumber + ", balance: " + this.balance;
    }
}

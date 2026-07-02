package com.example.androidpractice;

import java.util.ArrayList;

public class BankManager {
    ArrayList<Account> accounts = new ArrayList<>();

    public boolean accountExist(int accountNumber){
        for(Account account : accounts){
            if(account.getAccountNumber() == accountNumber){
                return true;
            }
        }
        return false;
    }
    public boolean createAccount(Account account){
        if(accountExist(account.getAccountNumber())){
            return false;
        }
        accounts.add(account);
        return true;
    }

    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }

    public Account searchAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    public boolean depositMoney(int accountNumber, double amount){
        if (amount <= 0) {
            return false;
        }

        for (Account account : accounts) {

            if (account.getAccountNumber() == accountNumber) {

                account.setBalance(account.getBalance() + amount);
                return true;
            }
        }

        return false;
    }

    public boolean withdrawMoney(int accountNumber, double amount){
        if(amount <= 0){
            return false;
        }
        for (Account account : accounts){
            if (account.getAccountNumber() == accountNumber){
                if(account.getBalance() < amount){
                    return false;
                }else {
                    account.setBalance(account.getBalance() - amount);
                    return true;
                }
            }
        }
        return false;
    }
}
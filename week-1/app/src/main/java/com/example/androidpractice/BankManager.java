package com.example.androidpractice;

import java.util.ArrayList;

public class BankManager {
    ArrayList<Account> accounts = new ArrayList<>();

    public boolean accountExist(String accountNumber){
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

    public Account searchAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    public boolean depositMoney(String accountNumber, String amount){
        if (Double.parseDouble(amount) <= 0) {
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

    public boolean withdrawMoney(String accountNumber, String  amount){
        if(Double.parseDouble(amount) <= 0){
            return false;
        }
        for (Account account : accounts){
            if (account.getAccountNumber() == accountNumber){
                if(Double.parseDouble(account.getBalance()) < Double.parseDouble(amount)){
                    return false;
                }else {
                    account.setBalance(String.valueOf(Double.parseDouble(account.getBalance()) - Double.parseDouble(amount)));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean transferMoney(String senderAccountNumber, String receiverAccountNumber, String amount) {

        if (Double.parseDouble(amount) <= 0) {
            return false;
        }

        Account sender = null;
        Account receiver = null;

        for (Account account : accounts) {
            if (account.getAccountNumber().equals(senderAccountNumber)) {
                sender = account;
            }

            if (account.getAccountNumber().equals(receiverAccountNumber)) {
                receiver = account;
            }
        }

        if (sender == null || receiver == null) {
            return false;
        }

        if (Double.parseDouble(sender.getBalance()) < Double.parseDouble(amount)) {
            return false;
        }

        sender.setBalance(String.valueOf(Double.parseDouble(sender.getBalance()) - Double.parseDouble(amount)));
        receiver.setBalance(String.valueOf(Double.parseDouble(receiver.getBalance()) + Double.parseDouble(amount)));

        return true;
    }

    public boolean deleteAccount(String deleteAccNo){
        for (Account account : accounts) {

            if (Integer.parseInt(account.getAccountNumber()) == Integer.parseInt(deleteAccNo)) {
                accounts.remove(account);
                return true;
            }
        }
        return false;
    }
}
package com.example.androidpractice;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidpractice.validation.CreateAccountValidation;
import com.example.androidpractice.validation.ValidationResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button one, two, three, four, five, six, seven, eight;
    BankManager bank = new BankManager();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dialog dialog = new Dialog(MainActivity.this);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);

        one.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createAccountDialog();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAllAccountDialog();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAccountDialog();
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositMoneyDialog();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawMoneyDialog();
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferMoneyDialog();
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountDialog();
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to Exit?");
                builder.setCancelable(true);
                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    public void createAccountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.create_account, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button submit, cancel;
        EditText accountNumber, accountHolderName, phoneNumber, email, address, balance;
        RadioGroup accountType;

        submit = view.findViewById(R.id.submit);
        cancel = view.findViewById(R.id.cancel);
        accountNumber = view.findViewById(R.id.accountNumber);
        accountHolderName = view.findViewById(R.id.accountHolderName);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        accountType = view.findViewById(R.id.accountType);
        balance = view.findViewById(R.id.balance);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accNo = accountNumber.getText().toString();
                String accHn = accountHolderName.getText().toString();
                String phNo = phoneNumber.getText().toString();
                String em = email.getText().toString();
                String addr = address.getText().toString();
                int selectedId = accountType.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    Toast.makeText(MainActivity.this, "Please select an account type.", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton radioButton = findViewById(selectedId);
                String accountTypeValue = radioButton.getText().toString();
                String bal = balance.getText().toString();

                ValidationResult result = CreateAccountValidation.accountValidate(
                        accNo,
                        accHn,
                        phNo,
                        em,
                        addr,
                        accountTypeValue,
                        bal
                );

                if (!result.getValid()) {

                    switch (result.getField()) {

                        case "accountNumber":
                            accountNumber.setError(result.getMessage());
                            accountNumber.requestFocus();
                            break;

                        case "accountHolderName":
                            accountHolderName.setError(result.getMessage());
                            accountHolderName.requestFocus();
                            break;

                        case "phoneNumber":
                            phoneNumber.setError(result.getMessage());
                            phoneNumber.requestFocus();
                            break;

                        case "email":
                            email.setError(result.getMessage());
                            email.requestFocus();
                            break;

                        case "address":
                            address.setError(result.getMessage());
                            address.requestFocus();
                            break;

                        case "balance":
                            balance.setError(result.getMessage());
                            balance.requestFocus();
                            break;
                    }

                    return;
                }

                Account account = new Account(accNo, accHn, phNo, em, addr, accountTypeValue, bal);
                boolean create = bank.createAccount(account);

                if(create){
                    Toast.makeText(MainActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(MainActivity.this, "Account already exist.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    public void viewAllAccountDialog(){
        Button end;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.view_all_acount, null);
        builder.setView(view);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Account> accountList = bank.getAllAccounts();

        if(accountList.isEmpty()){
            Toast.makeText(this, "No account found", Toast.LENGTH_SHORT).show();
            return;
        }

        ViewAllAccountAdapter adapter = new ViewAllAccountAdapter(accountList);
        recyclerView.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        end = view.findViewById(R.id.end);

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void searchAccountDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.search_account, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText accountNumber = view.findViewById(R.id.accountNumber);
        TextView display = view.findViewById(R.id.display);

        Button submit = view.findViewById(R.id.submit);
        Button cancel = view.findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String accountNoText = accountNumber.getText().toString().trim();

                if (accountNoText.isEmpty()) {
                    accountNumber.setError("Account number is required");
                    accountNumber.requestFocus();
                    return;
                }

                int accNo;

                try {
                    accNo = Integer.parseInt(accountNoText);
                } catch (NumberFormatException e) {
                    accountNumber.setError("Enter a valid account number");
                    accountNumber.requestFocus();
                    return;
                }

                Account account = bank.searchAccount(accNo);

                if (account != null) {

                    display.setText(
                            "Account Number : " + account.getAccountNumber() +
                                    "\n\nAccount Holder : " + account.getAccountHolderName() +
                                    "\nPhone Number : " + account.getPhoneNumber() +
                                    "\nEmail : " + account.getEmail() +
                                    "\nAddress : " + account.getAddress() +
                                    "\nAccount Type : " + account.getAccountType() +
                                    "\nBalance : Rs. " + account.getBalance()
                    );

                } else {
                    display.setText("Account not found.");
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    public void depositMoneyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.deposit_money, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText accountNumber, amount;
        Button deposit, cancel;

        accountNumber = view.findViewById(R.id.accountNumber);
        amount = view.findViewById(R.id.amount);
        deposit = view.findViewById(R.id.deposit);
        cancel = view.findViewById(R.id.cancel);

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNoText = accountNumber.getText().toString().trim();
                String  balance = amount.getText().toString().trim();

                if (accountNoText.isEmpty()) {
                    accountNumber.setError("Account number is required");
                    accountNumber.requestFocus();
                    return;
                }
                if (balance.isEmpty()) {
                    amount.setError("Deposit amount is required");
                    amount.requestFocus();
                    return;
                }

                int accNo;
                double money;

                try {
                    accNo = Integer.parseInt(accountNoText);
                } catch (NumberFormatException e) {
                    accountNumber.setError("Enter a valid account number");
                    accountNumber.requestFocus();
                    return;
                }

                try{
                    money = Double.parseDouble(balance);
                } catch(NumberFormatException e){
                    amount.setError("Enter a valid amount");
                    amount.requestFocus();
                    return;
                }

                Account account = bank.searchAccount(accNo);

                if (account != null) {

                    boolean success = bank.depositMoney(accNo, money);

                    if (success) {
                        Toast.makeText(MainActivity.this,
                                "Deposit Successful",
                                Toast.LENGTH_SHORT).show();

                        alertDialog.dismiss();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Money Deposit Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    public void withdrawMoneyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.withdraw_money, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText accountNumber, amount;
        Button withdraw, cancel;

        accountNumber = view.findViewById(R.id.accountNumber);
        amount = view.findViewById(R.id.amount);
        withdraw = view.findViewById(R.id.withdraw);
        cancel = view.findViewById(R.id.cancel);

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountNoText = accountNumber.getText().toString().trim();
                String  balance = amount.getText().toString().trim();

                if (accountNoText.isEmpty()) {
                    accountNumber.setError("Account number is required");
                    accountNumber.requestFocus();
                    return;
                }
                if (balance.isEmpty()) {
                    amount.setError("Withdraw amount is required");
                    amount.requestFocus();
                    return;
                }

                int accNo;
                double money;

                try {
                    accNo = Integer.parseInt(accountNoText);
                } catch (NumberFormatException e) {
                    accountNumber.setError("Enter a valid account number");
                    accountNumber.requestFocus();
                    return;
                }

                try{
                    money = Double.parseDouble(balance);
                } catch(NumberFormatException e){
                    amount.setError("Enter a valid amount");
                    amount.requestFocus();
                    return;
                }

                Account account = bank.searchAccount(accNo);

                if (account != null) {

                    boolean success = bank.withdrawMoney(accNo, money);

                    if (success) {
                        Toast.makeText(MainActivity.this,
                                "Withdraw Successful",
                                Toast.LENGTH_SHORT).show();

                        alertDialog.dismiss();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Money Withdraw Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    public void transferMoneyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.transfer_money, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText senderAccountNumber, receiverAccountNumber, amount;
        Button transfer, cancel;

        senderAccountNumber = view.findViewById(R.id.senderAccountNumber);
        receiverAccountNumber = view.findViewById(R.id.receiverAccountNumber);
        amount = view.findViewById(R.id.amount);
        transfer = view.findViewById(R.id.transfer);
        cancel = view.findViewById(R.id.cancel);

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderAccountNoText = senderAccountNumber.getText().toString().trim();
                String receiverAccountNoText = receiverAccountNumber.getText().toString().trim();
                String  balance = amount.getText().toString().trim();

                if (senderAccountNoText.isEmpty()) {
                    senderAccountNumber.setError("Sender account number is required");
                    senderAccountNumber.requestFocus();
                    return;
                }
                if (receiverAccountNoText.isEmpty()) {
                    receiverAccountNumber.setError("Receiver account number is required");
                    receiverAccountNumber.requestFocus();
                    return;
                }
                if (balance.isEmpty()) {
                    amount.setError("Withdraw amount is required");
                    amount.requestFocus();
                    return;
                }

                int senderAccNo, receiverAccNo;
                double money;

                try {
                    senderAccNo = Integer.parseInt(senderAccountNoText);
                } catch (NumberFormatException e) {
                    senderAccountNumber.setError("Enter a valid account number");
                    senderAccountNumber.requestFocus();
                    return;
                }

                try {
                    receiverAccNo = Integer.parseInt(receiverAccountNoText);
                } catch (NumberFormatException e) {
                    receiverAccountNumber.setError("Enter a valid account number");
                    receiverAccountNumber.requestFocus();
                    return;
                }

                try{
                    money = Double.parseDouble(balance);
                } catch(NumberFormatException e){
                    amount.setError("Enter a valid amount");
                    amount.requestFocus();
                    return;
                }

                Account sender = bank.searchAccount(senderAccNo);
                Account receiver = bank.searchAccount(receiverAccNo);

                if (sender != null && receiver != null) {

                    boolean success = bank.transferMoney(senderAccNo, receiverAccNo, money);

                    if (success) {
                        Toast.makeText(MainActivity.this,
                                "Transfer Successful",
                                Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Transfer Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    public void deleteAccountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_account, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        EditText accountNumber;
        Button delete, cancel;

        accountNumber = view.findViewById(R.id.accountNumber);
        delete = view.findViewById(R.id.delete);
        cancel = view.findViewById(R.id.cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accNo = accountNumber.getText().toString().trim();

                if(accNo.isEmpty()){
                    accountNumber.setError("Account number is required");
                    accountNumber.requestFocus();
                    return;
                }

                int deleteAccNo;

                try {
                    deleteAccNo = Integer.parseInt(accNo);
                } catch (NumberFormatException e) {
                    accountNumber.setError("Enter a valid account number");
                    accountNumber.requestFocus();
                    return;
                }

                Account account = bank.searchAccount(deleteAccNo);

                if (account != null) {
                    boolean delete = bank.deleteAccount(deleteAccNo);

                    if (delete) {
                        Toast.makeText(MainActivity.this,
                                "Account delete successful",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Account deletion failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Clicked cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
package com.example.androidpractice;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    }
    public void createAccountDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.create_account, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button submit, cancel;
        EditText accountNumber, accountHolderName, phoneNumber, email, address, accountType, balance;

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
                String accTy = accountType.getText().toString();
                String bal = balance.getText().toString();

                if(accNo.isEmpty()){
                    accountNumber.setError("Please enter an account number.");
                    accountNumber.requestFocus();
                    return;
                }

                if(accHn.isEmpty()){
                    email.setError("Please enter an account holder name.");
                    email.requestFocus();
                    return;
                }

                if(phNo.isEmpty()){
                    phoneNumber.setError("Please enter a phone number.");
                    phoneNumber.requestFocus();
                    return;
                }

                if(em.isEmpty()){
                    email.setError("Please enter an email.");
                    email.requestFocus();
                    return;
                }

                if(addr.isEmpty()){
                    address.setError("Please enter the address.");
                    address.requestFocus();
                    return;
                }

                if(accTy.isEmpty()){
                    accountType.setError("Please enter an account type.");
                    accountType.requestFocus();
                    return;
                }

                if(bal.isEmpty()){
                    balance.setError("Please enter a balance.");
                    balance.requestFocus();
                    return;
                }
                int accNumber;
                double balan;
                try {
                    accNumber = Integer.parseInt(accNo);
                }catch(NumberFormatException e){
                    accountNumber.setError("Enter a valid account number.");
                    accountNumber.requestFocus();
                    return;
                }

                try {
                    balan = Double.parseDouble(bal);
                }catch(NumberFormatException e){
                    balance.setError("Enter a valid balance.");
                    balance.requestFocus();
                    return;
                }

                Account account = new Account(accNumber, accHn, phNo, em, addr, accTy, balan);
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
    }
}
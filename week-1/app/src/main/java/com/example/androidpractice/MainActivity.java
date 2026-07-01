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

public class MainActivity extends AppCompatActivity {

    Button one, two, three, four, five, six, seven, eight;
    BankManager bank = new BankManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dialog dialog = new Dialog(MainActivity.this);
        one = findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createAccountDialog();
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
        accountNumber = findViewById(R.id.accountNumber);
        accountHolderName = view.findViewById(R.id.accountHolderName);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        accountType = view.findViewById(R.id.accountType);
        balance = view.findViewById(R.id.balance);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int accNo = Integer.parseInt(accountNumber.getText().toString());
                String accHn = accountHolderName.getText().toString();
                String phNo = phoneNumber.getText().toString();
                String em = email.getText().toString();
                String addr = address.getText().toString();
                String accTy = accountType.getText().toString();
                double bal = Double.parseDouble(balance.getText().toString());

                Account account = new Account(accNo, accHn, phNo, em, addr, accTy, bal);
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
}
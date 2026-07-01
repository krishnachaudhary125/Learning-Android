package com.example.androidpractice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewAllAccountAdapter extends RecyclerView.Adapter<ViewAllAccountAdapter.MyViewHolder> {

    private List<Account> accountList;

    public ViewAllAccountAdapter(List<Account> accountList){
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_all_account_recyclerview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Account  account = accountList.get(position);

        holder.accountNumber.setText("Account Number          : "+ account.getAccountNumber());
        holder.accountHolderName.setText("Account Holder Name : "+account.getAccountHolderName());
        holder.phoneNumber.setText("Phone Number              : "+account.getPhoneNumber());
        holder.email.setText("Email                           : "+account.getEmail());
        holder.address.setText("Address                       : "+account.getAddress());
        holder.accountType.setText("Account Type              : "+account.getAccountType());
        holder.balance.setText("Balance                       : "+account.getBalance());
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView accountNumber, accountHolderName, phoneNumber, email, address, accountType, balance;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            accountNumber = itemView.findViewById(R.id.accountNumber);
            accountHolderName = itemView.findViewById(R.id.accountHolderName);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            email = itemView.findViewById(R.id.email);
            address = itemView.findViewById(R.id.address);
            accountType = itemView.findViewById(R.id.accountType);
            balance = itemView.findViewById(R.id.balance);
        }
    }
}

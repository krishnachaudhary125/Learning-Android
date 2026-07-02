package com.example.androidpractice.validation

import com.example.androidpractice.Account
import com.example.androidpractice.BankManager

object TransferMoneyValidation {

    @JvmStatic
    fun transferValidate(
        senderAccountNo : String,
        receiverAccountNo : String,
        balance : String
    ): ValidationResult{
        var bank = BankManager()
        val accountRegex = Regex("^\\d+$")
        val balanceRegex = Regex("^\\d+(\\.\\d{1,2})?\$")

        if (senderAccountNo.isBlank()) {
            return ValidationResult(
                false,
                "senderAccountNumber",
                "Account number is required."
            )
        }else if(!senderAccountNo.matches(accountRegex)){
            return ValidationResult(
                false,
                "senderAccountNumber",
                "Invalid account number."
            )
        }
        if (receiverAccountNo.isBlank()) {
            return ValidationResult(
                false,
                "receiverAccountNumber",
                "Account number is required."
            )
        }else if(!receiverAccountNo.matches(accountRegex)){
            return ValidationResult(
                false,
                "receiverAccountNumber",
                "Invalid account number."
            )
        }

        if (balance.isBlank()) {
            return ValidationResult(
                false,
                "balance",
                "Amount is required."
            )
        }else if(!balance.matches(balanceRegex)){
            return ValidationResult(
                false,
                "balance",
                "Invalid amount."
            )
        }

        val senderAccount: Account? = bank.searchAccount(senderAccountNo)
        val receiverAccount: Account? = bank.searchAccount(receiverAccountNo)

        if (senderAccount == null) {
            return ValidationResult(
                false,
                "senderAccountNumber",
                "Account not found."
            )
        }
        if (receiverAccount == null){
            return ValidationResult(
                false,
                "receiverAccountNumber",
                "Account not found."
            )
        }

        return ValidationResult(
            true,
            "",
            ""
        )
    }
}
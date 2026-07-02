package com.example.androidpractice.validation

import com.example.androidpractice.Account
import com.example.androidpractice.BankManager

object WithdrawMoneyValidation {

    @JvmStatic
    fun withdrawValidate(
        accountNo : String,
        balance : String
    ): ValidationResult{
        var bank = BankManager()
        val accountRegex = Regex("^\\d+$")
        val balanceRegex = Regex("^\\d+(\\.\\d{1,2})?\$")

        if (accountNo.isBlank()) {
            return ValidationResult(
                false,
                "accountNumber",
                "Account number is required."
            )
        }else if(!accountNo.matches(accountRegex)){
            return ValidationResult(
                false,
                "accountNumber",
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

        val account: Account? = bank.searchAccount(accountNo)

        if (account == null) {
            return ValidationResult(
                false,
                "accountNumber",
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
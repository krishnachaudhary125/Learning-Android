package com.example.androidpractice.validation

import com.example.androidpractice.Account
import com.example.androidpractice.BankManager

object SearchAccountValidation {

    @JvmStatic
    fun searchValidate(
        accNo : String
    ): ValidationResult{
        var bank = BankManager()

        val accountRegex = Regex("^\\d+$")

        if (accNo.isBlank()) {
            return ValidationResult(
                false,
                "",
                "Account number is required."
            )
        }else if(!accNo.matches(accountRegex)){
            return ValidationResult(
                false,
                "",
                "Invalid account number."
            )
        }
        var account: Account? = bank.searchAccount(accNo)

        if (account == null) {
            return ValidationResult(
                false,
                "error",
                "Account not found."
            )
        }

        val accountDetails = String.format(
            "Account Number : %s\n\n" +
                    "Account Holder : %s\n" +
                    "Phone Number : %s\n" +
                    "Email : %s\n" +
                    "Address : %s\n" +
                    "Account Type : %s\n" +
                    "Balance : Rs. %.2f",
            account!!.getAccountNumber(),
            account.getAccountHolderName(),
            account.getPhoneNumber(),
            account.getEmail(),
            account.getAddress(),
            account.getAccountType(),
            account.getBalance()
        )

        return ValidationResult(
            true,
            "accountInfo",
            accountDetails
        )
    }
}
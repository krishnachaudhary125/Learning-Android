package com.example.androidpractice.validation

import com.example.androidpractice.Account
import com.example.androidpractice.BankManager

object DeleteAccountValidation {

    @JvmStatic
    fun deleteValidate(
        accNo : String
    ): ValidationResult{
        var bank = BankManager()
        val accountRegex = Regex("^\\d+$")

        if (accNo.isBlank()) {
            return ValidationResult(
                false,
                "accountNumber",
                "Account number is required."
            )
        }else if(!accNo.matches(accountRegex)){
            return ValidationResult(
                false,
                "accountNumber",
                "Invalid account number."
            )
        }

        val account: Account? = bank.searchAccount(accNo)
        return ValidationResult(
            true,
            "",
            ""
        )
    }
}
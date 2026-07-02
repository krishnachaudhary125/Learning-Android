package com.example.androidpractice.validation

object CreateAccountValidation {

    @JvmStatic
    fun accountValidate(
        accNo: String,
        accHn: String,
        phNo: String,
        em: String,
        addr: String,
        accountTypeValue: String,
        bal: String
    ): ValidationResult{
        val accountRegex = Regex("^\\d+$")
        val nameRegex = Regex("^[A-Za-z ]{2,50}\$")
        val phoneRegex = Regex("^(98|97)\\d{8}\$")
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        val addressRegex = Regex("^[A-Za-z0-9 ,./-]{5,100}\$")
        val balanceRegex = Regex("^\\d+(\\.\\d{1,2})?\$")
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

        if (accHn.isBlank()) {
            return ValidationResult(
                false,
                "accountHolderName",
                "Account holder name is required."
            )
        }else if(!accHn.matches(nameRegex)){
            return ValidationResult(
                false,
                "accountHolderName",
                "Invalid account holder name."
            )
        }

        if (phNo.isBlank()){
            return ValidationResult(
                false,
                "phoneNumber",
                "Phone number is required."
            )
        }else if (!phNo.matches(phoneRegex)){
            return ValidationResult(
                false,
                "phoneNumber",
                "Invalid phone number."
            )
        }

        if (em.isBlank()){
            return ValidationResult(
                false,
                "email",
                "Email is required."
            )
        }else if (!em.matches(emailRegex)){
            return ValidationResult(
                false,
                "email",
                "Invalid email."
            )
        }

        if (addr.isBlank()){
            return ValidationResult(
                false,
                "address",
                "Address is required."
            )
        }else if (!addr.matches(addressRegex)){
            return ValidationResult(
                false,
                "address",
                "Invalid address."
            )
        }

        if (bal.isBlank()){
            return ValidationResult(
                false,
                "balance",
                "Opening balance is required."
            )
        }else if (!bal.matches(balanceRegex)){
            return ValidationResult(
                false,
                "balance",
                "Invalid amount."
            )
        }
        return ValidationResult(
            true,
            "",
            ""
        )
    }
}
package com.example.finalproject

// object keyword makes it so all the functions are
// static functions
object RegistrationUtil {
    // use this in the test class for the is username taken test
    // make another similar list for some taken emails
    var existingUsers = listOf("cosmicF", "cosmicY", "bob", "alice")
//    you can use listOf<type>() instead of making the list & adding individually
//    List<String> blah = new ArrayList<String>();
//    blah.add("hi")
//    blah.add("hello")
    var existingEmails = listOf("cosmicF@gmail.com", "cosmicY@gmail.com", "bob@gmail.com", "alice@gmail.com")

    // isn't empty
    // already taken
    // minimum number of characters is 3
    fun validateUsername(username: String) : Boolean {
        var original = true
        for(i in existingUsers.indices) {
            if(username.equals(existingUsers[i]))
                original = false
        }
        if(username.length >= 3 && original)
            return true
        return false
    }

    // make sure meets security requirements (deprecated ones that are still used everywhere)
    // min length 8 chars
    // at least one digit
    // at least one capital letter
    // both passwords match
    // not empty
    fun validatePassword(password : String, confirmPassword: String) : Boolean {
        var hasNumber = false
        var hasCapital = false
        var charsInPassword = password.toCharArray()
        for(element in charsInPassword) {
            if(element.isDigit())
                hasNumber = true
            if(element.isUpperCase())
                hasCapital = true
        }
        if(password.length >= 8 && hasNumber && hasCapital && password.equals(confirmPassword))
            return true
        return false
    }

    // isn't empty
    fun validateName(name: String) : Boolean {
        if(name.isNotEmpty())
            return true
        return false
    }

    // isn't empty
    // make sure the email isn't used
    // make sure it's in the proper email format user@domain.tld
    fun validateEmail(email: String) : Boolean {
        var original = true
        var hasAt = false
        var hasPeriod = false
        var charsInEmail = email.toCharArray()
        for(i in existingEmails.indices) {
            if(email.equals(existingEmails[i]))
                original = false
        }
        for(i in email.indices) {
            if(email.subSequence(i, i+1).equals("@"))
                hasAt = true
            if(email.subSequence(i, i+1).equals("."))
                hasPeriod = true
        }
        if(email.isNotEmpty() && original && hasAt && hasPeriod)
            return true
        return false
    }
}
package com.bootcamp.stations.user.util


import android.text.TextUtils
import android.util.Patterns
import com.bootcamp.stations.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


enum class InputTypes { EMAIL, PASSWORD, REPASSWORD }

//region  The Emptiness com.bootcamp.stations.user.util.Validation of the Register
fun TextInputLayout.Validation(textIput: TextInputEditText, validationTypes: InputTypes):Boolean {
    var isValid = true
    val text = textIput.text.toString()
    when(validationTypes){
        InputTypes.EMAIL -> {
            if (TextUtils.isEmpty(text) || Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                this.error = this.context.getString(R.string.Email)
                isValid = false
            }
        }
        InputTypes.PASSWORD -> {
            if (TextUtils.isEmpty(text)) {
                this.error = this.context.getString(R.string.invalid_password)
                isValid = false
            }
        }
            InputTypes.REPASSWORD -> {
                if (TextUtils.isEmpty(text)){
                    this.error = this.context.getString(R.string.invalid_password)
                    isValid = false
                }
        }
    }
    return isValid
}

//?endregion
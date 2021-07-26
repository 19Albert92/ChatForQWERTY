package com.QwertyNetwork.chat.validation;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class InputValidation {
    private Context context;

    public InputValidation(Context context) {
        this.context = context;
    }

    /* метод проверки заполнения InputEditText */
    public boolean isInputEditTextFilled(EditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyBOardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /* метод проверки наличия у InputEditText действующего адреса электронной почты. */
    public boolean isInputEditTextEmail(EditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyBOardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isInputEditTextMatches(EditText textInputEditText1, EditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyBOardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /* метод Скрыть клавиатуру */
    public void hideKeyBOardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
}

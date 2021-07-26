package com.QwertyNetwork.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.QwertyNetwork.chat.db.DBHelper;
import com.QwertyNetwork.chat.models.User;
import com.QwertyNetwork.chat.validation.InputValidation;
import com.google.android.material.textfield.TextInputLayout;

public class FormToRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textInputEditTextPhone, textInputEditTextName, textInputEditTextEmail, textInputEditTextNick, textInputEditTextPassword, textInputEditTextConfirmPassword;
    private Button buttonRegistrationGoHome;
    private TextView textError;
    private TextInputLayout textInputLayoutPhone, textInputLayoutName, textInputLayoutEmail, textInputLayoutNick, textInputLayoutPassword, textInputLayoutConfirmPassword;

    private InputValidation inputValidation = new InputValidation(this);
    private DBHelper dbHelper = new DBHelper(this);
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_activity_registration);
        initial();
    }

    private void initial() {
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutPhone = (TextInputLayout) findViewById(R.id.textInputLayoutPhone);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutNick = (TextInputLayout) findViewById(R.id.textInputLayoutNick);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);


        textInputEditTextPhone = (EditText) findViewById(R.id.textInputEditTextPhone);
        textInputEditTextName = (EditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (EditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextNick = (EditText) findViewById(R.id.textInputEditTextNick);
        textInputEditTextPassword = (EditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (EditText) findViewById(R.id.textInputEditTextConfirmPassword);

        buttonRegistrationGoHome = (Button) findViewById(R.id.appCompatButtonRegister);
        buttonRegistrationGoHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        postDataToSQlite();
        Intent intent = new Intent();
        intent.putExtra("nick", textInputEditTextNick.getText().toString());
        intent.putExtra("mail", textInputEditTextEmail.getText().toString());
        intent.putExtra("password", textInputEditTextPassword.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void postDataToSQlite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword, textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        if (!dbHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {
            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPass(textInputEditTextPassword.getText().toString().trim());
            dbHelper.addUser(user);
            Log.d("myLog", "успешно сохпрнение записи");
        } else {
            Log.d("myLog", "запись уже есть такая");
        }
    }

    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextNick.setText(null);
        textInputEditTextPhone.setText(null);
    }
}
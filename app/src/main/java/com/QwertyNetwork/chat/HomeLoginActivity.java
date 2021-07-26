package com.QwertyNetwork.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.QwertyNetwork.chat.db.DBHelper;
import com.QwertyNetwork.chat.validation.InputValidation;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class HomeLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonToLogin;
    private TextView textForButtonHelp, textForButtonRegistration;
    private ImageView imageIconLogin;
    private EditText textInputEditTextEmail, textInputEditTextPassword;
    private TextInputLayout textInputLayoutEmail, textInputLayoutPassword;

    public static final int REQUURE_CODE = 1;

    private String password;
    private String mail;
    private String nick;

    private InputValidation inputValidation = new InputValidation(this);
    private DBHelper dbHelper = new DBHelper(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);
        initial();
    }

    private void initial() {

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        buttonToLogin = (Button) findViewById(R.id.appCompatButtonLogin);
        buttonToLogin.setOnClickListener(this);

        textForButtonHelp = (TextView) findViewById(R.id.login_help_parol);
        textForButtonHelp.setOnClickListener(this);

        textForButtonRegistration = (TextView) findViewById(R.id.textViewLinkRegister);
        textForButtonRegistration.setOnClickListener(this);

        textInputEditTextEmail = (EditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (EditText) findViewById(R.id.textInputEditTextPassword);
        imageIconLogin = (ImageView) findViewById(R.id.image_login_layout);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.appCompatButtonLogin: {
                verifyFromSQLite();
                break;
            }
            case R.id.login_help_parol: {
                break;
            }
            case R.id.textViewLinkRegister: {
                intent = new Intent(this, FormToRegistrationActivity.class);
                startActivityForResult(intent, REQUURE_CODE);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUURE_CODE) {
                nick = data.getStringExtra("nick");
                mail = data.getStringExtra("mail");
                password = data.getStringExtra("password");
                textInputEditTextEmail.setText(mail);
                textInputEditTextPassword.setText(password);
                Log.d("myLog", " " + nick + " " + mail + " " + password );
            }
        }
    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (dbHelper.checkUser(textInputEditTextEmail.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("nick", nick);
            emptyInputEditText();
            startActivity(intent);
        } else {
            Log.d("myLog", "Не получилось!");
        }
    }

    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }

    private void images(ImageView image) {
        Picasso.get().load("https://ru.qwertynetworks.com/images/qwertynetworkswhitelogo.png").into(image);
    }
}
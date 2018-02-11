package com.msaranu.tripney.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.msaranu.tripney.R;
import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {
    Button btnSignup;
    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etUserId;
    EditText etPassword;
    TextView tvSignUpWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvSignUpWelcome = (TextView) findViewById(R.id.tvSignUpWelcome);


        btnSignup.setOnClickListener(v -> {
        //    ParseUser p = ParseUser.getCurrentUser();
          //  if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                setupTripneyLogin(etUserId.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString(),
                        etFirstName.getText().toString(), etLastName.getText().toString());
           // }
        });

    }

    private void setupTripneyLogin(String userId, String password, String email, String firstName,
                                  String lastName) {
        ParseUser user = new ParseUser();
        user.setUsername(userId);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);

        user.signUpInBackground(e -> {
            if (e == null) {
                homePageIntent();
            } else {
                Toast.makeText(SignupActivity.this,"Signup failed"+e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public void homePageIntent() {
        Intent i = new Intent(SignupActivity.this, MainTripActivity.class);
        startActivity(i);
    }
}

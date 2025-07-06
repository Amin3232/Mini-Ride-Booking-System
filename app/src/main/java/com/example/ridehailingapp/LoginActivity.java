package com.example.ridehailingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehailingapp.data.DataManager;
import com.example.ridehailingapp.models.User;

public class LoginActivity extends AppCompatActivity {
    EditText usernameInput,passwordInput;
    Button loginBtn;
    TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerLink = findViewById(R.id.registerLink);

        loginBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,"Please enter username and password",Toast.LENGTH_SHORT).show();
                return;
            }

            DataManager dataManager = DataManager.getInstance();
            User user = dataManager.authenticateUser(username,password);

            if (user != null) {
                // auth success
                Toast.makeText(this, "Welcome, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this,"Invalid credentials",Toast.LENGTH_SHORT).show();
            }
        });

        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(this,RegisterActivity.class));
        });
    }
}
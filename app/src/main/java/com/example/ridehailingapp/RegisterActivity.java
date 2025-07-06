package com.example.ridehailingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ridehailingapp.data.DataManager;
import com.example.ridehailingapp.models.User;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameInput,passwordInput,nameInput,emailInput;
    RadioGroup userTypeGroup;
    RadioButton passengerRadio,driverRadio;
    Button registerBtn,backToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        userTypeGroup = findViewById(R.id.userTypeGroup);
        passengerRadio = findViewById(R.id.passengerRadio);
        driverRadio = findViewById(R.id.driverRadio);
        registerBtn = findViewById(R.id.registerBtn);
        backToLoginBtn = findViewById(R.id.backToLoginBtn);

        passengerRadio.setChecked(true);

        registerBtn.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            
            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 4) {
                Toast.makeText(this,"Password must be at least 4 characters",Toast.LENGTH_SHORT).show();
                return;
            }

            String userType = passengerRadio.isChecked() ? "passenger" : "driver";

            // reg user
            DataManager dataManager = DataManager.getInstance();
            User newUser = dataManager.registerUser(username,password,name,email,userType);

            if (newUser != null) {
                Toast.makeText(this,"Registration successful! Please login.",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this,"Username already exists",Toast.LENGTH_SHORT).show();
            }
        });

        backToLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });
    }
} 
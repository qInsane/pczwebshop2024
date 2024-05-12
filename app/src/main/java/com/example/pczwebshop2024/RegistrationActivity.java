package com.example.pczwebshop2024;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private static final int SECRET_KEY = 99;

    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        EditText confirmPasswordEditText = findViewById(R.id.confirm_password);

        preferences= getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        usernameEditText.setText(username);
        passwordEditText.setText(password);
        confirmPasswordEditText.setText(password);

        // Load animations
        Animation fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        // Apply animations to views
        usernameEditText.startAnimation(fadeInAnim);
        passwordEditText.startAnimation(fadeInAnim);
        confirmPasswordEditText.startAnimation(fadeInAnim);

        Button registerButton = findViewById(R.id.register);
        Button cancelButton = findViewById(R.id.cancel);
        registerButton.startAnimation(slideUpAnim);
        cancelButton.startAnimation(slideUpAnim);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = findViewById(R.id.email);

                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                Log.i("RegistrationForm", "Username: " + username);
                Log.i("RegistrationForm", "Email: " + email);

                if (password.equals(confirmPassword)) {
                    Log.i("RegistrationForm", "Passwords match");
                } else {
                    Log.e("RegistrationForm", "Passwords do not match");
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Log.i("regist", "User created successfully");
                                    startShopping();
                                } else {
                                    Log.i("regist", "User creation failed");
                                    if (task.getException() != null) {
                                        Log.e("regist", "Error: " + task.getException().getMessage());
                                    }
                                }
                            }
                        });
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }
    }

    private void startShopping(){
        Intent intent = new Intent(RegistrationActivity.this, ShopMainPageActivity.class);
        startActivity(intent);
    }
}

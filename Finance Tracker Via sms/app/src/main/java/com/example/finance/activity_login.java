package com.example.finance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class activity_login extends AppCompatActivity {

    Button signin;   //variable for button
    TextView register, forgot;//variable for register and forgot text
    Intent intent;      //global intent declaration
    FirebaseAuth auth;

    ImageView fingerprint;//Fingerprint image initialization
    EditText email, password;
    Matcher matcher;//for password matching
    ProgressBar loginprogress;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emaillogin);
        password = findViewById(R.id.passwordlogin);
        signin = findViewById(R.id.btnSignIn);
        auth = FirebaseAuth.getInstance();

        loginprogress = findViewById(R.id.loginprogress);
        fingerprint = findViewById(R.id.fingerprint);
        register = findViewById(R.id.tvSignIn);
        forgot = findViewById(R.id.tvForgotPass);


        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        if (isLogin) {
            fingerprint.setVisibility(View.VISIBLE);
        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();
                loginprogress.setVisibility(View.GONE);


            }

            public void validate() {
                String mail = email.getText().toString();
                String pssd = password.getText().toString();


                if (mail.isEmpty()) {

                    // set error if field is empty
                    loginprogress.setVisibility(View.GONE);
                    email.setError("Field Cannot be Empty");
                    email.requestFocus();


                } else if (!validateEmail(mail)) {

                    // set error if email not valid
                    email.setError("Email is not valid");
                    email.requestFocus();
                    loginprogress.setVisibility(View.GONE);
                } else if (pssd.isEmpty()) {

                    // set error if fields are empty
                    password.setError("Field Cannot be Empty");
                    password.requestFocus();
                    loginprogress.setVisibility(View.GONE);
                } else if (pssd.length() != 6) {
                    password.setError("password must be equal to 6 digits");
                    loginprogress.setVisibility(View.GONE);

                } else if (!validatePassword(pssd)) {

                    // set error if password week
                    password.setError("Password is week");
                    password.requestFocus();
                    loginprogress.setVisibility(View.GONE);
                } else {

                    performauth(mail, pssd);

                }

            }

            private boolean validateEmail(String mail) {
                final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                matcher = pattern.matcher(mail);

                return matcher.matches();

            }

            private boolean validatePassword(String pssd) {
                // This is how password pattern works

                //  start of string -->  ^
                //  password must contain at least one digit             --> 0-9 -->  (?=.*[0-9])
                //  password must contain at least one lover case letter --> a-z -->  (?=.*[a-z])
                //  password must contain at least one upper case letter --> A-Z -->  (?=.*[A-Z])
                //  no whitespace allowed in password                            -->  (?=\S+$)
                //  password must contain at least 6 characters --> .{6}
                // end of string  -- > $

                final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6}$";
                Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(pssd);

                return matcher.matches();


            }


        });


        //fingerprint code start


        // Initialising msgtext and loginbutton
        TextView msgtex = findViewById(R.id.msgtext);


        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("You can use the fingerprint sensor to login");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("This device does not have a fingerprint sensor");
                fingerprint.setVisibility(View.GONE);

                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("The Fingerprint sensor currently not availablr");
                fingerprint.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("Your device doesn't have fingerprint saved,please check your security settings");
                fingerprint.setVisibility(View.GONE);
                break;
        }

        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(activity_login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                String mail = sharedPreferences.getString("email", "");
                String pssd = sharedPreferences.getString("password", "");
                loginprogress.setVisibility(View.VISIBLE);
                performauth(mail, pssd);

            }


            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Finance Tracker")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });


        //fingerprint code end


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(activity_login.this,activity_liquidswipe.class);
                startActivity(intent);

            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(activity_login.this, activity_pwreset.class);
                startActivity(intent);

            }
        });

    }


    private void performauth(String mail, String pssd) {
        auth.signInWithEmailAndPassword(mail, pssd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {   //this code is for check email and password in databse
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    try {


                        if (!auth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(activity_login.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                            loginprogress.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(activity_login.this, "sign out/in error", Toast.LENGTH_SHORT).show();
                    }

                    //used shard prefernce for taking data and give back through Fingerprint
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("email", mail);
                    editor.putString("password", pssd);
                    editor.putBoolean("isLogin", true);
                    editor.apply();

                    if (auth.getCurrentUser().isEmailVerified()) {

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { //this code is for progressbar
                            @Override
                            public void run() {

                                loginprogress.setVisibility(View.GONE);
                                Toast.makeText(activity_login.this, "You are successfully logged In ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(activity_login.this, MainActivity.class));
                                finish();


                            }

                        }, 0);


                    }
                } else {
                    loginprogress.setVisibility(View.GONE);
                    Toast.makeText(activity_login.this, "Email or password is Incorrect", Toast.LENGTH_SHORT).show();
                    email.setText(""); password.setText(""); email.requestFocus();
                }
            }

        });
    }
}



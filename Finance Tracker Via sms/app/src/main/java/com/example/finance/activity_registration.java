package com.example.finance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_registration extends AppCompatActivity {

    TextView alrsignup, registersucess;
    EditText username, email, password, confpassword;
    Intent intent;
    Button signup;
    FirebaseAuth auth;
    String usrname, mail, pssd, confpssd;
    FirebaseFirestore db;
    ProgressBar progressbar;
    Matcher matcher;
    FirebaseUser user;
    DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();//Reference of firebase
        user = auth.getCurrentUser();

        String catchfname = getIntent().getStringExtra("firstname");
        String catchlname = getIntent().getStringExtra("lastname");
        String catchmobile = getIntent().getStringExtra("mobile");

        confpassword = findViewById(R.id.confpassword);
        alrsignup = findViewById(R.id.alrsignin);
        signup = findViewById(R.id.btnSignUp);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registersucess = findViewById(R.id.registersucess);
        db = FirebaseFirestore.getInstance();//Firebase reference
        progressbar = findViewById(R.id.registerprogress);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                validate();


            }

            private void validate() {

                usrname = username.getText().toString();
                mail = email.getText().toString();
                pssd = password.getText().toString();
                confpssd = confpassword.getText().toString();

                if (usrname.isEmpty()) {
                    username.setError("Required");
                    username.requestFocus();
                } else if (mail.isEmpty()) {
                    email.setError("Required");
                    email.requestFocus();

                } else if (!validateEmail(mail)) {
                    email.setError("Email is not Valid");
                    email.requestFocus();
                } else if (pssd.isEmpty()) {

                    // set error if fields are empty
                    password.setError("Field Cannot be Empty");
                    password.requestFocus();
                } else if (pssd.length() != 6) {
                    password.setError("password must be atleast 6 digits");

                } else if (!validatePassword(pssd)) {

                    // set error if password week
                    password.setError("Password is week");
                    password.requestFocus();
                } else if (!confpssd.equals(pssd)) {
                    confpassword.setError("Password and confirm password are not equal");
                    confpassword.requestFocus();


                } else {
                    createuser();
                }

            }

            private boolean validateEmail(String mail) {
                final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
                Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                matcher = pattern.matcher(mail);

                return matcher.matches();

            }

            public boolean validatePassword(String pass) {

                // This is how password pattern works

                //  start of string -->  ^
                //  password must contain at least one digit             --> 0-9 -->  (?=.*[0-9])
                //  password must contain at least one lover case letter --> a-z -->  (?=.*[a-z])
                //  password must contain at least one upper case letter --> A-Z -->  (?=.*[A-Z])
                //  no whitespace allowed in password                            -->  (?=\S+$)
                //  password must equal to 6 characters --> .{6}
                // end of string  -- > $

                final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6}$";
                Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(pass);

                return matcher.matches();

            }

            private void createuser() {

                auth.createUserWithEmailAndPassword(mail, pssd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    registersucess.setText("User Created Successfully Back to Login");
                                    registersucess.setTextColor(getResources().getColor(R.color.green));
                                    username.setText("");
                                    email.setText("");
                                    password.setText("");
                                    confpassword.setText("");
                                             user = auth.getCurrentUser();
                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(activity_registration.this, "Verification Email hase been sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(activity_registration.this, "Fail to Send Verification Email", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    ////below code for progressbar
                                    progressbar.setVisibility(View.VISIBLE);
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            progressbar.setVisibility(View.INVISIBLE);


                                        }
                                    }, 4000);

                                    //progressbar code ends
                                    uploaddata();  // to upload user data
                                } else {

                                }
                            }

                            private void uploaddata() {

                                mdatabase.child("users").child(user.getUid()).child("first").setValue(catchfname);
                                mdatabase.child("users").child(user.getUid()).child("last").setValue(catchlname);
                                mdatabase.child("users").child(user.getUid()).child("mobile").setValue(catchmobile);
                                mdatabase.child("users").child(user.getUid()).child("username").setValue(usrname);
                                mdatabase.child("credentials").child(user.getUid()).child("email").setValue(mail);
                                mdatabase.child("users").child(user.getUid()).child("email").setValue(mail);
                                mdatabase.child("credentials").child(user.getUid()).child("password").setValue(pssd);
                                mdatabase.child("users").child(user.getUid()).child("userid").setValue(user.getUid());
                                mdatabase.child("credentials").child(user.getUid()).child("userid").setValue(user.getUid());

                                /*
                                Map<String, Object> users = new HashMap<>();
                                users.put("first", catchfname);
                                users.put("last", catchlname);
                                users.put("mobile", catchmobile);
                                users.put("username", usrname);
                                users.put("email", mail);
                                users.put("password", pssd);
                                users.put("uid",user.getUid());


                                //Write above data in user collection in database
                                DocumentReference dr = db.collection("users")
                                        .document(user.getUid());

                                dr.set(users);*/

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        registersucess.setText("EMail already Registered");
                        registersucess.setTextColor(getResources().getColor(R.color.Red));
                        email.setText(""); email.requestFocus();

                    }
                });

            }
        });

        alrsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(activity_registration.this, activity_login.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
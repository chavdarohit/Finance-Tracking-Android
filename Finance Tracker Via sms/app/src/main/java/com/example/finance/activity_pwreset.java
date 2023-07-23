package com.example.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_pwreset extends AppCompatActivity {

    TextView goback;
    EditText emailreset;
    Button btnreset;
    Matcher matcher;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwreset);
        emailreset=findViewById(R.id.emailreset);
        btnreset=findViewById(R.id.btnreset);



        goback=findViewById(R.id.tvGoBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity_pwreset.this,activity_login.class);
                startActivity(intent);
                finish();

            }
        });



        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=emailreset.getText().toString();
                if(email.isEmpty())
                {
                    emailreset.requestFocus();
                    emailreset.setError("Required");
                }
                else if(!validateemail(email))
                {
                    emailreset.setError("Enter Valid Email");
                    emailreset.requestFocus();
                }
                else
                {
                    auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(activity_pwreset.this, "Reset Link sended to Mail", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_pwreset.this, "Error ! Link is not sent"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }




            }

            private boolean validateemail(String email) {
                    final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
                    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                    matcher = pattern.matcher(email);

                    return matcher.matches();



            }
        });


    }
}
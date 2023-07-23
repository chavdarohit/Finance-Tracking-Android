package com.example.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class activity_firstregister extends AppCompatActivity {

    private Button next;            // variable for the button initializing
    private TextView alrsignin;     //variable for already a user text
    private EditText fname, lname, mobile;   //for the layout
    private String firstname, lastname, mobileno;     //variable for validation
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstregister);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        mobile = findViewById(R.id.mobile);
        auth = FirebaseAuth.getInstance();// reference of firebase

        next = findViewById(R.id.next);   // Click Next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();

            }
        });
        alrsignin = findViewById(R.id.alrsignin);
        alrsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_firstregister.this, activity_login.class);
                startActivity(intent);
            }
        });


    }

    private void validate() {
        firstname = fname.getText().toString();
        lastname = lname.getText().toString();
        mobileno = mobile.getText().toString();

        if (firstname.isEmpty()) {
            fname.setError("Required");
            fname.requestFocus();
        } else if (lastname.isEmpty()) {
            lname.setError("Required");
            lname.requestFocus();
        } else if (mobileno.isEmpty()) {
            mobile.setError("Required");
            mobile.requestFocus();
        }else if(mobileno.length() != 10)
        {
            mobile.setError("Max & Min 10 digits required");
            mobile.requestFocus();
        }
        else {


            Intent intent = new Intent(activity_firstregister.this, activity_registration.class);
            intent.putExtra("firstname", firstname);
            intent.putExtra("lastname", lastname);
            intent.putExtra("mobile", mobileno);
            startActivity(intent);
            finish();
        }
    }


}
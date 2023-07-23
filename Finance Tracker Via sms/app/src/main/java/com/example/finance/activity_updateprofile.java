package com.example.finance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class activity_updateprofile extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    EditText profilename, profilemail, profilemobile;
    Button verify;
    TextView verifylater;
    ImageView back;
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        profilemail = findViewById(R.id.profile_email);
        profilemobile = findViewById(R.id.profile_mobile);
        profilename = findViewById(R.id.profile_name);
        verifylater = findViewById(R.id.verifylater);
        verify = findViewById(R.id.verify);
        back = findViewById(R.id.back);

        //--------------------Getting data from datbase and putting into update profile edittexts--------------
        mdatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                    Map map = (Map) snapshot.getValue();
                    profilemail.setText(map.get("email").toString());
                    profilemobile.setText(map.get("mobile").toString());
                    profilename.setText(map.get("username").toString());

                } catch (Exception e) {
                    Toast.makeText(activity_updateprofile.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("e.getmessage", "onDataChange: " + e.getMessage());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity_updateprofile.this, "No data exist", Toast.LENGTH_SHORT).show();

            }
        });


        //----------------------------------commented firebase firestore code------------------
       /* db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            profilemail.setText(documentSnapshot.getString("email"));
                            profilename.setText(documentSnapshot.getString("username"));
                            profilemobile.setText(documentSnapshot.getString("mobile"));
                        }

                    }
                });
        *///---------------------Firestore code ends---------------------------

        verifylater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_updateprofile.this, MainActivity.class));
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_updateprofile.this, MainActivity.class));
                finish();
            }
        });
//--------------------------------Verify and update profile starts--------------------------
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profilename.getText().toString().isEmpty() || profilemail.getText().toString().isEmpty() || profilemobile.getText().toString().isEmpty() || profilemobile.getText().toString().length() != 10) {
                    Toast.makeText(activity_updateprofile.this, "One or Many fields are empty or Wrong data filled", Toast.LENGTH_SHORT).show();
                } else {


                    user.updateEmail(profilemail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            mdatabase.child("users").child(user.getUid()).child("email").setValue(profilemail.getText().toString());
                            mdatabase.child("users").child(user.getUid()).child("mobile").setValue(profilemobile.getText().toString());
                            mdatabase.child("users").child(user.getUid()).child("username").setValue(profilename.getText().toString());
                            Toast.makeText(activity_updateprofile.this, "Email changed successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(activity_updateprofile.this,MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_updateprofile.this, "Email already existed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

//--------------------------------Verify and update profile ends--------------------------
    }
}
package com.example.finance;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.finance.Fragments.GraphFragment;
import com.example.finance.Fragments.HomeFragment;
import com.example.finance.Fragments.RemFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    TextView name, phoneno, mail;
    DatabaseReference mdatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        navigationView = findViewById(R.id.nav_view);


        View view = navigationView.getHeaderView(0);  //Necessary for navigation views
        name = view.findViewById(R.id.nav_name);
        phoneno = view.findViewById(R.id.nav_phoneno);
        mail = view.findViewById(R.id.nav_mail);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        //-----------------------sms Initlizations--------------
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        //--------------------------\\--------------------------



        //----------Code for navigation drawer selections-------------------------

        //---------------------Code for getting name from Database and give to navigation profile---------------


        mdatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                    Map map = (Map) snapshot.getValue();
                    mail.setText(map.get("email").toString());
                    phoneno.setText(map.get("mobile").toString());
                    name.setText(map.get("username").toString());

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("e.getmessage", "onDataChange: " + e.getMessage());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "No data exist", Toast.LENGTH_SHORT).show();

            }
        });

        /*db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            mail.setText(documentSnapshot.getString("email"));
                            name.setText(documentSnapshot.getString("username"));
                            phoneno.setText(documentSnapshot.getString("mobile"));
                        }

                    }
                });
          */

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment fragment = null;
                switch (id) {
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this, activity_updateprofile.class));

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.logout:
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this, activity_login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.support:
                        Intent support = new Intent(Intent.ACTION_SEND);
                        support.putExtra(Intent.EXTRA_EMAIL, new String[]

                                {
                                        "financetrackerviasms@gmail.com"
                                });
                        support.putExtra(Intent.EXTRA_SUBJECT, "Get support from Finance Tracker");
                        support.putExtra(Intent.EXTRA_TEXT, "I am user of Finance Tracker '" + name.getText() + "' I need support.");

                        //need this to prompts email client only
                        support.setType("message/rfc822");

                        startActivity(Intent.createChooser(support, "Choose an Email client :"));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.rateus:
                        Intent intent1 = new Intent(MainActivity.this, activity_rateus.class);

                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.aboutft:
                        startActivity(new Intent(MainActivity.this, activity_about.class));

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.share:
                        Intent myIntent = new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String body = "I use Finance Tracker app fro track expenses & bill reminders. Download it free! <Link>";
                        String sub = "Finance Tracker";
                        myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                        myIntent.putExtra(Intent.EXTRA_TEXT, body);

                        startActivity(Intent.createChooser(myIntent, "Share Using"));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
//-------------------------Navigation selection ends-----------------------

//-------------------------Code for Bottom Navigation----------------------
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);


    }

    HomeFragment firstFragment = new HomeFragment();
    GraphFragment secondFragment = new GraphFragment();
    RemFragment thirdFragment = new RemFragment();


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, firstFragment).commit();
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.teal_200));
                return true;

            case R.id.graph:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, secondFragment).commit();
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.purple_200));

                return true;

            case R.id.reminder:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, thirdFragment).commit();
                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.white));

                return true;

        }
        return false;

    }

    //-----------------------Bottom navigation Ends----------------------------------------------
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .show();
        }
    }


}
package com.example.finance;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class activity_rateus extends AppCompatActivity {

    Toast mytoast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus);

        RatingBar myratingbar;
        AppCompatButton mybutton;
        myratingbar = findViewById(R.id.ratingbar);
        mybutton = findViewById(R.id.submitrating);

        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mytoast != null) {
                    mytoast.cancel();
                }
                mytoast = Toast.makeText(getApplicationContext(), "you Gave us " + myratingbar.getRating() + " rating.", Toast.LENGTH_SHORT);
                mytoast.show();
                myratingbar.setRating(myratingbar.getRating());

            }
        });


    }
}
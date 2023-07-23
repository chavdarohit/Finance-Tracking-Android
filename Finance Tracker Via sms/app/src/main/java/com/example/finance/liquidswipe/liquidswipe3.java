package com.example.finance.liquidswipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.finance.R;
import com.example.finance.activity_firstregister;


public class liquidswipe3 extends Fragment {

    TextView skip3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_liquidswipe3, container, false);

        skip3=view.findViewById(R.id.skip3);

        skip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), activity_firstregister.class));
                getActivity().finish();
            }
        });
        return view;

    }
}
package com.example.finance.Fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.Adapter.AdapterBank;
import com.example.finance.Adapter.AdapterTran;
import com.example.finance.Models.ModelBank;
import com.example.finance.Models.ModelTran;
import com.example.finance.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    AdapterTran adapterTran;
    TextView totalexpense, totalincome;
    String other, date, accno, type, ref, mbalancestr, bank, amt;
    double texpense;
    double tincome;
    String datestr, acstr, refer, mbal, uri;
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    AdapterBank adapterBank;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView2 = view.findViewById(R.id.recyclerview2);

        totalexpense = view.findViewById(R.id.totalexpense);
        totalincome = view.findViewById(R.id.totalincome);

        //--------------------------------------SMS CODE STARTS-----------------------------------------

        Cursor cursor = getContext().getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);


        int count = 1;


        cursor.moveToFirst();
        int max = cursor.getCount();
        mdatabase.child("transaction").child(user.getUid()).removeValue();//Everytime activity starts it will remove all transactions
        texpense = 0;
        tincome = 0;

    try {

        while (count != max - 1) {
            if (cursor.getString(2).contains("UNIONB")) {


                String messageBody = cursor.getString(12).toString();

                String subString[] = messageBody.split("Rs.");
                other = subString[1];

                String finalString[] = subString[1].split("\\ ");
                amt = (finalString[0]);

                String datestring[] = subString[1].split("on ");
                datestr = datestring[1];

                String datefinal[] = datestring[1].split("\\ ");
                date = datefinal[0];

                String acstring[] = messageBody.split("A/c *");
                acstr = acstring[1];

                String finalac[] = acstring[1].split("\\ ");
                accno = finalac[0];

                String typestring[] = finalac[1].split("\\ ");
                type = typestring[0];

                String refstr[] = acstring[1].split("by ");
                refer = refstr[1];

                String reference[] = refstr[1].split("\\ ");
                ref = reference[0];

                String mbalstring[] = refstr[1].split("Rs:");
                mbalancestr = mbalstring[1];

                String mbalance[] = mbalstring[1].split(" ");
                mbal = mbalance[0];

                String bankname[] = mbalstring[1].split("-");
                bank = bankname[1];


                //---------------------adding msg to database
                if (type.equals("Debited")) {
                    texpense = texpense + Float.parseFloat(amt);
                } else {
                    tincome = tincome + Float.parseFloat(amt);
                }


                totalexpense.setText((new DecimalFormat(".##").format(texpense)));
                totalincome.setText((new DecimalFormat(".##").format(tincome)));


                Map<String, Object> tran = new HashMap<>();
                tran.put("date", date);
                tran.put("type", type);
                tran.put("amt", amt);
                tran.put("accno", accno);
                tran.put("ref", ref);
                tran.put("mbal", mbal);
                tran.put("bname", bank);
                tran.put("texpense", texpense);
                tran.put("tincome", tincome);


                mdatabase.child("transaction").child(user.getUid()).push().setValue(tran);
            }
            cursor.moveToNext();
            count = count + 1;
        }
    }catch(Exception e)
    {
        Toast.makeText(getContext(), "Messages Invalid", Toast.LENGTH_SHORT).show();
    }
        Double inc= Double.parseDouble(totalincome.getText().toString());
        Double exp= Double.parseDouble(totalexpense.getText().toString());

        //--------------------------------------SMS CODE ENDS--------------------------------------------


        //--------------------recycler 1---------------------

        FirebaseRecyclerOptions<ModelTran> options = new FirebaseRecyclerOptions.Builder<ModelTran>().setQuery(FirebaseDatabase.getInstance().getReference().child("transaction").child(user.getUid()), ModelTran.class).build();
        adapterTran = new AdapterTran(options);
        recyclerView.setAdapter(adapterTran);

        //--------------------recycler 2----------------------

        FirebaseRecyclerOptions<ModelBank> optionss = new FirebaseRecyclerOptions.Builder<ModelBank>().setQuery(FirebaseDatabase.getInstance().getReference().child("transaction").child(user.getUid()).limitToFirst(1), ModelBank.class).build();
        adapterBank = new AdapterBank(optionss);
        recyclerView2.setAdapter(adapterBank);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterTran.startListening();
        adapterBank.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterTran.stopListening();
        adapterBank.stopListening();
    }
}
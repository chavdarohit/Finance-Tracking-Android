package com.example.finance.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finance.Adapter.AdapterTask;
import com.example.finance.Models.ModelTask;
import com.example.finance.R;
import com.example.finance.activity_addtask;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RemFragment extends Fragment {

    RecyclerView taskrecycler;
    TextView addTask;
    Intent intent;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    AdapterTask adapterTask;
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rem, container, false);


        Bundle bundle = getArguments();


        addTask = view.findViewById(R.id.addTask);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), activity_addtask.class);
                startActivity(intent);
            }
        });


        taskrecycler = view.findViewById(R.id.taskRecycler);

        //modelTaskList.add(new ModelTask("Fri", "8", "Dec", "Party", "Birthday", "Penidng"));


        //-----------------getting tasks from database--------------------


            FirebaseRecyclerOptions<ModelTask> options = new FirebaseRecyclerOptions.Builder<ModelTask>().setQuery(FirebaseDatabase.getInstance().getReference().child("reminders").child(user.getUid()), ModelTask.class).build();
            adapterTask = new AdapterTask(options);
            taskrecycler.setAdapter(adapterTask);


        /*  mdatabase.child("reminders").child(user.getUid()).child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Map map=(Map) snapshot.getValue();
                    title=map.get("title").toString();
                    desc=map.get("desc").toString();
                    date=map.get("date").toString();
                    day=map.get("day").toString();
                    month=map.get("month").toString();
                    List<ModelTask> modelTaskList = new ArrayList<>();
                    modelTaskList.add(new ModelTask(day,date,month,title,desc,"Upcoming"));
                    AdapterTask adaptertask = new AdapterTask(modelTaskList, getContext());
                    taskrecycler.setAdapter(adaptertask);
                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No data exists", Toast.LENGTH_SHORT).show();

            }
        });




            db.collection("reminders").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            title = documentSnapshot.getString("title");
                            date = documentSnapshot.getString("date");
                            desc = documentSnapshot.getString("desc");
                            day = documentSnapshot.getString("day");
                            month = documentSnapshot.getString("month");

                        } else {
                            Toast.makeText(getContext(), "No data exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
*/

        //----------------DATABASE ENDS------------------------


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterTask.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterTask.stopListening();
    }
}
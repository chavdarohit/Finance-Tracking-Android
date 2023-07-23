package com.example.finance;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class activity_addtask extends AppCompatActivity {

    TextView tasktitle, taskdesc, taskdate, tasktime, taskevent, addtask;
    int mYear, mMonth, mDay;
    int mHour, mMinute;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    AlarmBroadcastReceiver activity;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        tasktitle = findViewById(R.id.addTaskTitle);
        taskdesc = findViewById(R.id.addTaskDescription);
        taskdate = findViewById(R.id.taskDate);
        tasktime = findViewById(R.id.taskTime);
        addtask = findViewById(R.id.addTask);
        taskevent = findViewById(R.id.taskEvent);



        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("123",
                    "Finance Tracker",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }


        taskdate.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(activity_addtask.this,
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            taskdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
            return true;
        });

        tasktime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(activity_addtask.this,
                        (view12, hourOfDay, minute) -> {
                            tasktime.setText(hourOfDay + ":" + minute);
                            timePickerDialog.dismiss();
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
            return true;
        });




        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    validate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            private boolean validate() throws ParseException {


                if (tasktitle.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity_addtask.this, "Please enter task title", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (taskdesc.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity_addtask.this, "Please enter a valid description", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (taskdate.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity_addtask.this, "Please enter date", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (tasktime.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity_addtask.this, "Please enter time", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (taskevent.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(activity_addtask.this, "Please enter an event", Toast.LENGTH_SHORT).show();
                    return false;
                } else {

                    //------------Converting date format from numbers to integers--------------------
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
                    String datecheck = taskdate.getText().toString();
                    Date date = null;
                    String outputDateString = null;
                    date = inputDateFormat.parse(datecheck);
                    outputDateString = dateFormat.format(date);


                    //---------------Splitting the date into parts---------------
                    String title, desc, time, event;
                    String[] items1 = outputDateString.split(" ");
                    String day = items1[0];
                    String dd = items1[1];
                    String month = items1[2];
                    String year = items1[3];

                    title = tasktitle.getText().toString();
                    desc = taskdesc.getText().toString();
                    time = tasktime.getText().toString();
                    event = taskevent.getText().toString();

                    String[] itemTime = tasktime.getText().toString().split(":");
                    String hour = itemTime[0];
                    String min = itemTime[1];


                    //--------------------Adding task to our Databases----------------------------
                    //-------------we can also add data to firebase by using "DATAHOLDER User define class"------------


                    Map<String, Object> task = new HashMap<>();
                    task.put("title", title);
                    task.put("desc", desc);
                    task.put("day", day);
                    task.put("month", month);
                    task.put("date", dd);
                    task.put("year", year);
                    task.put("time", time);
                    task.put("event", event);

                    mdatabase.child("reminders").child(user.getUid()).push().setValue(task);
                    Intent alarmIntent = new Intent(activity_addtask.this, AlarmBroadcastReceiver.class);
                    alarmIntent.putExtra("TITLE", title);
                    alarmIntent.putExtra("DESC", desc);

                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(activity_addtask.this,0,alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);



                  Intent intent=new Intent(activity_addtask.this,AlarmBroadcastReceiver.class);
                    PendingIntent pendingIntent=PendingIntent.getBroadcast(activity_addtask.this,0,intent,0);
                    AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);

                    long ten=1000*60;

                    alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+ten,pendingIntent);

                    Toast.makeText(activity_addtask.this, "Task added Successfully", Toast.LENGTH_SHORT).show();
                    tasktitle.setText("");
                    taskdesc.setText("");
                    taskdate.setText("");
                    tasktime.setText("");
                    taskevent.setText("");




                    /* DocumentReference dr = db.collection("reminders").document(user.getUid());
                    Map<String, Object> add = new HashMap<>();
                    add.put("Reminder", FieldValue.arrayUnion());
                    add.put("title", title);
                    add.put("desc", desc);
                    add.put("day", day);
                    add.put("date", dd);
                    add.put("month", month);
                    add.put("year", year);
                    add.put("time", time);
                    add.put("event", event);

                    dr.set(add).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(activity_addtask.this, "Task added Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(activity_addtask.this, MainActivity.class));


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_addtask.this, "Task Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    */
                    //-----------------------Task added to Db-------------------------

                }
                return true;
            }


        });


    }


}
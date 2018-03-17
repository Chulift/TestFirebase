package com.example.chulift.testfirebase;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, ChildEventListener, ValueEventListener {

    private final String ROOT = "HEART";
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    View rootView;
    TextView bpmText;
    TextView gsrText;
    TextView emoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        setupFirebase();
    }

    private void setupFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ROOT);

        mDatabaseReference.addChildEventListener(this);
        mDatabaseReference.addValueEventListener(this);
        //Log.i("DatabaseRef", mDatabaseReference.toString());
    }

    private void setupView() {
        findViewById(R.id.sendBtn).setOnClickListener(this);
        rootView = findViewById(R.id.layout_main);
        bpmText = findViewById(R.id.bpmShow);
        gsrText = findViewById(R.id.gsrShow);
        emoText = findViewById(R.id.emoShow);
    }

    private void send() {
        //timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant instant = timestamp.toInstant();
        //date
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a", Locale.ENGLISH);

        Map heart = new Heart(
                Integer.parseInt(((EditText) (findViewById(R.id.param1))).getText().toString()),
                "Bliss",
                Integer.parseInt(((EditText) (findViewById(R.id.param2))).getText().toString()),
                instant.toEpochMilli(),
                simpleDateFormat.format(now)
        ).toMap();
        mDatabaseReference.push().setValue(heart);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.sendBtn)
            send();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i("DataSnapshot", dataSnapshot.toString());
        try {
            Heart heart = dataSnapshot.getValue(Heart.class);

            Log.d("Heart", heart.BPM + ":" + heart.GSR);
            //Snackbar.make(rootView, heart.toString(), Snackbar.LENGTH_LONG).show();
            bpmText.setText(String.valueOf(heart.BPM));
            gsrText.setText(String.valueOf(heart.GSR));
            emoText.setText(String.valueOf(heart.Emo));

        } catch (Exception e) {
            Log.e("DataSnapshot", e.toString());
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

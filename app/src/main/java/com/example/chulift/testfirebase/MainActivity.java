package com.example.chulift.testfirebase;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener {

    private final String REF = "heart";
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFirebase();
        setupView();
    }

    private void setupFirebase() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(REF);

        mDatabaseReference.addChildEventListener(this);
        Log.i("DatabaseRef", mDatabaseReference.toString());
    }

    private void setupView() {
        findViewById(R.id.sendBtn).setOnClickListener(this);
        rootView = findViewById(R.id.layout_main);
    }

    private void send() {
        Map heart = new Heart(
                Integer.parseInt(((EditText)(findViewById(R.id.param1))).getText().toString()),
                Integer.parseInt(((EditText)(findViewById(R.id.param2))).getText().toString()))
                .toMap();
        mDatabaseReference.push().setValue(heart);
    }
    @Override
    public void onClick(View view) {
        int i  = view.getId();
        if (i == R.id.sendBtn)
            send();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.i("DataSnapshot", dataSnapshot.toString());
        try {
            Heart heart = dataSnapshot.getValue(Heart.class);
            Snackbar.make(rootView, heart.toString(), Snackbar.LENGTH_LONG).show();
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
    public void onCancelled(DatabaseError databaseError) {

    }
}

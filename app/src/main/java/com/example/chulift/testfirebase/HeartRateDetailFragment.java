package com.example.chulift.testfirebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HeartRateDetailFragment extends Fragment
        implements View.OnClickListener, ChildEventListener {

    //Interface
    private TextView tvBPM;
    private TextView tvEmo;
    private TextView tvGSR;
    private TextView tvEmergencyCall;
    private EditText etPhoneNo;

    //FireBase
    private static final String ROOT = "HEART";
    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mDatabaseReference;

    //Timer
    CountDownTimer countDownTimer;

    private boolean isCallEnable = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_heart_rate_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupFireBase();
        initialize();
    }

    private void setupView(@NonNull View view) {
        tvBPM = view.findViewById(R.id.bpmTextView);
        tvEmo = view.findViewById(R.id.emoTextView);
        tvGSR = view.findViewById(R.id.gsrTextView);
        tvEmergencyCall = view.findViewById(R.id.emergencyCallTextView);
        view.findViewById(R.id.settingsBtn).setOnClickListener(this);
        etPhoneNo = view.findViewById(R.id.phoneNoEditText);
    }

    private void setupFireBase() {
        mFireBaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFireBaseDatabase.getReference().child(ROOT);
        mDatabaseReference.addChildEventListener(this);
    }

    private void initialize() {
        tvEmergencyCall.setText(tvEmergencyCall.getText().toString());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isCallEnable = sharedPreferences.getBoolean(Settings.CALLING_PREFERENCES, false);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Log.i("Clicked", String.valueOf(viewId));
        if (viewId == R.id.settingsBtn) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new PrefsFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d("Data", dataSnapshot.toString());
        try {
            Heart heart = dataSnapshot.getValue(Heart.class);

            tvBPM.setText(getResources()
                    .getString(R.string.bpm_output_text)
                    .concat(String.valueOf(heart != null ? heart.BPM : 0)));
            tvEmo.setText(String.valueOf(
                    heart != null ? heart.Emo : getResources().getString(R.string.blank)));
            tvGSR.setText(getResources()
                    .getString(R.string.gsr_output_text)
                    .concat(String.valueOf(heart != null ? heart.GSR : 0)));
            checkBPM(heart.BPM);
        } catch (DatabaseException ex) {
            Log.e("FireBase Error", ex.toString());
        } catch (Exception e) {
            Log.e("Other Error", e.toString());
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //Do nothing.
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        //Do nothing.
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        //Do nothing.
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e("DatabaseError", databaseError.toString());
    }

    private void checkBPM(int bpm) {
        if (bpm >= Settings.MAX_BPM) {
            if (countDownTimer == null) {
                countDownTimer = new CountDownTimer(Settings.maxTime, Settings.interval) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        call();
                    }
                }.start();
            }
        } else {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            countDownTimer = null;
        }
    }

    private void call() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && isCallEnable) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (!basicCheck()) {
                Toast.makeText(getActivity(), "Please specify phone no below.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + etPhoneNo.getText().toString()));
            getActivity().startActivity(callIntent);
        } else if (!isCallEnable) {
            Toast.makeText(getActivity(), "Please enable call service in settings.", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getActivity(), "Please enable call service for this app.", Toast.LENGTH_SHORT).show();
    }

    private boolean basicCheck() {
        if (etPhoneNo.getText().toString().equals("")) {
            return false;
        }
        return true;
    }
}

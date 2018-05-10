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
        implements View.OnClickListener {

    //Interface
    private TextView tvBPM;
    private TextView tvEmo;
    private TextView tvGSR;

    //FireBase
    private DatabaseReference mHeartRateReference;
    private ChildEventListener mHeartRateListener;

    private boolean isCallEnable = false;
    private String phoneNo = "";
    private Long timeStamp;

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

    @Override
    public void onStart() {
        super.onStart();
        ChildEventListener heartRateListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.toString());
            }
        };

        mHeartRateReference.limitToLast(1).addChildEventListener(heartRateListener);
        mHeartRateListener = heartRateListener;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mHeartRateListener != null) {
            mHeartRateReference.removeEventListener(mHeartRateListener);
        }
    }

    private void setupView(@NonNull View view) {
        tvBPM = view.findViewById(R.id.bpmTextView);
        tvEmo = view.findViewById(R.id.emoTextView);
        tvGSR = view.findViewById(R.id.gsrTextView);
        view.findViewById(R.id.settingsBtn).setOnClickListener(this);
    }

    private void setupFireBase() {
        mHeartRateReference = FirebaseDatabase.getInstance().getReference("HEART");
    }

    private void initialize() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isCallEnable = sharedPreferences.getBoolean(Settings.CALLING_PREFERENCE, false);
        phoneNo = sharedPreferences.getString(Settings.PHONE_NO_PREFERENCE, Settings.PHONE_NO_DEF);
        timeStamp = 0L;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.settingsBtn) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new PrefsFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void checkBPM(final int bpm) {
        if (bpm >= Settings.MAX_BPM || bpm <= Settings.MIN_BPM) {
            if (timeStamp == 0) {
                timeStamp = System.currentTimeMillis() / 1000;
            }
            Long currentBPMTimeStamp = System.currentTimeMillis() / 1000;
            if (currentBPMTimeStamp - timeStamp > 10) timeStamp = System.currentTimeMillis() / 1000;
            if (currentBPMTimeStamp - timeStamp == 10) {
                Log.d("Call", currentBPMTimeStamp + ":" + timeStamp);
                call();
                timeStamp = System.currentTimeMillis() / 1000;
            }
        } else {
            timeStamp = System.currentTimeMillis() / 1000;
        }
    }

    private void call() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (!isCallEnable) {
                Toast.makeText(getActivity(), "Please enable call feature in settings.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phoneNo.equals("")) {
                Toast.makeText(getActivity(), "Please specify phone no in the settings.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNo));
            getActivity().startActivity(callIntent);
        } else {
            Toast.makeText(getActivity(), "Please enable call permission for this app.", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.lawnotes.lawadminapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lawnotes.lawadminapp1.Models.AdsModel;
import com.lawnotes.lawadminapp1.databinding.ActivityAdsControlBinding;

import java.util.HashMap;

public class AdsControlActivity extends AppCompatActivity {

    ActivityAdsControlBinding binding;
    FirebaseDatabase database;
    private String checkAdmob,checkFb;
    private String admobStatus,fbStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdsControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        checkStatus();
        checkFbStatus();
        adsData();

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("appId",binding.edtAppId.getText().toString());
                map.put("bannerAds",binding.edtBanner.getText().toString());
                map.put("interstitialAds",binding.edtInterstitial.getText().toString());

                database.getReference().child("adsId").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdsControlActivity.this, "updated", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {


                    AdsModel model = new AdsModel();
                    model.setStatus("true");

                    database.getReference().child("admob_ads").setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AdsControlActivity.this, "Admob ads on", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    AdsModel model = new AdsModel();
                    model.setStatus("false");

                    database.getReference().child("admob_ads").setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AdsControlActivity.this, "Admob ads off", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        binding.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {


                    AdsModel model = new AdsModel();
                    model.setStatus("true");

                    database.getReference().child("facebook_ads").setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AdsControlActivity.this, "Facebook ads on", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    AdsModel model = new AdsModel();
                    model.setStatus("false");

                    database.getReference().child("facebook_ads").setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(AdsControlActivity.this, "Facebook ads off", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    private void adsData() {

        database.getReference().child("adsId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    AdsModel model = snapshot.getValue(AdsModel.class);

                    String bannerAd = model.getBannerAds();
                    String interstitial = model.getInterstitialAds();

                    binding.edtAppId.setText(model.getAppId());
                    binding.edtBanner.setText(bannerAd);
                    binding.edtInterstitial.setText(interstitial);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdsControlActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void checkFbStatus() {

        database.getReference().child("facebook_ads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    AdsModel model = snapshot.getValue(AdsModel.class);

                    checkFb = model.getStatus();

                    if (checkFb.equals("true")){

                        binding.switch2.setChecked(true);
                    }
                    else {

                        binding.switch2.setChecked(false);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdsControlActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void checkStatus() {

        database.getReference().child("admob_ads").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    AdsModel model = snapshot.getValue(AdsModel.class);

                    checkAdmob = model.getStatus();

                    if (checkAdmob.equals("true")){

                        binding.switch1.setChecked(true);
                    }
                    else {

                        binding.switch1.setChecked(false);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdsControlActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
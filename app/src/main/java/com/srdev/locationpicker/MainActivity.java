package com.srdev.locationpicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.srdev.locationpicker.utils.SimplePlacePicker;


public class MainActivity extends AppCompatActivity {
    private TextView mLocationText, mLatitudeTv, mLongitudeTv;
    private LinearLayout mLlResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){

        mLocationText = findViewById(R.id.tv_location_text);
        mLatitudeTv = findViewById(R.id.tv_latitude);
        mLongitudeTv = findViewById(R.id.tv_longitude);
        mLlResult = findViewById(R.id.ll_result_data);

        Button selectLocation = findViewById(R.id.select_location_btn);
        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You must grant user permission for access device location first
                // please don't ignore this step >> Ignoring location permission may cause application to crash !
                if (hasPermissionInManifest(MainActivity.this,1, Manifest.permission.ACCESS_FINE_LOCATION))
                    selectLocationOnMap();
            }
        });
    }

    /**
     *
     * @param apiKey Required parameter, put your google maps and places api key
     *               { you must get a places key it's required for search autocomplete }
     *  */
    private void startMapActivity(String apiKey){
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(SimplePlacePicker.API_KEY,apiKey);
        intent.putExtras(bundle);
        startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE);
    }

    private void selectLocationOnMap() {
        String apiKey = getString(R.string.places_api_key);
        startMapActivity(apiKey);
    }

    private void updateUi(Intent data){
        mLocationText.setText(data.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS));
        mLatitudeTv.setText(String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,-1)));
        mLongitudeTv.setText(String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,-1)));
        mLlResult.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null) updateUi(data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectLocationOnMap();
        }
    }

    //check for location permission
    public static boolean hasPermissionInManifest(Activity activity, int requestCode, String permissionName) {
        if (ContextCompat.checkSelfPermission(activity,
                permissionName)
                != PackageManager.PERMISSION_GRANTED) {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionName},
                    requestCode);
        } else {
            return true;
        }
        return false;
    }

}
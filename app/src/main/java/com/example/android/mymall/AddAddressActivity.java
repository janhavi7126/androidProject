package com.example.android.mymall.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.mymall.Dac.MyAddresesModel;
import com.example.android.mymall.Dac.MyAdressesActivity;
import com.example.android.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddAddressActivity extends AppCompatActivity {

    private EditText city;
    private EditText locality;
    private EditText flat;
    private EditText pincode;
    private Spinner stateSpinner;
    private EditText landmark;
    private EditText name;
    private EditText mobile;
    private EditText alternateMob;
    private Dialog loadingDialog;
    private Button saveBtn;
    private String[] stateArrList;
    private String selectedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        saveBtn = findViewById(R.id.save_btn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality_or_street);
        flat = findViewById(R.id.flat_no_building_name);
        pincode = findViewById(R.id.pincode);
        stateSpinner = findViewById(R.id.state_spinner);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        alternateMob = findViewById(R.id.mobileOption);
        stateArrList = getResources().getStringArray(R.array.india_states);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.banner_back));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        ArrayAdapter spinnerState = new ArrayAdapter(this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.india_states));
        spinnerState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(spinnerState);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateArrList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())){
                    if (!TextUtils.isEmpty(locality.getText())){
                        if (!TextUtils.isEmpty(flat.getText())){
                            if (!TextUtils.isEmpty(landmark.getText())){
                                if(!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() == 6) {
                                    if (!TextUtils.isEmpty(name.getText())) {
                                        if (!TextUtils.isEmpty(mobile.getText()) && mobile.getText().length() == 10) {
                                            loadingDialog.show();

                                            String fullAddress =  flat.getText().toString() +",\n "+ locality.getText().toString() + ",\n "+ landmark.getText().toString() +",\n "+ city.getText().toString() +",\n "+ selectedState ;

                                            Map<String, Object> addressesMap = new HashMap<>();
                                            addressesMap.put("list_size", (long) DbQueries.myAddresesModelList.size() + 1);


                                            if (TextUtils.isEmpty(alternateMob.getText())) {
                                                addressesMap.put("mobile_no_" + String.valueOf((long) DbQueries.myAddresesModelList.size() + 1),mobile.getText().toString());
                                            }else{
                                                addressesMap.put("mobile_no_" + String.valueOf((long) DbQueries.myAddresesModelList.size() + 1), mobile.getText().toString() + "or" + alternateMob.getText().toString());
                                            }
                                            addressesMap.put("fullname_" + String.valueOf((long) DbQueries.myAddresesModelList.size() + 1), name.getText().toString());
                                            addressesMap.put("address_" + String.valueOf((long) DbQueries.myAddresesModelList.size() + 1), fullAddress);
                                            addressesMap.put("pincode_" + String.valueOf((long) DbQueries.myAddresesModelList.size() + 1), pincode.getText().toString());
                                            addressesMap.put("selected_" + String.valueOf((long) DbQueries.myAddresesModelList.size() + 1), true);

                                            if (DbQueries.myAddresesModelList.size() > 0) {
                                                addressesMap.put("selected_" +String.valueOf((long) DbQueries.selectedAddress + 1), false);
                                            }

                                            FirebaseFirestore.getInstance().collection("USERS")
                                                    .document(FirebaseAuth.getInstance().getUid())
                                                    .collection("USER_DATA")
                                                    .document("MY_ADDRESSES")
                                                    .update(addressesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DbQueries.myAddresesModelList.size() > 0) {
                                                            DbQueries.myAddresesModelList.get(DbQueries.selectedAddress).setSelected(false);
                                                        }
                                                        if (TextUtils.isEmpty(alternateMob.getText())) {
                                                            DbQueries.myAddresesModelList.add(new MyAddresesModel(name.getText().toString() , fullAddress, pincode.getText().toString(), true,mobile.getText().toString()));
                                                        }else{
                                                            DbQueries.myAddresesModelList.add(new MyAddresesModel(name.getText().toString()+ "\n" , fullAddress, pincode.getText().toString(), true,mobile.getText().toString() + " Or "+ alternateMob.getText().toString()));
                                                        }


                                                        if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                            startActivity(new Intent(AddAddressActivity.this, DeliveryActivity.class));
                                                        }else{
                                                          MyAdressesActivity.refreshItem(DbQueries.selectedAddress,DbQueries.myAddresesModelList.size() -1 );
                                                        }
                                                        DbQueries.selectedAddress  = DbQueries.myAddresesModelList.size() -1 ;
                                                        finish();
                                                    } else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    loadingDialog.dismiss();
                                                }
                                            });

                                        }else {
                                            mobile.requestFocus();
                                            Toast.makeText(getApplicationContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        name.requestFocus();
                                    }
                                }else {
                                    pincode.requestFocus();
                                    Toast.makeText(getApplicationContext(), "Please enter valid pincode", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                landmark.requestFocus();
                            }
                        }else {
                            flat.requestFocus();
                        }
                    }else {
                        locality.requestFocus();
                    }
                }else {
                    city.requestFocus();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
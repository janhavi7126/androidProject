package com.example.android.mymall.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymall.Activities.DashboardActivity;
import com.example.android.mymall.Activities.SignInFragment;
import com.example.android.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }


    private TextView already;
    private FrameLayout frameLayout;

    private EditText email , password , fullName , confirmPass ;
    private ImageButton closeBtn;
    private Button signUpBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    public static boolean disableClosebtn = false;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        already = view.findViewById(R.id.already);
        frameLayout = getActivity().findViewById(R.id.register_frame);

        email = view.findViewById(R.id.signUp_emailId);
        fullName = view.findViewById(R.id.signUp_fullname);
        password = view.findViewById(R.id.signUp_pass);
        confirmPass =view.findViewById(R.id.confirmPass);
        closeBtn = view.findViewById(R.id.closeBtn);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        progressBar = view.findViewById(R.id.signUp_progress);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();

        if (disableClosebtn){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }


        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new SignInFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }

    private void check() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullName.getText())){
                if (!TextUtils.isEmpty(password.getText())){
                    if (!TextUtils.isEmpty(confirmPass.getText())){
                        signUpBtn.setEnabled(true);
                        signUpBtn.setTextColor(Color.WHITE);
                    }
//                    else{ signUpBtn.setEnabled(false);
//                        signUpBtn.setTextColor(Color.argb(50,255,255,255)); }
                }
//                else{ signUpBtn.setEnabled(false);
//                    signUpBtn.setTextColor(Color.argb(50,255,255,255)); }
            }
//            else{ signUpBtn.setEnabled(false);
//                signUpBtn.setTextColor(Color.argb(50,255,255,255)); }
        }
//        else{ signUpBtn.setEnabled(false);
//            signUpBtn.setTextColor(Color.argb(50,255,255,255));
//        }
    }

    private void checkEmailAndPassword(){
        Drawable customErrorIcon = getResources().getDrawable(R.drawable.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());



        if (email.getText().toString().matches(emailPattern)){
            if(password.length() >= 8) {
                if (password.getText().toString().equals(confirmPass.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);
                    signUpBtn.setEnabled(false);

                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> userdata = new HashMap<>();
                                        userdata.put("name", fullName.getText().toString());
                                        userdata.put("email", email.getText().toString());
                                        userdata.put("profile", "");

                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).set(userdata)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            CollectionReference collectionReference = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA");
                                                            CollectionReference collectionReference1 = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS");


                                                            List<String> documentNames = new ArrayList<>();
                                                            documentNames.add("MY_WISHLIST");
                                                            documentNames.add("MY_RATINGS");
                                                            documentNames.add("MY_CART");
                                                            documentNames.add("MY_ADDRESSES");

                                                            List<String> documentNames1 = new ArrayList<>();
                                                            documentNames1.add("reward1");
                                                            documentNames1.add("reward2");

                                                            Map<String, Object> wishlistAdd = new HashMap<>();
                                                            wishlistAdd.put("list_size", (long) 0);

                                                            Map<String, Object> ratingsAdd = new HashMap<>();
                                                            ratingsAdd.put("list_size", (long) 0);

                                                            Map<String, Object> cartAdd = new HashMap<>();
                                                            cartAdd.put("list_size", (long) 0);

                                                            Map<String, Object> myAddressAdd = new HashMap<>();
                                                            myAddressAdd.put("list_size", (long) 0);

                                                            Calendar calendar = Calendar.getInstance();
                                                            calendar.add(Calendar.DAY_OF_YEAR, 1);

                                                            Map<String, Object> reward1add = new HashMap<>();
                                                            reward1add.put("already_used",false);
                                                            reward1add.put("amount","100");
                                                            reward1add.put("body","Get Flat Rs.100 OFF  on any items above Rs.500/- and below Rs.50000/-");
                                                            reward1add.put("lower_limit","500");
                                                            reward1add.put("type","Flat Rs.*OFF");
                                                            reward1add.put("upper_limit","50000");
                                                            reward1add.put("validity", calendar.getTime());

                                                            Map<String, Object> reward2add = new HashMap<>();
                                                            reward2add.put("already_used",false);
                                                            reward2add.put("percentage","20");
                                                            reward2add.put("body","20% discount on any item above Rs 500 and below Rs.50000");
                                                            reward2add.put("lower_limit","500");
                                                            reward2add.put("type","Discount");
                                                            reward2add.put("upper_limit","50000");
                                                            reward2add.put("validity", calendar.getTime());


                                                            List<Map<String, Object>> documentFields = new ArrayList<>();
                                                            documentFields.add(wishlistAdd);
                                                            documentFields.add(ratingsAdd);
                                                            documentFields.add(cartAdd);
                                                            documentFields.add(myAddressAdd);

                                                            List<Map<String, Object>> documentFields1 = new ArrayList<>();
                                                            documentFields1.add(reward1add);
                                                            documentFields1.add(reward2add);
                                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


                                                            for (int i = 0; i < documentNames.size(); i++) {

                                                                int finalI = i;
                                                                collectionReference.document(documentNames.get(i)).set(documentFields.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            if (finalI == documentNames.size() - 1) {
                                                                                for (int j = 0; j < documentNames1.size(); j++) {

                                                                                    collectionReference1.document(documentNames1.get(j)).set(documentFields1.get(j)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            Map<String ,Object> upd = new HashMap<>();
                                                                                            upd.put("Last seen",FieldValue.serverTimestamp());
                                                                                            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).set(upd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    mainIntent();
                                                                                                }
                                                                                            });

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        } else {
                                                                            String error = task.getException().getMessage();
                                                                            signUpBtn.setEnabled(true);
                                                                            signUpBtn.setTextColor(Color.rgb(255, 255, 255));
                                                                            progressBar.setVisibility(View.INVISIBLE);
                                                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                                                                        }

                                                                    }
                                                                });



                                                            }


                                                        } else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                    } else {
                                        String error = task.getException().getMessage();
                                        signUpBtn.setEnabled(true);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    confirmPass.setError("Password dosen't match", customErrorIcon);
                    confirmPass.requestFocus();
                }
            }else{
                password.setError("Password should be at least 8 character", customErrorIcon);
                password.requestFocus();
            }

        }else{
            email.setError("Invalid email!",customErrorIcon);
            email.requestFocus();
        }

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void mainIntent(){
        if (disableClosebtn){
            disableClosebtn = false;
        }else {
            Intent mainIntent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();

    }
}
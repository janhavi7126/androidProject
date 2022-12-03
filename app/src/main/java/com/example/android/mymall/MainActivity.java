package com.example.android.mymall.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Animation anim,anim1,anim2;
    ImageView iv;
    TextView tv,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        firebaseAuth = FirebaseAuth.getInstance();
//        iv = findViewById(R.id.basket);
//        tv = findViewById(R.id.store);
//        tv2 =findViewById(R.id.tv2);
//        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
//        anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
//        anim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
//        Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
//
//
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        iv.startAnimation(anim);
//
//
//        anim1.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//                if (currentUser == null){
//                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
//                    finish();
//                }else{
//                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        tv2.startAnimation(anim1);
//
//        anim2.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        tv.startAnimation(anim2);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        if(currentUser==null){
//
//            Intent loginintent = new Intent(MainActivity.this, RegisterActivity.class);
//            startActivity(loginintent);
//            finish();
//        }else {
//
//            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).update("Last seen", FieldValue.serverTimestamp())
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                Intent mainintent = new Intent(MainActivity.this, DashboardActivity.class);
//                                startActivity(mainintent);
//                                finish();
//                            }else {
//                                String error=task.getException().getMessage();
//                                Toast.makeText(MainActivity.this,error,Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
    }
}
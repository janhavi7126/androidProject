package com.example.android.mymall.Activities;

import static com.example.android.mymall.Activities.RegisterActivity.setSignUpFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.mymall.Dac.MyAccountFragment;
import com.example.android.mymall.Dac.MyCartFragment;
import com.example.android.mymall.Dac.MyOrderFragment;
import com.example.android.mymall.Dac.MyRewardsFragment;
import com.example.android.mymall.Dac.MyWishListFragment;
import com.example.android.mymall.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int HOME_FRAGMENT =0;
    private static final int CART_FRAGMENT =1;
    private static final int ORDER_FRAGMENT = 2 ;
    private static final int WISHLIST_FRAGMENT=3;
    private static final int REWARDS_FRAGMENT=4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static  boolean showcart = false;
    public static Activity mainActivity;


    private int CURRENT_FRAGMENT = -1;
    private FrameLayout frameLayout;
    NavigationView navigationView;
    LinearLayout appBarLogo;
    private Dialog dialog;
    private Window window;
    private TextView badgeCount;
    public static DrawerLayout drawerLayout;
    private FirebaseUser currentUser;
    private ImageView addProfileIcon;
    private CircleImageView profileView;
    private TextView fullname,email;

    private int scrollFlags;
    private AppBarLayout.LayoutParams params;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolba);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);




        drawerLayout = findViewById(R.id.drawer_layout);

        appBarLogo = findViewById(R.id.app_logo);
        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);

        fullname=navigationView.getHeaderView(0).findViewById(R.id.name_);
        email=navigationView.getHeaderView(0).findViewById(R.id.email_id);
        addProfileIcon=navigationView.getHeaderView(0).findViewById(R.id.add_profile_pic);
        profileView=navigationView.getHeaderView(0).findViewById(R.id.profile_pic);


        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();


            if (showcart) {
                mainActivity = this;
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                goToFragments("My Cart", new MyCartFragment(), -2);
            } else {
                ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawerLayout.addDrawerListener(actionBarDrawerToggle);
                actionBarDrawerToggle.syncState();

                setFragment(new HomeFragment(), HOME_FRAGMENT);
            }

        dialog = new Dialog(DashboardActivity.this);
        dialog.setContentView(R.layout.signin_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignIn = dialog.findViewById(R.id.dialog_sign_in_btn);
        Button dialogSignUp = dialog.findViewById(R.id.dialog_sign_up_btn);
        Intent signInOrSignUp = new Intent(DashboardActivity.this, RegisterActivity.class);

        dialogSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableClosebtn = true;
                SignInFragment.disableClosebtn = true;
                dialog.dismiss();
                setSignUpFragment = false;
                startActivity(signInOrSignUp);

            }
        });

        dialogSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableClosebtn = true;
                SignInFragment.disableClosebtn = true;
                dialog.dismiss();
                setSignUpFragment = true;
                startActivity(signInOrSignUp);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
//
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
//
        invalidateOptionsMenu();
    }

    private void setFragment(Fragment fragment, int fragNo) {
        if (fragNo != CURRENT_FRAGMENT) {
            CURRENT_FRAGMENT = fragNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (CURRENT_FRAGMENT == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.dashboard, menu);
            MenuItem cartItem =  menu.findItem(R.id.action_cart);

                cartItem.setActionView(R.layout.badge_layout);
                ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
                badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
                badgeIcon.setImageResource(R.drawable.cart_icon);
                if(currentUser != null){
                    if(DbQueries.cartList.size() == 0){
                        DbQueries.loadCartList(DashboardActivity.this,new Dialog(DashboardActivity.this),false,badgeCount,new TextView(DashboardActivity.this));
                    }else {
                        badgeCount.setVisibility(View.VISIBLE);


                        if (DbQueries.cartList.size() <= 99) {
                            badgeCount.setText(String.valueOf(DbQueries.cartList.size()));
                        } else {
                            badgeCount.setText("99+");
                        }
                    }
                }


                cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (currentUser == null){
                            dialog.show();
                        }else{
                            goToFragments("MY CART",new MyCartFragment(),CART_FRAGMENT);
                        }
                    }
                });


        }
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search){

        }
//        else if (id == R.id.action_notifications){
//
//        }
        else if (id == R.id.action_cart){
            if (currentUser == null){
                dialog.show();
            }else{
                goToFragments("MY CART",new MyCartFragment(),CART_FRAGMENT);
            }
            return true;

        }else if (id == android.R.id.home) {
            if (showcart) {
                mainActivity = null;
                showcart = false;
                finish();
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }


    MenuItem menuItem;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        menuItem = item;

        if (currentUser != null) {
            drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    int id = menuItem.getItemId();
                    if (id == R.id.nav_mall) {
                        appBarLogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                        navigationView.getMenu().getItem(0).setChecked(true);

                    } else if (id == R.id.nav_orders) {
                        goToFragments("My Orders", new MyOrderFragment(), ORDER_FRAGMENT);

                    } else if (id == R.id.nav_rewards) {
                        goToFragments("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);



                    } else if (id == R.id.nav_cart) {
                        goToFragments("My Cart", new MyCartFragment(), CART_FRAGMENT);

                    } else if (id == R.id.nav_wishlist) {
                        goToFragments("My Wishlist", new MyWishListFragment(), WISHLIST_FRAGMENT);

                    } else if (id == R.id.nav_account) {
                        goToFragments("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);

                    } else if (id == R.id.nav_signout) {
                        FirebaseAuth.getInstance().signOut();
                        DbQueries.clearData();
                        Intent registerIntent = new Intent(DashboardActivity.this,RegisterActivity.class);
                        startActivity(registerIntent);
                        finish();
                    }
                }
            });




            return true;
        }else{
            dialog.show();
            return false;
        }

    }

    private void goToFragments(String title,Fragment fragment,int fragNo) {
        appBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        setFragment(fragment,fragNo);
        if (fragNo == ORDER_FRAGMENT){
            navigationView.getMenu().getItem(1).setChecked(true);

        }
        if (fragNo == REWARDS_FRAGMENT){
            navigationView.getMenu().getItem(2).setChecked(true);
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.reward_app_bar));
            window.setStatusBarColor(Color.parseColor("#6534C6"));
        }else {
            getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.purple_500));
            window.setStatusBarColor(getResources().getColor(R.color.purple_500));
        }
        if (fragNo == WISHLIST_FRAGMENT){
            navigationView.getMenu().getItem(4).setChecked(true);

        }

        if (fragNo == CART_FRAGMENT || showcart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else{
            params.setScrollFlags(scrollFlags);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            if (CURRENT_FRAGMENT == HOME_FRAGMENT) {
                CURRENT_FRAGMENT = -1;
                super.onBackPressed();
            }else{
                if (showcart) {
                    mainActivity = null;
                    showcart = false;
                    finish();
                }else {

                    appBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                    getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.purple_500));
                    window.setStatusBarColor(getResources().getColor(R.color.purple_500));

                }

            }
        }

    }
}
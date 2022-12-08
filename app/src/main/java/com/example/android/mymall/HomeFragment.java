package com.example.android.mymall.Activities;

import static com.example.android.mymall.Activities.DashboardActivity.drawerLayout;
import static com.example.android.mymall.Activities.DbQueries.categoryModelList;
import static com.example.android.mymall.Activities.DbQueries.lists;
import static com.example.android.mymall.Activities.DbQueries.loadCategories;
import static com.example.android.mymall.Activities.DbQueries.loadFragmentData;
import static com.example.android.mymall.Activities.DbQueries.loadedCategoriesNames;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.mymall.R;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter homePageAdapter;
    private ImageView noInternetConnection;
    private Button retryButton;
    private List<CategoryModel> fakeCategoryModelList = new ArrayList<>();
    private List<HomePageModel> fakeHomePageModelList = new ArrayList<>();
    public static SwipeRefreshLayout swipeRefreshLayout;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        homePageRecyclerView = view.findViewById(R.id.mainTestingRecyclerView);
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        retryButton = view.findViewById(R.id.retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.purple_500),getContext().getResources().getColor(R.color.purple_500),getContext().getResources().getColor(R.color.purple_500));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);


        LinearLayoutManager testLinearLayout = new LinearLayoutManager(getContext());
        testLinearLayout.setOrientation(RecyclerView.VERTICAL);
        homePageRecyclerView.setLayoutManager(testLinearLayout);








        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            retryButton.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);



            if (categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView,getContext());
            }else{
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);


            if (lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());

                loadFragmentData(homePageRecyclerView,getContext(),0,"HOME");

            }else{
                homePageAdapter = new HomePageAdapter(lists.get(0));
                homePageAdapter.notifyDataSetChanged();
            }
            homePageRecyclerView.setAdapter(homePageAdapter);



        }else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.cart).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
        }

       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               swipeRefreshLayout.setRefreshing(true);
               reload();

           }
       });

      retryButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              reload();
          }
      });

            return view;
    }

    private void reload() {
        DbQueries.clearData();
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);
            categoryAdapter = new CategoryAdapter(fakeCategoryModelList);
            homePageAdapter = new HomePageAdapter(fakeHomePageModelList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            homePageRecyclerView.setAdapter(homePageAdapter);

            loadCategories(categoryRecyclerView,getContext());

            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(homePageRecyclerView,getContext(),0,"HOME");

        } else {
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            categoryRecyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.cart).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }


}
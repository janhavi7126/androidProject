package com.example.android.mymall.Dac;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mymall.Activities.DbQueries;
import com.example.android.mymall.R;

import java.util.ArrayList;
import java.util.List;


public class MyRewardsFragment extends Fragment {



    public MyRewardsFragment() {
        // Required empty public constructor
    }
    private RecyclerView myRewardsRecyclerView;
    private Dialog loadingDialog;
    public static RewardAdapter rewardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        myRewardsRecyclerView = view.findViewById(R.id.my_rewards_recycler_view);

        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.banner_back));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        LinearLayoutManager linearLayoutManagerReward = new LinearLayoutManager(getContext());
        linearLayoutManagerReward.setOrientation(RecyclerView.VERTICAL);
        myRewardsRecyclerView.setLayoutManager(linearLayoutManagerReward);

//        List<MyRewardModel> myRewardModelList = new ArrayList<>();
//        myRewardModelList.add(new MyRewardModel("Discount","GET 20% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Cashback","GET 50% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Buy 1 Get 1 Free","GET 80% OFF on any product above Rs.500/- and below Rs.2500","27th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Rewards","GET 10% OFF on any product above Rs.500/- and below Rs.2500","28th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Discount","GET 60% OFF on any product above Rs.500/- and below Rs.2500","25th May 2021"));
//        myRewardModelList.add(new MyRewardModel("Cashback","GET 20% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Buy 1 Get 1 Free","GET 30% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Discount","GET 45% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Cashback","GET 5% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Discount","GET 20% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Cashback","GET 50% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Buy 1 Get 1 Free","GET 80% OFF on any product above Rs.500/- and below Rs.2500","27th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Rewards","GET 10% OFF on any product above Rs.500/- and below Rs.2500","28th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Discount","GET 60% OFF on any product above Rs.500/- and below Rs.2500","25th May 2021"));
//        myRewardModelList.add(new MyRewardModel("Cashback","GET 20% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Buy 1 Get 1 Free","GET 30% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Discount","GET 45% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//        myRewardModelList.add(new MyRewardModel("Cashback","GET 5% OFF on any product above Rs.500/- and below Rs.2500","25th April 2021"));
//

        rewardAdapter = new RewardAdapter(DbQueries.myRewardModelList,false);
        myRewardsRecyclerView.setAdapter(rewardAdapter);

        if(DbQueries.myRewardModelList.size() == 0){
            DbQueries.loadRewards(getContext(),loadingDialog,true);
        }else {
            loadingDialog.dismiss();
        }


        rewardAdapter.notifyDataSetChanged();




        return view;
    }
}
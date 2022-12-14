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


public class MyWishListFragment extends Fragment {



    public static WishListAdapter wishListAdapter;
    private Dialog loadingDialog;
    private RecyclerView wishListRecyclerView;


    public MyWishListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_wish_list, container, false);
        wishListRecyclerView = view.findViewById(R.id.my_wishlist_recycler_view);
        //deleteBtn = view.findViewById(R.id.wishlist_delete);

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.banner_back));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        LinearLayoutManager wishlistLinearLayout = new LinearLayoutManager(getContext());
        wishlistLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        wishListRecyclerView.setLayoutManager(wishlistLinearLayout);

        if (DbQueries.wishlistModelList.size() == 0){
            DbQueries.wishlist.clear();
            DbQueries.loadWishlist(getContext(),loadingDialog,true);
        }else{
            loadingDialog.dismiss();
        }


        wishListAdapter = new WishListAdapter(DbQueries.wishlistModelList,true);
        wishListRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();

        return view;
    }
}
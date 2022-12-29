package com.example.android.mymall.Activities;

import static com.example.android.mymall.Activities.DbQueries.lists;
import static com.example.android.mymall.Activities.DbQueries.loadFragmentData;
import static com.example.android.mymall.Activities.DbQueries.loadedCategoriesNames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.mymall.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private HomePageAdapter homePageAdapter;
    private List<HomePageModel> fakeHomePageModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolba);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getStringExtra("categoryNameKey");
        getSupportActionBar().setTitle(title);

//        List<SliderModel> fakeSliderModelList = new ArrayList<>();
//        fakeSliderModelList.add(new SliderModel("null","#ffffff"));
//        fakeSliderModelList.add(new SliderModel("null","#ffffff"));
//        fakeSliderModelList.add(new SliderModel("null","#ffffff"));
//        fakeSliderModelList.add(new SliderModel("null","#ffffff"));
//        fakeSliderModelList.add(new SliderModel("null","#ffffff"));
//
//        List<HorizontalScrollViewModel> fakeHorizontalScrollViewModelList = new ArrayList<>();
//        fakeHorizontalScrollViewModelList.add(new HorizontalScrollViewModel("","","","",""));
//        fakeHorizontalScrollViewModelList.add(new HorizontalScrollViewModel("","","","",""));
//        fakeHorizontalScrollViewModelList.add(new HorizontalScrollViewModel("","","","",""));
//        fakeHorizontalScrollViewModelList.add(new HorizontalScrollViewModel("","","","",""));
//        fakeHorizontalScrollViewModelList.add(new HorizontalScrollViewModel("","","","",""));
//        fakeHorizontalScrollViewModelList.add(new HorizontalScrollViewModel("","","","",""));
//        fakeHorizontalScrollViewModelList.add(new HorizontalScrollViewModel("","","","",""));
//
//        fakeHomePageModelList.add(new HomePageModel(0,fakeSliderModelList));
//        fakeHomePageModelList.add(new HomePageModel(1,"","#ffffff"));
//        fakeHomePageModelList.add(new HomePageModel(2,"",fakeHorizontalScrollViewModelList,"#ffffff",new ArrayList<>()));
//        fakeHomePageModelList.add(new HomePageModel(3,"",fakeHorizontalScrollViewModelList,"#ffffff"));
//
//        homePageAdapter = new HomePageAdapter(fakeHomePageModelList);
//
//


        final List<SliderModel> sliderModelList = new ArrayList<SliderModel>();


        List<HorizontalScrollViewModel> horizontalScrollViewModelList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.categoryActivityRecyclerView);
        LinearLayoutManager testLinearLayout = new LinearLayoutManager(this);
        testLinearLayout.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(testLinearLayout);

        int listPosition = 0;
        for (int i = 0; i < loadedCategoriesNames.size(); i++) {
            if (loadedCategoriesNames.get(i).equals(title.toUpperCase())){
                listPosition = i;
            }
        }
        if (listPosition == 0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(recyclerView,this,loadedCategoriesNames.size() - 1,title);
        }else{
            homePageAdapter = new HomePageAdapter(lists.get(listPosition));
            homePageAdapter.notifyDataSetChanged();
        }
        recyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_icon,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search){

        }else if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
package com.haroldadmin.kshitijchauhan.rxfittest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    RecyclerView activitiesRecyclerView;
    ProgressBar progressBar;
    PhysicalActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activitiesRecyclerView = findViewById(R.id.activities_recycler_view);
        progressBar = findViewById(R.id.progressBar);

        MainViewModel mainViewModel = ViewModelProviders
                .of(this)
                .get(MainViewModel.class);

        adapter = new PhysicalActivityAdapter(new ArrayList<PhysicalActivity>(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        activitiesRecyclerView.setLayoutManager(linearLayoutManager);
        activitiesRecyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        itemDecor.setDrawable(ContextCompat.getDrawable(this, R.drawable.list_divider));
        activitiesRecyclerView.addItemDecoration(itemDecor);

        mainViewModel.loadListOfActivities();
        mainViewModel.getLoadingStatus()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean) {
                            progressBar.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        mainViewModel.getListOfActivities()
                .observe(this, new Observer<List<PhysicalActivity>>() {
                    @Override
                    public void onChanged(List<PhysicalActivity> physicalActivities) {
                        adapter.updateList(physicalActivities);
                    }
                });
    }
}

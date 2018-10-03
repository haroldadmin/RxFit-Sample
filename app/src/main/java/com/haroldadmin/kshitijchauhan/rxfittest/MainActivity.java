package com.haroldadmin.kshitijchauhan.rxfittest;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    RecyclerView activitiesRecyclerView;
    ConstraintLayout rootView;
    SwipeRefreshLayout swipeRefreshLayout;
    PhysicalActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activitiesRecyclerView = findViewById(R.id.activities_recycler_view);
        rootView = findViewById(R.id.root_view);
        swipeRefreshLayout = findViewById(R.id.swipe_container);

        final MainViewModel mainViewModel = ViewModelProviders
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
                    public void onChanged(Boolean isLoading) {
                        swipeRefreshLayout.setRefreshing(isLoading);
                    }
                });
        mainViewModel.getListOfActivities()
                .observe(this, new Observer<List<PhysicalActivity>>() {
                    @Override
                    public void onChanged(List<PhysicalActivity> physicalActivities) {
                        if (physicalActivities.isEmpty()) {
                            Snackbar.make(rootView, "Google Fit returned no activities", Snackbar.LENGTH_SHORT).show();
                        } else {
                            adapter.updateList(physicalActivities);
                            Snackbar.make(rootView, "Google Fit returned " + physicalActivities.size() + " activities", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearAdapter();
                mainViewModel.loadListOfActivities();
                Snackbar.make(rootView, "Refreshing", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}

package com.haroldadmin.kshitijchauhan.rxfittest;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.patloew.rxfit.RxFit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private long endTime;
    private long startTime;
    private MutableLiveData<List<PhysicalActivity>> activitiesLiveData;
    private MutableLiveData<Boolean> isLoading;

    public MainViewModel(@NonNull Application application) {
        super(application);
        activitiesLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    private RxFit rxFit = new RxFit(
            getApplication(),
            new Api[]{Fitness.HISTORY_API},
            new Scope[]{new Scope(Scopes.FITNESS_ACTIVITY_READ)}
    );


    private DataReadRequest getDataReadRequest() {
        Calendar cal = Calendar.getInstance();
        endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        startTime = cal.getTimeInMillis();

        return new DataReadRequest.Builder()
                .read(DataType.TYPE_ACTIVITY_SEGMENT)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
    }

    void loadListOfActivities() {

        final List<PhysicalActivity> activities = new ArrayList<>();

        DataReadRequest dataReadRequest = getDataReadRequest();

        Observer<PhysicalActivity> physicalActivityObserver = new Observer<PhysicalActivity>() {
            @Override
            public void onSubscribe(Disposable d) {
                isLoading.postValue(true);
                Log.d("MainViewModel", "onSubscribe: Starting to emit items!");
            }

            @Override
            public void onNext(PhysicalActivity activity) {
                Log.d("MainViewModel", "onNext for " + activity.getName());
                activities.add(activity);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("MainViewModel", "onError");
            }

            @Override
            public void onComplete() {
                Log.d("MainViewModel", "onComplete: Completed emitting activities!");
                activitiesLiveData.postValue(activities);
                isLoading.postValue(false);
            }
        };

        Observable<PhysicalActivity> observable = rxFit.history().readDataSets(dataReadRequest)
                .filter(new Predicate<DataSet>() {
                    @Override
                    public boolean test(DataSet dataSet) {
                        return !dataSet.isEmpty();
                    }
                })
                .map(new Function<DataSet, List<DataPoint>>() {
                    @Override
                    public List<DataPoint> apply(DataSet dataSet) {
                        return dataSet.getDataPoints();
                    }
                })
                .flatMap(new Function<List<DataPoint>, ObservableSource<DataPoint>>() {
                    @Override
                    public ObservableSource<DataPoint> apply(List<DataPoint> dataPoints) {
                        return Observable.fromIterable(dataPoints);
                    }
                })
                .map(new Function<DataPoint, PhysicalActivity>() {
                    @Override
                    public PhysicalActivity apply(DataPoint dataPoint) {
                        return new PhysicalActivity(dataPoint.getStartTime(TimeUnit.MILLISECONDS), dataPoint.getEndTime(TimeUnit.MILLISECONDS), dataPoint.getValue(Field.FIELD_ACTIVITY).asActivity());
                    }
                })
                .map(new Function<PhysicalActivity, PhysicalActivity>() {
                    @Override
                    public PhysicalActivity apply(PhysicalActivity activity) {
                        activity.setName(capitalizeFirstLetter(activity.getName()));
                        return activity;
                    }
                })
                .subscribeOn(Schedulers.io());

        observable.subscribe(physicalActivityObserver);
    }

    LiveData<List<PhysicalActivity>> getListOfActivities() {
        return activitiesLiveData;
    }

    LiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }

    private String capitalizeFirstLetter(String input) {
        String output = Character.toUpperCase(input.charAt(0)) + input.substring(1);
        return output;
    }
}

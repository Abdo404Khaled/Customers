package com.example.customers.Modules;

import android.app.Dialog;
import android.content.Context;
import android.location.Geocoder;

import com.example.customers.Adapters.CustomerAdapter;
import com.example.customers.Models.Database.DatabaseHelper;
import com.example.customers.Presenter.MainActivityPresenter;
import com.example.customers.Views.MainActivity;
import com.example.customers.Views.iMainActivity;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@Module
@InstallIn(ActivityComponent.class)
public abstract class AppModule {

    @Provides
    @ActivityScoped
    @Named ("userDiag")
    public static Dialog provideUserDialog(@ActivityContext Context mContext) {
        return  new Dialog(mContext);
    }

    @Provides
    @ActivityScoped
    @Named ("addDiag")
    public static Dialog provideAddDialog(@ActivityContext Context mContext) {
        return  new Dialog(mContext);
    }

    @Provides
    @ActivityScoped
    public static Geocoder provideGeocoder(@ActivityContext Context mContext) {
        return  new Geocoder(mContext);
    }

}

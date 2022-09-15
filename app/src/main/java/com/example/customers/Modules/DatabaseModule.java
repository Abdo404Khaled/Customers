package com.example.customers.Modules;

import android.content.Context;

import com.example.customers.Models.Database.DatabaseHelper;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.ActivityScoped;

@Module
@InstallIn(ActivityComponent.class)
public class DatabaseModule {

    @Provides
    @ActivityScoped
    public static DatabaseHelper provideDatabaseHelper(@ActivityContext Context mContext){
        return new DatabaseHelper(mContext);
    }
}

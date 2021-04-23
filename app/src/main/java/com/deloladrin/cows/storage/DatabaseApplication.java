package com.deloladrin.cows.storage;

import android.app.Application;

import com.deloladrin.cows.Preferences;
import com.deloladrin.cows.data.Company;

public class DatabaseApplication extends Application
{
    private static final String DATABASE_NAME = "Cows.db3";

    protected Database database;
    protected Preferences preferences;

    @Override
    public void onCreate()
    {
        super.onCreate();

        this.database = new Database(this, DATABASE_NAME);
        this.database.register(Company.class);

        this.database.initialize();

        this.preferences = new Preferences(this);
    }

    public Database getDatabase()
    {
        return this.database;
    }

    public Preferences getPreferences()
    {
        return this.preferences;
    }
}

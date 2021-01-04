package com.deloladrin.cows.database;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.deloladrin.cows.data.UserPreferences;

public abstract class DatabaseActivity extends AppCompatActivity
{
    protected Database database;
    protected UserPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.database = new Database(this);
        this.preferences = new UserPreferences(this.database);
    }

    public Database getDatabase()
    {
        return this.database;
    }

    public UserPreferences getPreferences()
    {
        return this.preferences;
    }
}

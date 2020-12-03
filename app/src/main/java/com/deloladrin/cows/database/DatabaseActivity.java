package com.deloladrin.cows.database;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class DatabaseActivity extends AppCompatActivity
{
    protected Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.database = new Database(this);
    }

    public Database getDatabase()
    {
        return this.database;
    }
}

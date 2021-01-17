package com.deloladrin.cows.database;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.deloladrin.cows.data.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseActivity extends AppCompatActivity
{
    protected Database database;
    protected UserPreferences preferences;

    protected List<OnActivityResultListener> onActivityResultListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.database = new Database(this);
        this.preferences = new UserPreferences(this.database);

        this.onActivityResultListeners = new ArrayList<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        for (OnActivityResultListener listener : this.onActivityResultListeners)
        {
            listener.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Database getDatabase()
    {
        return this.database;
    }

    public UserPreferences getPreferences()
    {
        return this.preferences;
    }

    public void addOnActivityResultListener(OnActivityResultListener listener)
    {
        this.onActivityResultListeners.add(listener);
    }

    public interface OnActivityResultListener
    {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}

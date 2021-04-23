package com.deloladrin.cows;

import android.content.Context;
import android.content.SharedPreferences;

import com.deloladrin.cows.data.Company;
import com.deloladrin.cows.storage.Database;
import com.deloladrin.cows.storage.DatabaseApplication;

public class Preferences
{
    private static final String PREFERENCE_KEY_FILE = "com.deloladrin.cows.Preferences";
    private static final String PREFERENCE_KEY_ACTIVE_COMPANY = "com.deloladrin.cows.Preferences.ACTIVE_COMPANY";

    private Database database;
    private SharedPreferences preferences;

    public Preferences(DatabaseApplication app)
    {
        this.database = app.getDatabase();
        this.preferences = app.getSharedPreferences(PREFERENCE_KEY_FILE, Context.MODE_PRIVATE);
    }

    public Company getActiveCompany()
    {
        int companyID = this.preferences.getInt(PREFERENCE_KEY_ACTIVE_COMPANY, 0);

        return this.database.getTable(Company.class).select().stream()
                .filter((Company company) -> company.getID() == companyID)
                .findFirst()
                .orElse(null);
    }

    public void setActiveCompany(Company company)
    {
        if (company != null)
        {
            this.putInt(PREFERENCE_KEY_ACTIVE_COMPANY, company.getID());
        }
        else
        {
            this.putInt(PREFERENCE_KEY_ACTIVE_COMPANY, 0);
        }
    }

    private void putInt(String key, int value)
    {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public Database getDatabase()
    {
        return this.database;
    }

    public SharedPreferences getPreferences()
    {
        return this.preferences;
    }
}

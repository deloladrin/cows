package com.deloladrin.cows.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.deloladrin.cows.database.Database;

public class UserPreferences
{
    private static final String KEY_ACTIVE_COMPANY = "com.deloladrin.cows.data.UserPreferences.ACTIVE_COMPANY";

    private Database database;
    private SharedPreferences preferences;

    public UserPreferences(Database database)
    {
        this.database = database;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(database.getContext());
    }

    public Company getActiveCompany()
    {
        int id = this.preferences.getInt(KEY_ACTIVE_COMPANY, 0);

        if (id != 0)
        {
            return this.database.getCompanyTable().select(id);
        }

        return null;
    }

    public void setActiveCompany(Company company)
    {
        int id = 0;

        if (company != null)
        {
            id = company.getID();
        }

        this.storeInt(KEY_ACTIVE_COMPANY, id);
    }

    private void storeInt(String key, int value)
    {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}

package com.deloladrin.cows.loader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.deloladrin.cows.Preferences;
import com.deloladrin.cows.storage.Database;
import com.deloladrin.cows.storage.DatabaseApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoaderUtils
{
    public static Database getDatabase(Context context)
    {
        Context app = context.getApplicationContext();

        if (app instanceof DatabaseApplication)
        {
            return ((DatabaseApplication) app).getDatabase();
        }

        return null;
    }

    public static Database getDatabase(ParentView parent)
    {
        return LoaderUtils.getDatabase(parent.getContext());
    }

    public static Preferences getPreferences(Context context)
    {
        Context app = context.getApplicationContext();

        if (app instanceof DatabaseApplication)
        {
            return ((DatabaseApplication) app).getPreferences();
        }

        return null;
    }

    public static Preferences getPreferences(ParentView parent)
    {
        return LoaderUtils.getPreferences(parent.getContext());
    }

    public static <T extends ParentView> List<Field> getFields(T parent)
    {
        List<Field> fields = new ArrayList<>();
        Class<?> type = parent.getClass();

        while (type != null)
        {
            List<Field> current = Arrays.asList(type.getDeclaredFields());
            fields.addAll(current);

            type = type.getSuperclass();
        }

        return fields;
    }

    public static void update(Context context)
    {
        LoaderActivity activity = null;

        if (context instanceof LoaderActivity)
        {
            activity = (LoaderActivity) context;
        }

        else if (context instanceof ContextWrapper)
        {
            Context base = ((ContextWrapper) context).getBaseContext();

            if (base instanceof LoaderActivity)
            {
                activity = (LoaderActivity) base;
            }
        }

        if (activity != null)
        {
            activity.onUpdate();
        }
    }

    public static <T extends ParentView> void reload(T parent)
    {
        String packageName = parent.getContext().getPackageName();

        for (Field field : LoaderUtils.getFields(parent))
        {
            Class<?> type = field.getType();

            if (View.class.isAssignableFrom(type) ||
                Activity.class.isAssignableFrom(type))
            {
                if (!field.isAnnotationPresent(Ignore.class))
                {
                    String name = field.getName();

                    int childID = parent.getContext().getResources().getIdentifier(name, "id", packageName);
                    LoaderUtils.setChild(field, parent, childID);
                }
            }
        }
    }

    private static <T extends ParentView> void setChild(Field field, T parent, int childID)
    {
        try
        {
            field.setAccessible(true);

            if (childID != 0)
            {
                Object value = parent.findViewById(childID);
                field.set(parent, value);
            }
            else
            {
                field.set(parent, null);
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }
}

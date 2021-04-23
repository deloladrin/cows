package com.deloladrin.cows;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class Ui
{
    private static final String TAG = Ui.class.getSimpleName();

    private Handler handler;
    private Runner runner;

    private Ui(Runner runner)
    {
        Log.d(TAG, "Starting Ui task ...");
        this.runner = runner;

        Looper looper = Looper.getMainLooper();
        this.handler = new Handler(looper);
        this.handler.post(runner);
    }

    public static Ui run(Runner runner)
    {
        return new Ui(runner);
    }

    public Handler getHandler()
    {
        return this.handler;
    }

    public Runner getRunner()
    {
        return this.runner;
    }

    @FunctionalInterface
    public static interface Runner extends Runnable
    {
    }
}

package com.deloladrin.cows;

import android.util.Log;

public class AsyncVoid
{
    private static final String TAG = AsyncVoid.class.getSimpleName();

    private Runner runner;
    private Result callback;

    private AsyncVoid(Result callback, Runner runner)
    {
        Log.d(TAG, "Starting Async task ...");
        this.runner = runner;
        this.callback = callback;

        new Thread(() ->
        {
            this.runner.run();

            if (this.callback != null)
            {
                this.callback.run();
            }
        })
        .start();
    }

    public static AsyncVoid run(Result callback, Runner runner)
    {
        return new AsyncVoid(callback, runner);
    }

    public Runner getRunner()
    {
        return this.runner;
    }

    public Result getCallback()
    {
        return this.callback;
    }

    @FunctionalInterface
    public static interface Runner
    {
        void run();
    }

    @FunctionalInterface
    public static interface Result
    {
        void run();
    }
}

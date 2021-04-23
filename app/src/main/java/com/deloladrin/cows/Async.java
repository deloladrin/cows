package com.deloladrin.cows;

import android.util.Log;

public class Async<T>
{
    private static final String TAG = Async.class.getSimpleName();

    private Runner<T> runner;
    private Result<T> callback;

    private Async(Result<T> callback, Runner<T> runner)
    {
        Log.d(TAG, "Starting Async task ...");
        this.callback = callback;
        this.runner = runner;

        new Thread(() ->
        {
            T result = this.runner.run();

            if (this.callback != null)
            {
                this.callback.run(result);
            }
        })
        .start();
    }

    public static <T> Async<T> run(Result<T> callback, Runner<T> runner)
    {
        return new Async<>(callback, runner);
    }

    public Runner<T> getRunner()
    {
        return this.runner;
    }

    public Result<T> getCallback()
    {
        return this.callback;
    }

    @FunctionalInterface
    public static interface Runner<T>
    {
        T run();
    }

    @FunctionalInterface
    public static interface Result<T>
    {
        void run(T result);
    }
}

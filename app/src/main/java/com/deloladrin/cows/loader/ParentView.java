package com.deloladrin.cows.loader;

import android.content.Context;
import android.view.View;

public interface ParentView
{
    <T extends View> T findViewById(int id);

    Context getContext();
}

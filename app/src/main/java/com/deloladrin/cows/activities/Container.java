package com.deloladrin.cows.activities;

import android.content.Context;
import android.view.View;

public interface Container
{
    <T extends View> T findViewById(int id);
}

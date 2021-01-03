package com.deloladrin.cows.database;

public interface DatabaseEntry
{
    void insert();
    void update();
    void delete();
    void refresh();
}

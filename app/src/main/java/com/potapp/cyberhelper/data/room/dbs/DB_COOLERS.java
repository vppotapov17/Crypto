package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Cooler;
import com.potapp.cyberhelper.data.room.daos.DAO_COOLERS;

@Database(entities = {Component.class, Cooler.class}, version = 1)
public abstract class DB_COOLERS extends RoomDatabase {
    public abstract DAO_COOLERS getMyDao();
}

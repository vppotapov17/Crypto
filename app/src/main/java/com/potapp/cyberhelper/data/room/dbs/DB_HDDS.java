package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Hdd;
import com.potapp.cyberhelper.data.room.daos.DAO_HDDS;

@Database(entities = {Component.class, Hdd.class}, version = 1)
public abstract class DB_HDDS extends RoomDatabase {
    public abstract DAO_HDDS getMyDao();
}

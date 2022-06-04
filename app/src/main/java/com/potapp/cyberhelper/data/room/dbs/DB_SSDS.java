package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Ssd;
import com.potapp.cyberhelper.data.room.daos.DAO_SSDS;

@Database(entities = {Component.class, Ssd.class}, version = 1)
public abstract class DB_SSDS extends RoomDatabase {
    public abstract DAO_SSDS getMyDao();
}

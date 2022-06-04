package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Mb;
import com.potapp.cyberhelper.data.room.daos.DAO_MOTHERBOARDS;

@Database(entities = {Component.class, Mb.class}, version = 1)
public abstract class DB_MOTHERBOARDS extends RoomDatabase {
    public abstract DAO_MOTHERBOARDS getMyDao();
}

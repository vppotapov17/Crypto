package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Ozu;
import com.potapp.cyberhelper.data.room.daos.DAO_OZUS;

@Database(entities = {Component.class, Ozu.class}, version = 1)
public abstract class DB_OZUS extends RoomDatabase {
    public abstract DAO_OZUS getMyDao();
}

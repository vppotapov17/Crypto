package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.Configuration;
import com.potapp.cyberhelper.data.room.daos.DAO_CONFIGURATIONS;

@Database(entities = {Configuration.class}, version = 1)
public abstract class DB_CONFIGURATIONS extends RoomDatabase {
    public abstract DAO_CONFIGURATIONS getMyDao();
}

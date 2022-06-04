package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Gpu;
import com.potapp.cyberhelper.data.room.daos.DAO_VIDEOCARDS;

@Database(entities = {Component.class, Gpu.class}, version = 1)
public abstract class DB_VIDEOCARDS extends RoomDatabase {
    public abstract DAO_VIDEOCARDS getMyDao();
}

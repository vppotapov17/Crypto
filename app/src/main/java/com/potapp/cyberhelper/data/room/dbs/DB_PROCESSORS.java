package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Cpu;
import com.potapp.cyberhelper.data.room.daos.DAO_PROCESSORS;

@Database(entities = {Component.class, Cpu.class}, version = 1)
public abstract class DB_PROCESSORS extends RoomDatabase {
    public abstract DAO_PROCESSORS getMyDao();
}

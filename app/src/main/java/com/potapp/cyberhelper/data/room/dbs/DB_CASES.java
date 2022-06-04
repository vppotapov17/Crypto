package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Component;
import com.potapp.cyberhelper.data.models.components.Case;
import com.potapp.cyberhelper.data.room.daos.DAO_PCCASES;

@Database(entities = {Component.class, Case.class}, version = 1)
public abstract class DB_CASES extends RoomDatabase {
    public abstract DAO_PCCASES getMyDao();
}

package com.potapp.cyberhelper.data.room.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.data.models.components.Bp;
import com.potapp.cyberhelper.data.room.daos.DAO_BPS;

@Database(entities = {Bp.class}, version = 1)
public abstract class DB_BPS extends RoomDatabase {
    public abstract DAO_BPS getMyDao();
}

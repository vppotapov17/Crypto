package com.potapp.cyberhelper.database.dbs;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.potapp.cyberhelper.database.daos.DAO_BPS;
import com.potapp.cyberhelper.database.daos.DAO_CASES;
import com.potapp.cyberhelper.database.daos.DAO_COOLERS;
import com.potapp.cyberhelper.database.daos.DAO_HDDS;
import com.potapp.cyberhelper.database.daos.DAO_MOTHERBOARDS;
import com.potapp.cyberhelper.database.daos.DAO_OZUS;
import com.potapp.cyberhelper.database.daos.DAO_PROCESSORS;
import com.potapp.cyberhelper.database.daos.DAO_SSDS;
import com.potapp.cyberhelper.database.daos.DAO_VIDEOCARDS;
import com.potapp.cyberhelper.models.components.*;

@Database(entities = {Component.class, Mb.class, Cpu.class, Gpu.class, Ozu.class, Bp.class, Cooler.class, Case.class, Hdd.class, Ssd.class}, version = 1)
public abstract class DB_COMPONENTS extends RoomDatabase {
    public abstract DAO_MOTHERBOARDS getMotherboardsDao();
    public abstract DAO_PROCESSORS getProcessorsDao();
    public abstract DAO_VIDEOCARDS getVideocardsDao();
    public abstract DAO_OZUS getOzusDao();
    public abstract DAO_BPS getBpsDao();
    public abstract DAO_COOLERS getCoolersDao();
    public abstract DAO_CASES getCasesDao();
    public abstract DAO_HDDS getHddsDao();
    public abstract DAO_SSDS getSsdsDao();
}

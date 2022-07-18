package com.potapp.cyberhelper.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.models.Configuration;

import java.util.List;

@Dao
public interface DAO_CONFIGURATIONS {

    @Insert
    void addConfiguration(Configuration configuration);
    @Delete
    void removeConfiguration(Configuration configuration);
    @Update
    void updateConfiguration(Configuration configuration);
    @Query("select * from Configuration")
    List<Configuration> getConfiguration();
}

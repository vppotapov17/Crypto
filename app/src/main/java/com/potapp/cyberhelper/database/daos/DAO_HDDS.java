package com.potapp.cyberhelper.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.models.components.Hdd;

import java.util.List;

@Dao
public interface DAO_HDDS {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addHDD(Hdd HDD);
    @Delete
    void deleteHDD(Hdd HDD);
    @Update
    void updateHDD(Hdd HDD);
    @Query("select * from Hdd")
    List<Hdd> getHDD();
}

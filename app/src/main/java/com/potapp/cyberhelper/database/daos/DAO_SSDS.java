package com.potapp.cyberhelper.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.models.components.Ssd;

import java.util.List;

@Dao
public interface DAO_SSDS {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addSSD(Ssd SSD);
    @Delete
    void deleteSSD(Ssd SSD);
    @Update
    void updateSSD(Ssd SSD);
    @Query("select * from Ssd")
    List<Ssd> getSSD();
}

package com.potapp.cyberhelper.data.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.data.models.components.Cooler;

import java.util.List;

@Dao
public interface DAO_COOLERS {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCOOLER(Cooler COOLER);
    @Delete
    void deleteCOOLER(Cooler COOLER);
    @Update
    void updateCOOLER(Cooler COOLER);
    @Query("select * from Cooler")
    List<Cooler> getCOOLER();
}

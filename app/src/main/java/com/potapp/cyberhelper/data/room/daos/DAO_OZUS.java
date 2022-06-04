package com.potapp.cyberhelper.data.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.data.models.components.Ozu;

import java.util.List;

@Dao
public interface DAO_OZUS {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addOZU(Ozu OZU);
    @Delete
    void deleteOZU(Ozu OZU);
    @Update
    void updateOZU(Ozu OZU);
    @Query("select * from Ozu")
    List<Ozu> getOZU();
}

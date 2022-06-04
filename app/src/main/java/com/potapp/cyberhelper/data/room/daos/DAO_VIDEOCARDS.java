package com.potapp.cyberhelper.data.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.data.models.components.Gpu;

import java.util.List;

@Dao
public interface DAO_VIDEOCARDS {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addGPU(Gpu GPU);
    @Delete
    void deleteGPU(Gpu GPU);
    @Update
    void updateGPU(Gpu GPU);
    @Query("select * from Gpu")
    List<Gpu> getGPU();
}

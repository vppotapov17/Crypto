package com.potapp.cyberhelper.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.models.components.Cpu;

import java.util.List;

@Dao
public interface DAO_PROCESSORS {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCPU(Cpu CPU);
    @Delete
    void deleteCPU(Cpu CPU);
    @Update
    void updateCPU(Cpu CPU);
    @Query("select * from Cpu")
    List<Cpu> getCPU();
}

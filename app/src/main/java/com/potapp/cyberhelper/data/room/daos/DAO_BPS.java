package com.potapp.cyberhelper.data.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.data.models.components.Bp;

import java.util.List;

@Dao
public interface DAO_BPS {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addBP(Bp BP);
    @Delete
    void deleteBP(Bp BP);
    @Update
    void updateBP(Bp BP);
    @Query("select * from Bp")
    List<Bp> getBP();
}

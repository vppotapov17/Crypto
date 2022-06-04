package com.potapp.cyberhelper.data.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.data.models.components.Mb;

import java.util.List;

@Dao
public interface DAO_MOTHERBOARDS {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMB(Mb MB);
    @Delete
    void deleteMB(Mb CPU);
    @Update
    void updateMB(Mb CPU);
    @Query("select * from Mb")
    List<Mb> getMB();
}

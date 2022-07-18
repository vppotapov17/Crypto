package com.potapp.cyberhelper.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.models.components.Case;

import java.util.List;

@Dao
public interface DAO_CASES {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCASE(Case CASE);
    @Delete
    void deleteCASE(Case CASE);
    @Update
    void updateCASE(Case CASE);
    @Query("select * from `Case`")
    List<Case> getCASE();
}

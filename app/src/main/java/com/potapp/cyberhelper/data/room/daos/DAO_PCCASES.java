package com.potapp.cyberhelper.data.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potapp.cyberhelper.data.models.components.Case;

import java.util.List;

@Dao
public interface DAO_PCCASES {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPCCASE(Case PCCASE);
    @Delete
    void deletePCCASE(Case PCCASE);
    @Update
    void updatePCCASE(Case PCCASE);
    @Query("select * from `Case`")
    List<Case> getPCCASE();
}

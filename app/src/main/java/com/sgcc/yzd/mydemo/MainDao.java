package com.sgcc.yzd.mydemo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {
    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    //Delete
    @Delete
    void delete(MainData mainData);

    //Update
//    @Query("Update table_name SET ")

    //Get all data query
    @Query("SELECT * FROM comment_client")
    List<MainData> getAll();



}

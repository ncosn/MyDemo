package com.sgcc.yzd.mydemo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    @Query("SELECT * FROM comment_client Order By time Desc")
    List<MainData> getAll();

    @Query("SELECT * FROM comment_client WHERE time BETWEEN :startDate AND :endDate Order By time Desc")
    List<MainData> getAllTime(String startDate, String endDate);

    @Query("SELECT * FROM COMMENT_CLIENT WHERE if_reply = :ifReply AND type = :type AND time BETWEEN :startDate AND :endDate Order By time Desc")
    List<MainData> getFilterTime(int ifReply, int type, String startDate, String endDate);

    @Query("SELECT * FROM COMMENT_CLIENT WHERE type = :type AND time BETWEEN :startDate AND :endDate Order By time Desc")
    List<MainData> getAllTypeTime(int type, String startDate, String endDate);

    @Query("SELECT * FROM COMMENT_CLIENT WHERE if_reply = 0 AND time BETWEEN :startDate AND :endDate Order By time Desc")
    List<MainData> getReplyAllTime(String startDate,String endDate);

    @Query("SELECT * FROM COMMENT_CLIENT WHERE if_reply = 1 AND time BETWEEN :startDate AND :endDate Order By time Desc")
    List<MainData> getNoReplyAllTime(String startDate,String endDate);

    @Query("Update COMMENT_CLIENT SET if_reply = :ifReply, admin_name = :adminCall, reply = :reply WHERE ID = :sID")
    void update(int sID, int ifReply, String adminCall, String reply);

}

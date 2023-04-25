package com.sgcc.yzd.mydemo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "reply_admin")
public class AdminData implements Serializable {
    //Create id column
    @PrimaryKey(autoGenerate = true)
    private int ID;

}

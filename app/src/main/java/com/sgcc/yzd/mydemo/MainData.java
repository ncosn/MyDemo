package com.sgcc.yzd.mydemo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Define table name
@Entity(tableName = "comment_client")
public class MainData implements Serializable {
    //Create id column
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "sex")
    private int sex;

    @ColumnInfo(name = "tel")
    private String tel;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "msg")
    private String msg;

    @ColumnInfo(name = "time")
    private String time;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


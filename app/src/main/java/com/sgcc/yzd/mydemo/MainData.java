package com.sgcc.yzd.mydemo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

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

    @ColumnInfo(name = "if_reply")
    private int ifReply;

    @ColumnInfo(name = "admin_name")
    private String adminName;

    @ColumnInfo(name = "reply")
    private String reply;

    @ColumnInfo(name = "age", defaultValue = "20")
    private int age;

    @ColumnInfo(name = "service", defaultValue = "5")
    @NotNull
    private int servcie;

    public int getServcie() {
        return servcie;
    }

    public void setServcie(int servcie) {
        this.servcie = servcie;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getIfReply() {
        return ifReply;
    }

    public void setIfReply(int ifReply) {
        this.ifReply = ifReply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


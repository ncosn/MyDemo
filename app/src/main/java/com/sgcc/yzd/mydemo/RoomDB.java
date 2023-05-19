package com.sgcc.yzd.mydemo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;

@Database(entities = {MainData.class}, version=3, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    /**
     * Create database Instance
     */
    public static RoomDB database;
    /**
     * Define database name
     */
    private static String DATABASE_NAME = "database";


    public synchronized static RoomDB getInstance(Context context) {
        //Check condition
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_1_3)
                    .build();
//            删除.fallbackToDestructiveMigration()
        }
        return database;
    }

    /**
     * 数据库版本1升到版本2，留言表添加新的列年龄(age),默认值为20
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE comment_client ADD COLUMN age INTEGER NOT NULL DEFAULT 20");
        }
    };

    /**
     * 数据库版本2升到版本3，留言表添加新的列服务类型(service),默认值为5(其他)
     */
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE comment_client ADD COLUMN service INTEGER NOT NULL DEFAULT 5");
        }
    };

    /**
     * 数据库版本1升到版本3，留言表添加新的列年龄(age),默认值为20，新的列服务类型(service),默认值为5(其他)
     */
    static final Migration MIGRATION_1_3 = new Migration(1, 3) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE comment_client ADD COLUMN age INTEGER NOT NULL DEFAULT 20");
            database.execSQL("ALTER TABLE comment_client ADD COLUMN service INTEGER NOT NULL DEFAULT 5");
        }
    };


    /* Create Dao */

    /**
     * 留言表数据库访问对象
     * @return
     */
    public abstract MainDao mainDao();
}

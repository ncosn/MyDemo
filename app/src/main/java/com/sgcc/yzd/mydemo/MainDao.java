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
    /**
     * 新建留言
     * @param mainData MainData类型留言
     */
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    //Delete
    @Delete
    void delete(MainData mainData);

    /**
     * 获取所有留言
     * @return 返回MainData类型列表
     */
    @Query("SELECT * FROM comment_client Order By time Desc, ID Desc")
    List<MainData> getAll();

    /**
     * 获取留言总数
     * @return int类型总数
     */
    @Query("SELECT COUNT(*) FROM comment_client")
    int getCount();

    /**
     * 从指定位置读取指定数据条数
     * @param limit 读取的数据条数
     * @param offset 从该位置开始读取数据
     * @return 返回MainData类型列表
     */
    @Query("SELECT * FROM comment_client Order By time Desc, ID Desc limit :limit offset :offset ")
    List<MainData> queryPage(int limit,int offset);

    /**
     * 根据条件筛选数据
     * @param ifReply 是否回复，0表示已回复，1表示未回复，2表示全部
     * @param type 留言类型，0表示表扬，1表示建议，2表示投诉
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param service 服务类型
     * @return 返回MainData类型列表
     */
    @Query("SELECT * FROM comment_client WHERE " +
            "(:ifReply = 2 OR if_reply = :ifReply) AND " +
            "(:type = 3 OR type = :type) AND " +
            "(:service = 6 OR service = :service) AND " +
            "time BETWEEN :startDate AND :endDate Order By time Desc, ID Desc")
    List<MainData> getFilter(int ifReply, int type, String startDate, String endDate, int service);

    /**
     * 更新留言回复
     * @param sID 用户ID
     * @param ifReply 是否回复
     * @param adminCall 称呼
     * @param reply 回复
     */
    @Query("Update COMMENT_CLIENT SET if_reply = :ifReply, admin_name = :adminCall, reply = :reply WHERE ID = :sID")
    void update(int sID, int ifReply, String adminCall, String reply);

}

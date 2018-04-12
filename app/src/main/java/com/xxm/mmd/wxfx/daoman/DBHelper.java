package com.xxm.mmd.wxfx.daoman;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xxm.mmd.wxfx.daoman.bean.AccountBean;
import com.xxm.mmd.wxfx.daoman.bean.AutoReplyBean;
import com.xxm.mmd.wxfx.daoman.bean.DriftBottleContentBean;
import com.xxm.mmd.wxfx.daoman.bean.FriendCircleContentBean;
import com.xxm.mmd.wxfx.daoman.bean.MapSearchBean;
import com.xxm.mmd.wxfx.daoman.bean.NearbyPeopleBean;
import com.xxm.mmd.wxfx.daoman.bean.PublicNumBean;
import com.xxm.mmd.wxfx.daoman.bean.SendMessageBean;
import com.xxm.mmd.wxfx.daoman.bean.WhiteListContentBean;

import java.sql.SQLException;



/**
 * Created by Eren on 2017/6/28.
 * <p/>
 * 创建朋友圈数据库
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "earn.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            //创建朋友圈表
            TableUtils.createTable(connectionSource, FriendCircleContentBean.class);
            //创建附近人表
            TableUtils.createTable(connectionSource, NearbyPeopleBean.class);
            //创建漂流瓶表
            TableUtils.createTable(connectionSource, DriftBottleContentBean.class);
            //地图瓶表
            TableUtils.createTable(connectionSource, MapSearchBean.class);
            //账号管理表
            TableUtils.createTable(connectionSource, AccountBean.class);
            //创建白名单表
            TableUtils.createTable(connectionSource, WhiteListContentBean.class);
            //创建自动回复表
            TableUtils.createTable(connectionSource, AutoReplyBean.class);
            //创建一键发消息表
            TableUtils.createTable(connectionSource, SendMessageBean.class);
            //创建公众号表
            TableUtils.createTable(connectionSource, PublicNumBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

//        //地图瓶表
//        try {
//            Log.i("123",DATABASE_VERSION+"");
//            TableUtils.dropTable(connectionSource, MapSearchBean.class, true);
//            TableUtils.createTable(connectionSource, MapSearchBean.class);
//            TableUtils.createTable(connectionSource, MessagesBean.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}

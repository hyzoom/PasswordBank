package com.hazem.passwordbank.common;

import android.content.Context;
import android.util.Log;

import com.hazem.passwordbank.database.DatabaseHelper;
import com.hazem.passwordbank.model.Item;
import com.hazem.passwordbank.model.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 12/19/2015.
 */
public class CommonService {

    private final String commonServiceLog = "CommonService";

    public Dao<Item, Integer> itemDao;
    public Dao<User, Integer> userDao;

    public CommonService(Context context) {
        itemDao = DatabaseHelper.getHelper(context).getItemDao();
        userDao = DatabaseHelper.getHelper(context).getUserDao();
    }


    /**
     * save user to database and create new record for profileImage
     * @param user
     * @return true if user is added successfully, false if not
     */
    public Boolean saveUser(User user) {
        Boolean result = false;

        try {
            Dao.CreateOrUpdateStatus status = userDao.createOrUpdate(user);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(commonServiceLog, e.getMessage());
        }
        return result;
    }


    /**
     * save item to database
     * @param item
     * @return true if category is added successfully, false if not
     */
    public Boolean saveItem(Item item) {
        Boolean result = false;
        try {
            Dao.CreateOrUpdateStatus status = itemDao.createOrUpdate(item);
            if (status.isCreated() || status.isUpdated()) {
                result = true;
            }
        } catch (SQLException e) {
            Log.e(commonServiceLog, e.getMessage());
        }
        return result;
    }


    /*الميثود دي بلوي كبيرة لأن مينفعش نستخدمها خالص بس انا عملها هنا فقط للاختبار وهتتشال بعدين تمام   :D */
    /**
     *
     * @return Items
     */
    public List<Item> getItems() {
        List<Item> list = null;
        try {
            list = itemDao.queryForAll();
        } catch (SQLException e) {
            Log.e(commonServiceLog, e.getMessage());
        }
        return list;
    }


    /**
     *
     * @return User
     */
    public User getUser() {
        User user = null;
        List<User> list = null;
        try {
            list = userDao.queryForAll();
        } catch (SQLException e) {
            Log.e(commonServiceLog, e.getMessage());
        }
        return list.get(0);
    }


    /**
     * get list<Recipe> created by given user
     * @param user
     * @return
     */
    public List<Item> getUserItems(User user) {
        List<Item> items = null;
        if (user == null) {
            return null;
        }

        try {
            items = itemDao.queryForEq("author_id", user.getId());
        } catch (SQLException e) {
            Log.e(commonServiceLog, e.getMessage());
        }
        return items;
    }

}

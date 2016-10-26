package com.smartfarm.db.access;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.smartfarm.db.bean.User;
import com.smartfarm.tools.ExceptionUtils;

public class UserDao {

	public static User add(User user) {
		
		User res = null;
		try {
			
			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			User old = dao.queryBuilder().where().eq("phone", user.getPhone()).queryForFirst();
			
			if(old == null) {
				
				dao.create(user);
				res = user;
			} else {
				
				old.setAddr(user.getAddr());
				old.setName(user.getName());
//				old.setToken(user.getToken());
				old.setSex(user.getSex());
				old.setRefreshToken(user.getRefreshToken());
				
				dao.update(old);
				res = old;
			}
		} catch(SQLException e) {
			ExceptionUtils.report(e);
		}
		
		return res;
	}
	
	public static User findById(int id) {
		
		User user = null;
		try {

			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			
			user = dao.queryForId(id);
		} catch(SQLException e) {
			ExceptionUtils.report(e);
		}

		return user;
	}
	
	public static void update(User user) {
		
		try {

			Dao<User, Integer> dao = DatabaseHelper.getDbHelper().getDao(User.class);
			dao.update(user);
		} catch(SQLException e) {
			ExceptionUtils.report(e);
		}
	}
}

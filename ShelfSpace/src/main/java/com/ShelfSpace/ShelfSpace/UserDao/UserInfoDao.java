package com.ShelfSpace.ShelfSpace.UserDao;

import com.ShelfSpace.ShelfSpace.entites.User;
import com.ShelfSpace.ShelfSpace.model.ResetPassword;

public interface UserInfoDao {

	User addUser(User user);
	boolean resetPassword(ResetPassword resetPassword , User user);

}

package com.mybatis;
import java.util.*;
public interface userservice {
	 public List<user> getAllUser();
	    public void saveUser(user user);
	    public void deleteUserById(Long id);
	    public void updateUser(Long id, String userName, String  passWord);
}

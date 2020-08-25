package com.mybatis;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class userserviceimp implements userservice{
	@Autowired
	private usermapper mapper;
	 public List<user> getAllUser()
	 {
		 return mapper.getAll();
	 }
	    public void saveUser(user user)
	    {
	    	mapper.saveUser(user);
	    }
	    public void deleteUserById(Long id)
	    {
	    	mapper.deleteUserById(id);
	    }
	    public void updateUser(Long id, String userName, String  passWord)
	    {
	    	mapper.updateUser(id, userName, passWord);
	    }
}

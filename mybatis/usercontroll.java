package com.mybatis;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
public class usercontroll {
	@Autowired
    private userservice service ;
	@RequestMapping("/find")          //localhost:8080/find
	public List<user> getAllUser()
	{
		return service.getAllUser();
	}
	 @RequestMapping("/save")       //localhost:8080/save?userName=唐小花&passWord=123456
	    public void saveUser(user user)
	    {
		    service.saveUser(user);
	    }
	@RequestMapping("/delete")     //localhost:8080/delete?id=1
	    public void deleteUserById(Long id)
	    {
		     service.deleteUserById(id);
	    }
	@RequestMapping("/update")    //localhost:8080/id=1&userName=唐花&passWord=1423556
	    public void updateUser(Long id, String userName, String  passWord)
	    {
		   service.updateUser(id, userName, passWord);
	    }
	
}

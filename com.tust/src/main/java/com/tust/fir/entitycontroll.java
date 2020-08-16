package com.tust.fir;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController
@RequestMapping("user")
public class entitycontroll {
	   @Autowired
	   private entityrepo entityr;
	   @RequestMapping("/getAllUser")
	   @ResponseBody
	   public List<entity> findAll() {
	       List<entity> list = new ArrayList<entity>();
	       list = entityr.findAll();
	       return list;
	   }

	   @RequestMapping("/getByUserName")
	   @ResponseBody
	   public entity getByUserName(String userName) {
	       entity e = entityr.findByUserName(userName);
	       return e;
	   }
	   @RequestMapping("/getbyid")
	   @ResponseBody
	   public entity getbyid(long id)
	   {
		   entity e=entityr.findbyid(id);
		   return e;
	   }
	   @RequestMapping("/querylike1")
	   @ResponseBody
	   public List<entity> queryLike1(String name)
	   {
		   List<entity> e=entityr.queryLike1(name);
		   return e;
	   }
	   @RequestMapping("/findbyidandusername")
	   @ResponseBody
	   public entity findbyidandusername(long id, String name)
	   {
		   entity e=entityr.findbyidandusername(id, name);
		   return e;
	   }
	   @RequestMapping("/setfirstname")
	   @ResponseBody
	   @Transactional
	   public int setfirstname(String firstname, String lastname) {
		   int e=entityr.setfirstname(firstname, lastname);
		   return e;
	   }
	   @RequestMapping("/delete")
	   @ResponseBody
	   @Transactional
	   public void delete(long id)
	   {
		   entityr.delete(id);
	   }
	    public String hi(){
	        return "hello";
	    }
}

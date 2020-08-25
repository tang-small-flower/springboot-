package com.mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
@RestController
public class teacher_studentcontroll {
	@Autowired
    teachermapper mapper;
	@Autowired
	studentmapper smapper;
	@RequestMapping("/findall")
	public List<teacher> getteachers()
	{
		return mapper.findall();
	}
	@RequestMapping("findallt")
	public List<student> getstudents()
	{
		return smapper.getalls();
	}
}

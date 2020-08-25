package com.mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
@RestController
public class customer_ordercontroll {
	@Autowired
	private customermapper mapper;
	@RequestMapping("/findo")
	public customer get1()
	{
		return mapper.getAll();
	}
}

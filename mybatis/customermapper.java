package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.mapping.FetchType;
@Mapper
public interface customermapper {
	/*@Select("select * from customer")
	@Results(id="re1",value= {
			@Result(id=true,column = "id",property = "id"),
			@Result(column = "name",property = "name"),
			// many(对多)
			@Result(column = "id",property = "list",
				many=@Many(
					select="com.mybatis.ordermapper.findbyid",  //获取"从表"的数据时,调用的方法来获取
					fetchType= FetchType.EAGER  //使用"EAGER",表示立即加载
				)
			)
	}
			)*/
	public customer getAll();
}

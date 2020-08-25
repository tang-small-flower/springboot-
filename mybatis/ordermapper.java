package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface ordermapper {
	//@Select("select id,name from ord where cid=#{id}")
    public order findbyid(long id);
}

package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
@Mapper
public interface teachermapper {
        public List<teacher> findall(); 
}

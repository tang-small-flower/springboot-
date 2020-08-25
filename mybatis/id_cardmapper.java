package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface id_cardmapper {
     public id_card getbyid(long id);
}

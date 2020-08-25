package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface usermapper {
    public List<user> getAll();
    public void saveUser(user user);
    public void deleteUserById(Long id);
    public void updateUser(@Param("id") Long id,  @Param("userName") String userName,
            @Param("passWord") String passWord);
}
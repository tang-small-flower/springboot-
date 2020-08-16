package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
public interface entityrepo extends JpaRepository<entity, Long> {
    entity findByUserName(String userName);
    @Query("select e from entity e where e.id=?1")
    entity findbyid(long id);
    @Query("select e from entity e where e.userName like %?1%")
    List<entity> queryLike1(String name);
    @Query("select e from entity e where e.id=:id and e.userName=:name")
    entity findbyidandusername(@Param("id") long id,@Param("name") String name);
    @Modifying
    @Query("update entity e set e.passWord = ?1 where e.userName = ?2")
    int setfirstname(String firstname, String lastname);
    @Modifying
    @Query("delete from entity e where e.id = :id")
    void delete(@Param("id") long id);
}

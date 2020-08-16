package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
public interface cat_repository extends JpaRepository<Cat,Integer>{
       public Cat findByCatName(String catname);
}

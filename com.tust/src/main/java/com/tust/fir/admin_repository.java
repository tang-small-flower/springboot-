package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
public interface admin_repository extends JpaRepository<admin,Integer>{
      public admin findByAdminUserName(String username);
}

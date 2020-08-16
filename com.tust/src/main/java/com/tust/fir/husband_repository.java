package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface husband_repository extends JpaRepository<husband,Integer>{
        public husband findByName(String name);
}

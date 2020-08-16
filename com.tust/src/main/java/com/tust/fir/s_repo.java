package com.tust.fir;
import org.springframework.data.jpa.repository.JpaRepository;
public interface s_repo extends JpaRepository<student,Integer>{
             public student findByStudentName(String name);
}

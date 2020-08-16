package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface t_repo extends JpaRepository<teacher,Integer>{
             public teacher findByTeacherName(String name);
             @Query(nativeQuery=true,value="select t.teacher_name,t.teacherid,GROUP_CONCAT(s.student_name) from teacher t join l on t.teacherid=l.teacher_id join student s on l.student_id=s.studentid where t.teacher_name like %?1% group by t.teacherid")
             public List<Object[]> findbyteachernamelike(String name);
}
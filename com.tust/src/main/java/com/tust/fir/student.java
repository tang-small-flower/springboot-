package com.tust.fir;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
@Entity
@Table(name="student")
public class student implements Serializable{
	private static final long serialVersionUID=6L;
	private Integer studentid;//学生ID  
    private String studentName;//学生姓名  
    private Set<teacher> teachers = new HashSet<teacher>();//对应的教师集合  
  
    public student() {  
    }  
  
    public student(String studentName) {  
        this.studentName = studentName;  
    }  
  
    @Id  
    @GeneratedValue  
    public Integer getStudentid() {  
        return studentid;  
    }  
  
    public void setStudentid(Integer studentid) {  
        this.studentid = studentid;  
    }  
  
    @Column(nullable = false, length = 32)  
    public String getStudentName() {  
        return studentName;  
    }  
  
    public void setStudentName(String studentName) {  
        this.studentName = studentName;  
    }  
      
    /* 
     * @ManyToMany 注释表示Student是多对多关系的一边，mappedBy 属性定义了Student 为双向关系的维护端 
     */  
    @ManyToMany(mappedBy = "students")  
    public Set<teacher> getTeachers() {  
        return teachers;  
    }  
  
    public void setTeachers(Set<teacher> teachers) {  
        this.teachers = teachers;  
    }  
    public void deleteteacher(teacher t)
    {
    	if(teachers.contains(t)) teachers.remove(t);
    }
}

package com.tust.fir;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("student")
public class student_teacher_controller {
	@Autowired
	t_repo tre;
	@Autowired
	s_repo sre;
	@RequestMapping("/addteacher")
    @Transactional
    public void addAdmin() {
        student s1 = new student();
        s1.setStudentName("唐小花");
        student s2=new student();
        s2.setStudentName("吴邪");
        teacher t1=new teacher();
        t1.setTeacherName("嫄姐");
        teacher t2=new teacher();
        t2.setTeacherName("老邓头");
        t1.addStudent(s1);
        t2.addStudent(s2);
        t2.addStudent(s1);
        tre.save(t1);
        tre.save(t2);
    }
	/*只删除了教师表中的嫄姐与关系表中与嫄姐联系的关系，student表不受影响*/
	@RequestMapping("/deletest")
	@Transactional
	public void deletestudentandteacher()
	{
		 teacher t=tre.findByTeacherName("嫄姐");
		 for(student s:t.getStudents())
		 {
			 s.deleteteacher(t);
		 }
		 tre.delete(t);
	}
	/*教师表删除，学生表不变，联系表删除与该教师有关的记录*/
	@RequestMapping("/deletestwith")
	@Transactional
	public void deleteteacher()
	{
		 teacher t=tre.findByTeacherName("嫄姐");
		 tre.delete(t);
		 /*Hibernate: select teacher0_.teacherid as teacheri1_5_, teacher0_.teacher_name as teacher_2_5_ from teacher teacher0_ where teacher0_.teacher_name=?
           Hibernate: delete from l where teacher_id=?
           Hibernate: delete from teacher where teacherid=?*/
	}
	/*sre端没有设置级联，且没有从教师端删除学生报错*/
	@RequestMapping("/dstudent")
	@Transactional
	public void deletestudent()
	{
		 student s=sre.findByStudentName("唐小花");
		 sre.delete(s);
	}
	/*在teacher端删除了学生，delete成功，删除了student表唐小花与l表联系表与唐小花有联系的所有内容，而教师表不变*/
	@RequestMapping("/dstudentw")
	@Transactional
	public void deletestudentd()
	{
		 student s=sre.findByStudentName("唐小花");
		 for(teacher t:s.getTeachers())
		 {
			 t.removeStudent(s);
		 }
		 sre.delete(s);
	}
	/*Hibernate: select student0_.studentid as studenti1_2_, student0_.student_name as student_2_2_ from student student0_ where student0_.student_name=?
      Hibernate: select teachers0_.student_id as student_2_1_0_, teachers0_.teacher_id as teacher_1_1_0_, teacher1_.teacherid as teacheri1_5_1_, teacher1_.teacher_name as teacher_2_5_1_ from l teachers0_ inner join teacher teacher1_ on teachers0_.teacher_id=teacher1_.teacherid where teachers0_.student_id=?
      Hibernate: select students0_.teacher_id as teacher_1_1_0_, students0_.student_id as student_2_1_0_, student1_.studentid as studenti1_2_1_, student1_.student_name as student_2_2_1_ from l students0_ inner join student student1_ on students0_.student_id=student1_.studentid where students0_.teacher_id=?
      Hibernate: delete from l where teacher_id=? and student_id=?
      Hibernate: delete from student where studentid=?*/
	@RequestMapping("/findname")
	@Transactional
	public List<Object[]> find(String name)
	{
		 return tre.findbyteachernamelike(name);
	}
}

package com.tust.fir;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController
@RequestMapping("test1")
public class admin_cat_controll {
	@Autowired
	private admin_repository adminre;
	@Autowired
    private cat_repository catre;
	@RequestMapping("/addAdmin")
    @Transactional
    public void addAdmin() {
        admin ad = new admin();
        ad.setAdminUserName("admin");
        ad.setAdminPassWord("123");
        ad.setAdminNickName("yoko");
        Cat cat = new Cat();
        cat.setcatName("Tom");
        Cat cat1 = new Cat();
        cat1.setcatName("Miki");
        ad.getCatList().add(cat);
        ad.getCatList().add(cat1);
        //保存结果：双方都插入了对象，但是维护方（cat）没有外键
        adminre.save(ad);
    }
	    @RequestMapping("/addCat")
	    @Transactional
	    public void addCat() {
	        admin admin = new admin();//被维护方
	        admin.setAdminUserName("admin6");
	        admin.setAdminPassWord("123");
	        admin.setAdminNickName("yo");
	        Cat cat = new Cat();//维护方
	        cat.setcatName("iji");
	        cat.setAdmin(admin);
	        admin.getCatList().add(cat);
	        catre.save(cat);//保存结果：双方都插入了对象，并且维护方有外键
	        cat.setcatName("tang");
	        Cat c=new Cat();
	        c.setcatName("okk");
	        c.setAdmin(admin);
	        catre.save(c);
	    }
	    @RequestMapping("/addAdminCat")
	    @Transactional
	    public void addAdminCat() {
	        admin admin = new admin();
	        admin.setAdminUserName("admin23");
	        admin.setAdminPassWord("123232");
	        admin.setAdminNickName("yoko223");
	        Cat cat = new Cat();
	        cat.setcatName("Tom2223");
	        cat.setAdmin(admin);
	        Cat cat1 = new Cat();
	        cat1.setcatName("Miki2223");
	        cat1.setAdmin(admin);
	        admin.getCatList().add(cat);
	        admin.getCatList().add(cat1);
	        //保存结果：设置了双向关联，即便插入对象是被维护方，2张表都有数据并且维护方（cat）中有外键
	        adminre.save(admin);
	    }
	    @RequestMapping("/deleteCat")
	    @Transactional
	    public void deleteCat() {
	        Cat cat = catre.findByCatName("tang");
	        System.out.println(cat);
	        cat.getAdmin().getCatList().remove(cat);
	        //可以看到在进行上一步设置后，成功有了删除语句delete from tab_cat where catId=?
	        //删除结果：由于未设置级联删除，即便删除的是维护方对象，也只删除了自己，不能删除Admin
	        catre.delete(cat);
	    }
	    @RequestMapping("/deleteAdmin")
	    @Transactional
	    public void deleteAdmin() {
	        admin admin3 = adminre.findByAdminUserName("admin3");
	        System.out.println(admin3);
	        //虽然删除的是被维护端对象，但是设置了级联删除，能顺利删除两张表的数据，这里没有缓存机制作祟
	        adminre.delete(admin3);
	    }
}

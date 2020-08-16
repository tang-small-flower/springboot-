package com.tust.fir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController()
@RequestMapping("getwife")
public class wife_husband_controll {
	@Autowired
	husband_repository husbandre;
	@Autowired
	wife_repository wifere;
	/*维护方设置级联那么只需要将被维护类set到维护类中就可以实现两个表都插入且外键不为null了*/
    @Transactional
    @RequestMapping("/husbandaddpair")
    public void addpair()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    //	me.sethusband(myhusband);
    	myhusband.setwife(me);
    	husbandre.save(myhusband);
    }
    /*被维护方如果设置级联但只将维护类set到被维护类，确实能将两个表都插入，但外键为null*/
    @Transactional
    @RequestMapping("/wifeaddpair")
    public void add()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	me.sethusband(myhusband);
    	wifere.save(me);
    }
    /*被维护端如果设置级联，并且双方实体类都set了对方的属性，无论是不是被维护端，都可以插入两个表且外键不为null*/
    @Transactional
    @RequestMapping("/wifeaddpairwith")
    public void a()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	me.sethusband(myhusband);
    	myhusband.setwife(me);
    	wifere.save(me);
    }
    @Transactional
    @RequestMapping("/wifedelete")
    public void dele()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	wife me=myhusband.getwife();
    	//myhusband.setwife(null);
    	wifere.delete(me);
    	husbandre.delete(myhusband);
    }
    @Transactional
    @RequestMapping("/husbanddelete")
    public void delet()
    {
    	husband myhusband=husbandre.findByName("吴邪");
        wife me=myhusband.getwife();
        me.sethusband(null);
    	husbandre.delete(myhusband);
    }
    @Transactional
    @RequestMapping("/wifedeletewith")
    public void del()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	wife me=myhusband.getwife();
    	myhusband.setwife(null);
    	wifere.delete(me);
    }
}

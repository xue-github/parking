package com.qhit.parking.comtroller;

import com.qhit.parking.entity.Admin;
import com.qhit.parking.service.LoginService;
import com.qhit.parking.utils.RedisUtil;
import com.qhit.parking.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Created by lenovo on 2019/5/25.
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    RedisUtil redisUtil;


    @RequestMapping("/welcome")
    public String welcome(HttpServletRequest request, Model model) {
        String localAddr = request.getLocalAddr();    //取得服务器IP
        int localPort = request.getLocalPort();    //取得服务器端口
        String localName = request.getLocalName();    //取得服务器名称
        String serverName = request.getServerName(); //服务器域名
        Properties props=System.getProperties(); //系统属性
        String javaVersion = props.getProperty("java.version");//操作系统的版本
        String osName = props.getProperty("os.name");//操作系统的名称
        model.addAttribute("localAddr",localAddr);
        model.addAttribute("localPort",localPort);
        model.addAttribute("localName",localName);
        model.addAttribute("serverName",serverName);
        model.addAttribute("osName",osName);
        model.addAttribute("javaVersion",javaVersion);
        return "admin/welcome";
    }

//登录
    @RequestMapping("/login")
    public  String login(){
        return "admin/login";
    }

    @RequestMapping(value ="/checkLogin",method = RequestMethod.POST)
    public String checkLogin(@ModelAttribute("admin") Admin admin,HttpSession session, Model model,@RequestParam("code")String code)throws IOException{
//        Admin admin1 = loginService.Login(admin.getName(), admin.getPassword());
//        if(session.getAttribute("verCode").toString().equalsIgnoreCase(code)){//验证码校验
//                //判断状态是否为1，如果为1，就可以登录，如果不为1，就会提示权限不够
//            if (admin1 != null&&admin1.getStatus()==1){
//                session.setAttribute("LogAdmin",admin1);
//                return "admin/index";
//            }else {
//                model.addAttribute("error","用户名或者密码错误,或者权限不够");
//                return "admin/login";
//    }
//}else {
//        model.addAttribute("error","验证码错误");
//        return "admin/login";
//        }
        //获取输入用户名密码
        boolean  flag = loginService.rediosLogin(admin);
        Map<String,String> map= new HashMap<>();
        map.put("res", "0");
        //获取session
        Admin admin1 = (Admin) session.getAttribute("LogAdmin");
        if (admin1 != null && flag==true) {
            String name = redisUtil.get("name");
            if(admin.getName().equals(name)){
                model.addAttribute("error", "你已经登录，不能重复登录");
            }else if(admin1.getName().equals(admin.getName())){
                model.addAttribute("error", "你已经登录，不能重复登录");
            }else{
                //信息保存在session
                session.setAttribute("LogAdmin", admin);
                //信息保存在redis
                redisUtil.set("id", String.valueOf(admin.getId()));
                redisUtil.set("name", admin.getName());
//                map.put("res", "1");;
                return "admin/index";
            }
        } else if (flag==false){
            model.addAttribute("error", "用户名或密码错误");
        }else if(redisUtil.hasKey(""+admin.getId())){
            String name = redisUtil.get("name");
            if(admin.getName().equals(name)){
                model.addAttribute("error", "你已经登录，不能重复登录");

            }else{
                session.setAttribute("LogAdmin", admin);
                redisUtil.set("id", String.valueOf(admin.getId()));
                redisUtil.set("name", admin.getName());
                map.put("res", "1");;
                return "admin/index";
//                return map;
            }
        }
        else{
            session.setAttribute("LogAdmin", admin);
            redisUtil.set("id", String.valueOf(admin.getId()));
            redisUtil.set("name", admin.getName());
            map.put("res", "1");;
//            return map;
            return "admin/index";
        }
        return "admin/login";
//        return map;

    }
//验证码
    @RequestMapping("/captcha")
    public void  captcha(HttpServletResponse response,HttpServletRequest request,HttpSession session)throws IOException{

            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
           //生成随机字符串
            String verifyCode=VerifyCodeUtils.generateVerifyCode(4);
            //存入session
        session=request.getSession(true);
        session.setAttribute("verCode",verifyCode.toLowerCase());
            //生成图片	 * 输出指定验证码图片流
            int w=135,h=40;
            VerifyCodeUtils.outputImage(w,h,response.getOutputStream(),verifyCode);

    }

    @RequestMapping("/admin-list")
    public String selectAll(Admin admin,HttpSession session, Model model){
        List<Admin> list=loginService.selectAll();
        model.addAttribute("adminList",list);
            return "admin/admin-list";

    }
    @RequestMapping("/findAdminByName")
    public String adminName(@RequestParam("nameTmp") String name,Model model)throws IOException{
        List<Admin> list=loginService.adminName(name);
        if (list!=null){
            model.addAttribute("adminList",list);
            model.addAttribute("nameTmp",name);
        }
        return "admin/admin-list";
    }

    @RequestMapping("/admin-add")
    public  String adminAdd(){
        return "admin/admin-add";
    }

    @RequestMapping("/admin-save")
    public void  insert(Admin admin,HttpServletResponse response)throws IOException{
        Date date=new Date();
        admin.setCreatedate(date);
        String result="";
        Boolean falg=loginService.insert(admin);
        response.getWriter().write(result= falg? "Y" : "N");
    }
    @RequestMapping("/delAdmin")
    public void deleteByPrimaryKey(@RequestParam("adminId")Integer id,HttpServletResponse response)throws IOException{
        String result="";
        Boolean falg=loginService.deleteByPrimaryKey(id);
        response.getWriter().write(result= falg? "Y" : "N");
    }
    @RequestMapping("/admin-load")
    public String selectByPrimaryKey(@RequestParam("adminId")Integer id,Model model)throws IOException{
        Admin admin1=loginService.selectByPrimaryKey(id);
        model.addAttribute("admin",admin1);
        return "admin/change-edit";
    }
    @RequestMapping("/updateAdmin")
    public void updateByPrimaryKeySelective(Admin admin,HttpServletResponse response)throws IOException{
        String result="";

        int i=loginService.updateByPrimaryKey(admin);
        if(i>0){
            result="Y";
        }else {
            result="N";
        }
        response.getWriter().write(result);
    }
    //启用
    @RequestMapping("/startAdmin")
    public void updateStart(@RequestParam("adminId") Integer id,HttpServletResponse response)throws IOException{
        String result="";
        int i=loginService.updateStart(id);
        if(i>0){
            result="Y";
        }else {
            result="N";
        }
        response.getWriter().write(result);
    }
    //停用
    @RequestMapping("/stopAdmin")
    public void updateStop(@RequestParam("adminId") Integer id,HttpServletResponse response)throws IOException{
        String result="";
        int i=loginService.updateStart(id);
        if(i>0){
            result="Y";
        }else {
            result="N";
        }
        response.getWriter().write(result);
    }
    @RequestMapping("/change-info")
    public String changeInfo(@RequestParam("adminId") Integer adminId,Model model){
        Admin admin = loginService.selectByPrimaryKey(adminId);
        model.addAttribute("admin",admin);
        return "admin/change-info";
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public Map<String,String> updatePwd(Admin admin, HttpServletResponse response)throws IOException{
        Boolean result = loginService.updatePwd(admin);
        Map<String,String> map = new HashMap<>();
        if(result){
            map.put("res","0");
        }else {
            map.put("res","1");
        }
        return map;
    }
     //退出
    @RequestMapping("/logout")
    public  String logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        //清除session
        session.removeAttribute("LogAdmin");
        return "admin/login";
    }
}

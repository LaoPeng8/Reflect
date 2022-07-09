package org.pjj.encryption;

import java.util.Scanner;

/**
 * 加密解密的应用场景:
 * 比如: 客户端 注册  最后注册的信息  肯定存到 服务端的数据库中 这样就算注册完成了.
 * 但是,密码在服务端是可以被什么技术人员啊，什么xxx的看见的. 密码这样的敏感信息直接被看见还是不好的,所以通过加密后放在数据库
 *
 * 所以数据库中存放的密码是加密后的密码
 * 那么用户登录时 输入的密码 肯定也需要加密 然后拿现在输入的加密后的密码 和 原本数据库中加密的密码比较 一致就登录成功 否则 登录失败
 *
 * 就是说用户注册时的密码 不会直接存在数据库中 而是存放的加密后的密码  而  用户登录时 也不会直接拿用户输入的密码去和数据库中的 加密的密码比较
 * 而是先将用户登录时 的密码 用同样的方式加密后 再与数据库中的 密码的密码 比较 一致就登录成功 否则 登录失败
 * @author PengJiaJun 
 */
public class SecurityDemo {
    String uName;
    String uPwd;
    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        SecurityDemo demo = new SecurityDemo();
        demo.register();
        boolean login = demo.login();
        if(login){
            System.out.println("登录成功");
        }else{
            System.out.println("登录失败");
        }
    }

    public boolean register(){//注册
        boolean flag = false;
        try{
            System.out.println("请输入用户名:");
            uName = sc.next();

            System.out.println("请输入密码:");
            uPwd = sc.next();//原密码
            uPwd = SecurityUtil.base64Encode(uPwd.getBytes());//加密后的密码
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    public boolean login(){
        boolean flag = false;
        System.out.println("请输入用户名:");
        String loginUName = sc.next();

        System.out.println("请输入密码:");
        String loginUPwd = sc.next();//原密码
        loginUPwd =SecurityUtil.base64Encode(loginUPwd.getBytes());//加密后的密码

        //在用户注册时 数据库中存储的密码就是加密后的密码 所以现在用户输入密码 也需要加密 然后再与数据库中的密码 比较
        if (uName.equals(loginUName) && uPwd.equals(loginUPwd)){
            flag = true;
        }
        return flag;
    }

}

package org.pjj.constructor;

/**
 * 测试 子类构造函数 自动调用[super()] 父类无参构造
 *
 * 经测试: (隐式调用: 系统自己加的 , 显示调用: 自己写的)
 * 子类只会隐式调用父类的 无参构造 也就是 super();  即 不会隐式调用父类的有参构造
 * 子类不管是无参构造还是有参构造, 只要没有显式调用super(); 即会默认隐式调用
 * @author PengJiaJun
 * @Date 2021/12/13 10:30
 */
public class TestSuper {
    public static void main(String[] args) {
        new A(555);
    }
}

class A extends B{
    public A(){
        // super(); //隐式调用 super();
        System.out.println("A类 无参构造...");
    }
    public A(int a){
        // super(); //隐式调用 super();
        super(888);//如已经显示调用 super, 即不会隐式调用super
        System.out.println("A类 有参构造 a = " + a);
    }
}

class B{
    public B(){
        System.out.println("B类 有参构造...");
    }
    public B(int a){
        System.out.println("B类 有参构造 a = " + a);
    }
}

package org.pjj.file.spilt.merge.serializable.entity;

import java.io.Serializable;

public class Person implements Serializable {

    /**
     * 这个UID的作用:    (serialVersionUID在这里就简称UID)
     *  如果不写这个UID,那么将该类的对象写入硬盘后 把该类 修改一下例如将 private String name; 改为public String name;
     *  那么 将该对象从硬盘读入程序(内存)时  就会报错 报错的大概意思就是 写入对象时的UID 和 现在读取对象时的UID不一致
     *  UID不一致 Java就认为 这是两个不同的类 就会报错.
     *
     *  那么UID为什么不一致呢:
     *      因为 我们没有写下面这个属性的话 Java是会给我们创建一个默认的这个属性 而默认的这个属性是根据一些因素来计算出来的
     *      而 当前类就是因数之一  所以类被修改了 那么计算出来的属性就不一致了.
     *
     *  如果我们写这个UID, 那么意思就是 告诉Java不用计算这个UID的值了.我们已经给当前类把这个UID赋值了,而且还是写死了
     *  既然我们把UID的值写死了, 而且Java也不会去计算这个UID的值了, 那么我们修不修改类 UID都是我们写死的那个值,
     *  也就不存在 UID不一致 这种说法了, 也不会报错.
     *
     *  强烈建议 所有可序列化类都显式声明UID值, 原因是计算机默认的UID对类的详细信息具有较高的敏感性,
     *  根据编译器实现的不同可能千差万别, 这样在反序列化过程中可能导致意外的InvalidClassException异常(之前说的UID不一致抛出的异常)
     *  意思就是说 编译器不同 计算出来的UID可能相差很大, 很有可能会导致意外的报错. 建议我们自己写这个UID
     *
     */
    private static final long serialVersionUID = 1234L;
    private String name;
    private int age;

    public Person() {
    }
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

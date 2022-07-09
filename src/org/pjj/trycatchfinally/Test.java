package org.pjj.trycatchfinally;

import java.io.IOException;

/**
 * 测试
 * try {} catch{} finally{} 有return时的执行情况
 *
 * 结论:
 * 1、不管有木有出现异常, finally块中代码都会执行;
 *
 * 2、当try和catch中有return时, finally仍然会执行;
 *
 * 3、finally是在return后面的表达式运算后执行的(此时并没有返回运算后的值, 而是先把要返回的值保存起来,
 *   不管finally中的代码怎么样, 返回的值都不会改变, 任然是之前保存的值), 所以函数返回值是在finally执行前确定的;
 *
 * 4、finally中最好不要包含return, 否则程序会提前退出, 返回值不是try或catch中保存的返回值。
 *
 * 最终结论：任何执行try 或者catch中的return语句之前，都会先执行finally语句，如果finally存在的话。
 * 如果finally中有return语句，那么程序就return了，所以finally中的return是一定会被return的，
 * 编译器把finally中的return实现为一个warning。
 *
 * @author PengJiaJun
 * @Date 2021/12/13 10:39
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println("test01 = " + test01(80));
//        System.out.println("test02 = " + test02(80));
//        System.out.println("test03 = " + test03(80));
//        System.out.println("test04 = " + test04(80));
//        System.out.println("test05 = " + test05(80));
//        System.out.println("test06 = " + test06(80));
        System.out.println("test07 = " + test07());
    }

    /**
     * 情况1: try{} catch(){} finally{} return;
     *             显然程序按顺序执行。
     */
    public static int test01(int result){
        try{
            System.out.println("try...");
        } catch(Exception e){
            System.out.println("catch...");
            e.printStackTrace();
        } finally {
            System.out.println("finally...");
        }

        return result;
    }

    /**
     * 情况2: try{ return; } catch(){} finally{} return;
     *           程序执行try块中return之前(包括return语句中的表达式运算)代码;
     *          再执行finally块，最后执行try中return;
     *          finally块之后的语句return，因为程序在try中已经return所以不再执行.
     * @param result
     * @return
     */
    public static int test02(int result){
        try{
            System.out.println("try...");
            return result;//已经确定返回值的值了, 然后执行finally{} 后, 再执行该return
            //值得注意的是, 不管在 finally 中 对 result + - * / 都不会影响 真正返回result的值, 因为在return之前已经保存好需要返回的值了
            //然后才进入的 finally{}
        } catch(Exception e){
            System.out.println("catch...");
            e.printStackTrace();
        } finally {
            System.out.println("finally...");
            result = result + 1000;//不会生效
        }

        return result;//该return 不会执行, 在try中return了(return 之前 执行了 finally 之后 try中的return执行了)
    }

    /**
     * 情况3:try{} catch(){return;} finally{} return;
     *     程序先执行try, 如果遇到异常执行catch块,
     *     有异常: 则执行catch中return之前 (包括return语句中的表达式运算) 代码, 再执行finally语句中全部代码,
     *     最后执行catch块中return, finally之后也就是4处的代码不再执行.
     *     无异常: 执行完try再finally再return.
     * @param result
     * @return
     */
    public static int test03(int result){
        try{
            System.out.println("try...");
            Thread.sleep(3000);
        } catch(InterruptedException e){
            System.out.println("catch...");
            return result;
        } finally {
            System.out.println("finally...");
        }

        return result;//如果发生了异常 该return不会执行,  因为执行了catch中 return之前的代码, 然后执行 finally{}, 然后执行 catch中的 return;
        //如果没有发生异常: 执行完try再finally再return.
    }

    /**
     * 情况4: try{ return; } catch(){} finally{return;}
     *           程序执行try块中return之前 (包括return语句中的表达式运算) 代码;
     *           再执行finally块, 因为finally块中有return所以提前退出.
     */
    public static int test04(int result){
        try{
            System.out.println("try...");
            return result + 8;//虽然先执行该 return 然后保存 return的值 再执行finally 不管finally中对result进行什么操作, 都改变不了该return返回的result值
            //但是, 由于finally 中有 return , 所以finally中的return执行了, 导致了 程序提前退出, 返回值为 finally 中的 result
            //try 中的 return 就相当于被打断了
        } catch(Exception e){
            System.out.println("catch...");
            e.printStackTrace();
        } finally {
            System.out.println("finally...");
            return result;
        }
    }

    /**
     * 情况5: try{} catch(){return;} finally{return;}
     *           程序执行catch块中return之前（包括return语句中的表达式运算）代码；
     *           再执行finally块，因为finally块中有return所以提前退出。
     * @param result
     * @return
     */
    public static int test05(int result){
        try{
            System.out.println("try...");
            throw new Exception();
        } catch(Exception e){
            System.out.println("catch...");
            return result;//返回 80, 先保存返回 80, 然后执行 finally{} 待 finally{}执行完之后, 在返回catch中return
        } finally {
            System.out.println("finally...");
            return result + 108;//由于 finally 中有 return 所以, 该return直接返回了 (导致 catch 中的 return 被打断了, 没有返回 )
        }

    }


    /**
     * 情况6:try{ return;} catch(){return;} finally{return;}
     *     程序执行try块中return之前(包括return语句中的表达式运算)代码;
     *     有异常: 执行catch块中return之前(包括return语句中的表达式运算)代码;
     *     则再执行 finally块, 因为finally块中有return所以提前退出.
     *     无异常: 则再执行finally块, 因为finally块中有return所以提前退出.
     * @param result
     * @return
     */
    public static int test06(int result){
        try{
            System.out.println("try...");
            return result;
        } catch(Exception e){
            System.out.println("catch...");
            return result + 8;
        } finally {
            System.out.println("finally...");
            return result + 108;
        }
    }

    /**
     * 结果是 2
     * 分析:
     * 	在try语句中, 在执行return语句时, 要返回的结果已经准备好了, 就在此时, 程序转到finally执行了.
     *  在转去之前, try中先把要返回的结果存放到不同于x的局部变量中去, 执行完finally之后, 在从中取出返回结果,
     *  因此, 即使finally中对变量x进行了改变, 但是不会影响返回结果.
     * @return
     */
    public static int test07() {
        int x = 1;
        try {
            x++;
            return x;
        } finally {
            ++x;
        }
    }


}

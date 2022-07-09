package org.pjj.file.spilt.merge;

import java.io.*;
import java.util.Properties;

/**
 * 测试文件拆分
 * @author PengJiaJun
 */
public class SplitFile {
    public static void main(String[] args) {

        //原文件(等待拆分的文件)
        File resFile = new File("E:/IDEA/ideaProject/JavaCommonApplications/data/music.mp3");//写相对路径也行/data/music.mp3 因为在project中，相对路径的根目录是project的根文件夹

        //拆分后的目录(原文件拆分成很多文件,放在该目录)
        File splitDir = new File("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir");

        splitFile(resFile,splitDir);
    }

    //文件拆分            resFile: 原文件(等待拆分的文件)     splitDir: 拆分后的目录(原文件拆分成很多文件,放在该目录)
    public static void splitFile(File resFile,File splitDir){
        if(!resFile.exists()){//判断 需要拆分的文件是否存在, 不存在就报错
            System.out.println("文件不存在:"+resFile.getAbsolutePath());
            return;//结束方法
        }
        if(!splitDir.exists()){//判断  存放拆分后的文件的目录是否存在, 不存在即创建一个目录
            splitDir.mkdirs();
        }

        //拆分思路: 需要一个输入流, n个输出流
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(resFile);
            byte[] buf = new byte[1024*1024];//定义缓冲区为1M , 当缓冲区填满时, 一次性刷出成一个文件
            int len = -1;
            int count = 1;
            while((len=input.read(buf)) != -1){
                output = new FileOutputStream(new File(splitDir,count+".part")); //1.part  2.part  3.part
                output.write(buf,0,len);
                count++;
                output.flush();
                output.close();//关闭当前流, 下次循环又new了一个新的流, 每次向不同的文件输出
            }
            //拆分的时候: 如何将文件名、分割的文件数量保留, 为后续合并做准备
            //再生成一个配置文件count.conf(这个count是上面用来给分割后的文件命名的一个变量) 保存上述信息
            //方式一
            output = new FileOutputStream(new File(splitDir,"conf.properties"));
//            //查询当前操作系统的换行符(因为windows和linux的换行符可能不一样 比如 \n  /n )(这里是举例子而已 并不是真的是\n  /n)
//            String lineSeparator = System.getProperty("line.separator");//lineSeparator就是代表换行符了.
//            output.write(("filename="+resFile.getName()+lineSeparator).getBytes());//保存文件名 并且换个行(将文件名写入到配置文件中)
//            output.write(("partcount="+(count-1)).getBytes());//保存分割文件的数量(count-1)是因为写这个配置文件前count++了的, 现在-1就是这个分割文件的数量不包括 现在这个配置文件
//            output.flush();
//            output.close();

            //方式二: Properties,将内存中的多个属性 以kv对 的形式 写到硬盘中
            Properties prop = new Properties();
            prop.setProperty("filename",resFile.getName());//文件名 (将文件名写入到配置文件中)
            prop.setProperty("partcount",(count-1)+"");//保存分割文件的数量
            //写入硬盘(保存: 持久化)
            prop.store(output,"file configuration...");//参数需要一个流(写入到硬盘的哪里),第二个参数是注释
            output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(input!=null) input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

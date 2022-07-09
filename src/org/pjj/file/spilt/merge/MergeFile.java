package org.pjj.file.spilt.merge;

import java.io.*;
import java.util.*;

/**
 * 测试文件合并
 *
 * @author PengJiaJun
 */
public class MergeFile {
    public static void main(String[] args) {
//        土方法版(最菜)
//        mergeFile();

        //指定拆分后的文件的位置(然后才能将其合并)
        File splitFile = new File("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir");
        //将拆分的文件 合并后的文件位置
        File mergeFile = new File("E:/IDEA/ideaProject/JavaCommonApplications/data/myMusic2.mp3");

//        加强版
//        mergeFilePlus(splitFile,mergeFile);

//        究极版
//        mergeFilePlusPlus(splitFile);

//        软件使用次数: 5   可以免费使用5次, 之后就不能使用了    使用hasRemainingTries()判断是否还能使用
        if(hasRemainingTries()){
            mergeFilePlusPlus(splitFile);
        }else{
            System.out.println("使用次数以到!!!\t请付费: ");
        }


    }


    //文件合并   方式一: 土方法
    //文件合并  合并思路: 需要n个输入流, 一个输出流 (注意顺序)
    public static void mergeFile() {
        //读取多个拆分后的文件(inputs 就是所有输入流的集合)
        List<FileInputStream> inputs = new ArrayList<>();
        InputStream input = null;
        OutputStream output = null;
        try {
            //将多个输入流 弄到一个 list集合inputs中  inputs中就存放了所有需要合并起来的 被拆分的文件 流
            for (int i = 1; i <= 12; i++) {
                inputs.add(new FileInputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/" + i + ".part"));
            }
            //指定合并后的文件输出流
            output = new FileOutputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/myMusic.mp3");

            //将多个 输入流 依次读入内存, 最后再一次性输出到myMusic.mp3
            byte[] buf = new byte[1024 * 1024];

            for (FileInputStream in : inputs) {
                int len = in.read(buf);
                output.write(buf, 0, len);
            }
            output.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputs != null) {
                for (FileInputStream in : inputs) {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //文件合并 方式二 升级版
    public static void mergeFilePlus(File splitDir,File mergeFile) {
        //判空
        if (!splitDir.exists()) {//判断该文件是否存在
            //如文件不存在
            System.out.println("找不到该文件:" + splitDir.getPath());
            return;//停止运行
        } else {
            //文件存在
            if (!splitDir.isDirectory()) {//判断该file执行的文件 是一个文件,还是一个文件夹(目录)
                //如不是目录, 也就是说是个文件
                System.out.println("文件合并需要一个目录,而不是单个文件!!!");
                return;
            }
        }
        //读取多个拆分后的文件(inputs 就是所有输入流的集合)
        List<FileInputStream> inputs = new ArrayList<>();
        InputStream input = null;
        OutputStream output = null;
        SequenceInputStream sin = null;
        //将多个输入流 弄到一个 list集合inputs中  inputs中就存放了所有需要合并起来的 被拆分的文件 流
        try {
            for (int i = 1; i <= 12; i++) {
                inputs.add(new FileInputStream(splitDir.getAbsoluteFile().toString() + "/" + i + ".part"));
            }

            //Collections提供了一个方法 enumeration 可以将一个Collection类型的集合 转成Enumeration类型的集合
            Enumeration<FileInputStream> enumeration = Collections.enumeration(inputs);

            //这个流 可以将很多流 合并在一起 依次读取  但是他需要一个Enumeration类型的集合 (一个 SequenceInputStream代表其他输入流的逻辑连接。它从输入流的有序集合，从第一个直到文件结束达到读取，然后读取第二，等等，直到文件结束在包含输入流上达到)
            sin = new SequenceInputStream(enumeration);

            output = new FileOutputStream(mergeFile);
            byte[] buf = new byte[1024 * 1024];
            int len = -1;
            while ((len = sin.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            output.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (sin != null) {
                    sin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputs != null) {
                for (FileInputStream in : inputs) {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    //文件合并 方式三 究极版
    public static void mergeFilePlusPlus(File splitDir) {
        //判空
        if (!splitDir.exists()) {//判断该文件是否存在
            //如文件不存在
            System.out.println("找不到该文件:" + splitDir.getPath());
            return;//停止运行
        } else {
            //文件存在
            if (!splitDir.isDirectory()) {//判断该file执行的文件 是一个文件,还是一个文件夹(目录)
                //如不是目录, 也就是说是个文件
                System.out.println("文件合并需要一个目录,而不是单个文件!!!");
                return;
            }
        }

        //合并之前读取配置文件
        Properties properties = getProperties("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/conf.properties");
        //读取配置文件中的属性
        String filename = properties.getProperty("filename");
        Integer partcount = Integer.parseInt(properties.getProperty("partcount"));

        //读取多个拆分后的文件(inputs 就是所有输入流的集合)
        List<FileInputStream> inputs = new ArrayList<>();
        InputStream input = null;
        OutputStream output = null;
        SequenceInputStream sin = null;
        //将多个输入流 弄到一个 list集合inputs中  inputs中就存放了所有需要合并起来的 被拆分的文件 流
        try {
            for (int i = 1; i <= partcount; i++) {
                inputs.add(new FileInputStream(splitDir.getAbsoluteFile().toString() + "/" + i + ".part"));
            }

            //Collections提供了一个方法 enumeration 可以将一个Collection类型的集合 转成Enumeration类型的集合
            Enumeration<FileInputStream> enumeration = Collections.enumeration(inputs);

            //这个流 可以将很多流 合并在一起 依次读取  但是他需要一个Enumeration类型的集合 (一个 SequenceInputStream代表其他输入流的逻辑连接。它从输入流的有序集合，从第一个直到文件结束达到读取，然后读取第二，等等，直到文件结束在包含输入流上达到)
            sin = new SequenceInputStream(enumeration);

            //输出流 通过这个流 所有拆分的文件 全部写到这个流对应的文件中    这个文件的路径: 和原本存放被拆分文件同一个目录,只是名字是从 配置文件中读取出来的.
            output = new FileOutputStream(splitDir.getAbsoluteFile().toString()+"/"+filename);
            byte[] buf = new byte[1024 * 1024];
            int len = -1;
            while ((len = sin.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            output.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (sin != null) {
                    sin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputs != null) {
                for (FileInputStream in : inputs) {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public static Properties getProperties(String configFileName){
        Properties properties = new Properties();
        try {
            //通过文件字节流 的形式将配置文件 加载到properties类中
            properties.load(new FileInputStream(configFileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    //判断 是否还有试用
    //思路: 将当前用的次数保存在硬盘, 然后每一次使用时 和 5比较
    public static boolean hasRemainingTries(){
        //读取到配置文件  "E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/conf.properties"
        Properties properties = new Properties();
        try {
            //每使用一次: 1.先获取之前用了几次(3)  2.使用 再将之前次数+1(4)

            //查询本次使用之前 已经用了几次
            properties.load(new FileInputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/conf.properties"));
            String times = properties.getProperty("times");
            if(times == null){//如果是第一次使用 那么配置文件中就没有times属性 所以为 null
                //算数本次(第一次)使用, 使用次数 = 1
                properties.setProperty("times","1");//使用后将times属性写回去配置文件
            }else{
                //times不等于null 说明配置文件中已经有了times属性 也就是不是第一次使用了(因为第一次使用配置文件中会生成times属性)
                Integer timeCount = Integer.parseInt(times);
                timeCount++;
                properties.setProperty("times",timeCount+"");

                if(timeCount>5){
                    return false;
                }
            }
            properties.store(new FileOutputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/conf.properties"),"");

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }



}

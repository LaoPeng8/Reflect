package org.pjj.file.spilt.merge;

import java.io.*;

/**
 * 加入配置文件的读取
 * @author PengJiaJun
 */
public class ConfReader {
    public static void main(String[] args) {
        //读取配置文件
        File confFile = new File("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/conf.properties");
        readConfig(confFile);
    }

    public static void readConfig(File confFile){
        BufferedReader burReader = null;
        try {
            burReader = new BufferedReader(new FileReader(confFile));
            String line = null;
            while((line=burReader.readLine()) != null){
                if(line.startsWith("#")){//判断是否是以#开头的.
                    //如果是#开头的说明是注释 不做任何处理
                    System.out.println(line);
                }else if(line.contains("=")){//判断读取的这一行中是否包含 =
                    //如果包含 就说明这是一行正常的配置文件 例如 name=张三
                    String[] arr = line.split("=");
                    System.out.println(arr[0]+"="+arr[1]);
                }else{
                    //既不是以#开头(注释), 也不是正常的配置文件(没有=号), 那么就 不作任何处理,假装没有读取到这行
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

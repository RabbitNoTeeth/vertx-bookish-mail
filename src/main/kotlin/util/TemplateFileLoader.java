package util;


import com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 邮件模版文件工具类,用于加载邮件模版
 */
public class TemplateFileLoader {

    private TemplateFileLoader(){}

    private static final StringBuilder registerMailTempStr = new StringBuilder();

    private static final StringBuilder subscribeMailTempStr = new StringBuilder();

    static {

        //加载注册邮件模版
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(TemplateFileLoader.class.getResourceAsStream("/templates/RegisterMailTemplate.txt"), Charsets.UTF_8))
        ){
            String line;
            while ((line=reader.readLine())!=null){
                registerMailTempStr.append(line);
            }
        }catch (Exception e){
            throw new RuntimeException("初始化 : 注册邮件模版文件加载失败!");
        }

        //加载订阅邮件模版
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(TemplateFileLoader.class.getResourceAsStream("/templates/SubscribeMailTemplate.txt"), Charsets.UTF_8))
        ){
            String line;
            while ((line=reader.readLine())!=null){
                subscribeMailTempStr.append(line);
            }
        }catch (Exception e){
            throw new RuntimeException("初始化 : 订阅邮件模版文件加载失败!");
        }

    }


    /**
     * 返回注册邮件模版内容
     * @return
     */
    public static String loadRegisterMailTemplate(){
        return registerMailTempStr.toString();
    }

    /**
     * 返回订阅邮件模版内容
     * @return
     */
    public static String loadSubscribeMailTemplate(){
        return subscribeMailTempStr.toString();
    }


}

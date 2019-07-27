package com.example.visit_record;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
//用junit 发送请求
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName JunitTest
 * @description junit 模拟get请求
 * @Author
 * @Date 2018/9/27 19:06
 ******注解说明******
 * ①、@SuppressWarnings 简介：java.lang.SuppressWarnings是J2SE5.0中标准的Annotation之一。可以标注在类、字段、方法、参数、构造方法，以及局部变量上。
 *                   作用：告诉编译器忽略指定的警告，不用在编译完成后出现警告信息。
 *     @SuppressWarnings("unchecked") 告诉编译器忽略 unchecked 警告信息,如使用List,ArrayList等未进行参数化产生的警告信息
 * ②、Get请求对应的是getForObject(String url, Class<T> responseType, Object... uriVariables),它直接接受一个具体的URL，以及返回的类型，
 *       接受一个Class类型的泛型，也就是XXX.class。
 * ③、标准输出流System.out.println和标准错误输出流System.err.println：
 *      System.out.println   能重定向到别的输出流，比如输出到txt文本中， 一般性的输出用out；
 *      System.err.println   只能在屏幕上实现打印，即便重定向也一样，错误使用err。
 **/
public class JunitTest1 {

    //spring的RestTemplate，它实现的是RestOperations接口，里面有好多方法，即get,post,put,delete。
    private RestTemplate template = new RestTemplate();

    @SuppressWarnings("unchecked")
    @Test
    public void test() {



        for (int i = 0; i < 100; i++) {
            //异常处理

                //String url = "http://localhost:" + 8090 +"/find?tag=ns=1002;s=TZ_HD_1106_SW.PV&startTime=2018-08-13 04:09:09&endTime=2018-08-13 23:59:59";
                String url = "http://localhost:" + 8080 + "/1";
                //url 和返回类型写上
                template.getForObject(url,String.class);
                //Junit的静态方法  assertTrue：判断两个值是否相等 ，如果为true，则运行success，反之Failure ； 没有错误提示



        }


    }







//下面做多项成测试.
@Test
    public  void main1() {

        int threadNum=200;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);//计数器

        ExecutorService pool = Executors.newFixedThreadPool(200);










        for (int i = 0; i <threadNum; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {

                    String url = "http://localhost:" + 8080 + "/1";
                    //url 和返回类型写上
                    template.getForObject(url,String.class);//每人连续敲几次.
                    template.getForObject(url,String.class);
                    template.getForObject(url,String.class);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();//如果计数器到0才会开始所有进程.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }








}
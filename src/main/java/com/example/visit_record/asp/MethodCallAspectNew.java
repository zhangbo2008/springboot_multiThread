package com.example.visit_record.asp;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wangxin17
 * @desc
 * @date 2018/8/9 下午8:11
 */

@Aspect
@Component
@EnableAsync

public class MethodCallAspectNew {

    private static Logger LOGGER = LoggerFactory.getLogger(MethodCallAspectNew.class);




//给一个线程池,做多线程,改成定时任务,
ScheduledExecutorService tp = Executors.newScheduledThreadPool(5);

int capacity=3;
ArrayList save=new ArrayList(capacity);
private int flag=0;//表示开始没有人点击时候不用开启10s循环的读写sql任务.

    private ThreadLocal<Date> clickTime = new ThreadLocal<>();
    private ThreadLocal<String> method = new ThreadLocal<>();
    private ThreadLocal<String> uri = new ThreadLocal<>();
    private HashMap map=new HashMap<>();
//用ThreadLocal变量可以线程之间不干扰这个变量的值.
    //每一个线程都做了副本


/*    //因为spring boot 有一些默认的访问地址，这里进行排除
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) && execution(* com.jdcloud.zhike.openapi.web.controller.*.*(..))")*/

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" )

    /*这个是新的切面.
    左右是第一次访问时候开始记录时间,然后时间每到10s就传送数据库一次.
    这次改用map存
    *
    *
    *
    *
    * */






    public void methodCall() {
        System.out.println("methodCall");
    }

    @Before("methodCall()")//这里面只获取请求时间,需要返回请求的路径和时间
    public void doBefore(JoinPoint joinPoint) {
//        System.out.println("doBefore");
        /*startTime.set(new Date());
        Object[] args = joinPoint.getArgs();
        Object operateParamObj = null;
        for (Object arg : args) {
            if (arg instanceof BaseOpenApiRequest) {
                operateParamObj = arg;
                break;
            }
        }
        if (operateParamObj == null) {
            operateParamObj = new BaseOpenApiRequest();
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();//获取request
        String requestId = request.getHeader(CommonConstants.X_JDCLOUD_REQUEST_ID);
        CallerInfo requestNumCallerInfo = null;
        try {
            requestNumCallerInfo = Profiler.registerInfo("com.jdcloud.zhike.openapi.requestNum", false, true);
            Map<String, String> propertyMap = BeanUtils.describe(operateParamObj);
            try {
                if (propertyMap.containsKey("requestId")) {
                    BeanUtils.setProperty(operateParamObj, "requestId", requestId);
                }
            } catch (Exception e) {
                LOGGER.error("MethodCallAspect setRequestId error:" + e.getMessage());
            }
            try {
                if (propertyMap.containsKey("pin")) {
                    String pin = ParamValidatorUtil.getPinFromHeader(request);
                    BeanUtils.setProperty(operateParamObj, "pin", pin);
                }
            } catch (Exception e) {
                LOGGER.error("MethodCallAspect setPin error:" + e.getMessage());
            }
        } catch (Exception e) {
            Profiler.functionError(requestNumCallerInfo);
            LOGGER.error("MethodCallAspect setProperty error:", e);
        } finally {
            Profiler.registerInfoEnd(requestNumCallerInfo);
        }
        StringBuilder builder = new StringBuilder(128);
        builder.append("########invoke doBefore########")
                .append("###uri:").append(request.getRequestURI())
                .append("###requestId:").append(requestId)
                .append("###param:").append(gson.toJson(operateParamObj));
        LOGGER.info(builder.toString());*/
    }

    @AfterReturning(returning = "ret", pointcut = "methodCall()")
    //应该把返回写这里.
    public   void doAfterReturning(Object ret) {//ret是切点return的内容.
//        System.out.println("doAfterReturning");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();//获取request
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();//获取response
//        System.out.println(request.getHeaderNames());
//        Enumeration tmp=request.getHeaderNames();
//        String tmp2=request.getMethod();
//        String tmp3=request.getRequestURI();
//        Object tmp2=request.getRequestURL()
//        System.out.println(tmp2);
//        System.out.println(tmp3);
        clickTime.set(new Date());
//        System.out.println(clickTime.toString());
//       System.out.println(clickTime.get().toString());
        method.set(request.getMethod());
        uri.set(request.getRequestURI());
        //把这3个打包发送给mysql就行了

        //用一个数组装,里面元素=2就发送写入请求.

        String msg=clickTime.get().toString()+method+uri; //需要先get,结果精确到秒.
        //启动一个线程去写这个msg到数组

        /*
        * 所以这个
        *
        * */















        tp.schedule(new Runnable() {




//需要2个线程,一个是不停的接收信息写入map
            //一个是定时线程,到10s就写入sql,清空map,对数据库用事务来操作.
            public    void  run() {
                    synchronized (map.getClass()){

                        if ( flag==0&&map.size()>0)
                        {   flag=1;
                            tp.scheduleAtFixedRate(

                                new Runnable() {

                                    @Override
                                    public void run() {
                                        System.out.println("每2秒执行一次,写入sql");
                                        //打印第一个value的大小
                                        System.out.println(map.values());
                                        map.clear();
                                        System.out.println("清空map然后map大小"+map.size());


                                    }
                                }, 0, 2, TimeUnit.SECONDS);
                        }

                        //扔map里面
                        if (!map.containsKey(msg))

                        {map.put(msg,1);
                        System.out.println("加入一条后:map大小"+map.size());}
                        else{
                            map.put(msg,(int)map.get(msg)+1);
                            System.out.println("加入一条后:map大小"+map.size());
                        }
                    }
            }
        } ,0,TimeUnit.SECONDS  );




















    }


}

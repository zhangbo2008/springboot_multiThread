package com.example.visit_record.asp;


import lombok.Synchronized;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author wangxin17
 * @desc
 * @date 2018/8/9 下午8:11
 */

@Aspect
@Component
@EnableAsync

public class MethodCallAspect {

    private static Logger LOGGER = LoggerFactory.getLogger(MethodCallAspect.class);
//给一个线程池,做多线程
ExecutorService tp = Executors.newFixedThreadPool(5);

int capacity=3;
ArrayList save=new ArrayList(capacity);


    private ThreadLocal<Date> clickTime = new ThreadLocal<>();
    private ThreadLocal<String> method = new ThreadLocal<>();
    private ThreadLocal<String> uri = new ThreadLocal<>();
//用ThreadLocal变量可以线程之间不干扰这个变量的值.
    //每一个线程都做了副本


/*    //因为spring boot 有一些默认的访问地址，这里进行排除
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) && execution(* com.jdcloud.zhike.openapi.web.controller.*.*(..))")*/

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" )
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
        method.set(request.getMethod());
        uri.set(request.getRequestURI());
        //把这3个打包发送给mysql就行了

        //用一个数组装,里面元素=2就发送写入请求.

        String msg=clickTime.toString()+method+uri;
        //启动一个线程去写这个msg到数组


        tp.submit(new Runnable() {
            //job函数

            public    void  run() {
                synchronized (System.class){;//把需要唯一访问的操作括起来.
                if (save.size()>=capacity){//这个地方如果写等号就会出现数组无线添加的bug么?
//                    莫名其妙又好了
                    //xieru sql,清空save
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("写入 sql后size:"+save.size());
                    save.clear();
                    System.out.println("清空save后size:"+save.size());
                    save.add(msg);
                }
                else

                        //写入save
                {
                    save.add(msg);
                }
                    System.out.println("xieru save当前size:"+save.size());
//                    System.out.println(save.toString());

                    //还需要关闭服务的时候,把save中剩余的写入sql






            }



        }}
        );
















//        每一次就返回tmp5就得了.










     /*   String requestId = request.getHeader(CommonConstants.X_JDCLOUD_REQUEST_ID);
        String pin = ParamValidatorUtil.getPinFromHeader(request);
        if (ret instanceof OpenApiResponse) {
            OpenApiResponse openApiResponse = (OpenApiResponse) ret;
            openApiResponse.setRequestId(requestId);
            OpenApiError openApiError = openApiResponse.getError();
            if (openApiError != null && openApiError.getCode() != null) {
                response.setStatus(openApiError.getCode());//如果有错误就写入response
            } else {
                if (openApiResponse.getResult() != null
                        && openApiResponse.getResult().isStatus()) {
                    //成功了才计数 reqNumService是计数函数
                    reqNumService.increase(pin, request.getRequestURI(), startTime.get(), new Date(), openApiResponse.getCustomReqNum());
                }
            }
        }*/



    }


}

package com.example.visit_record;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;
@Data
public class NumAction {
    private  Integer num1=0;
    private  Integer num2=0;
    private  Integer num3=0;
    private  Integer num4=0;
    private  Integer num5=0;
    private  volatile Integer num6=0;
    private  AtomicInteger  num7=new AtomicInteger(0);//原子整数

    public NumAction() {
    }
    
//省略get/set方法
public void Initializers(NumAction lk){
    lk.num1=0;//不行
    lk.num2=0;
    lk.num3=0;
    lk.num4=0;
    lk.num5=0;
    lk.num6=0;  //不行
    lk.num7=new AtomicInteger(0);  //可以并发

}
    //----------------------------------------------------------重点部分------------------------------------------------------
    public  void   addMeethod1(){
        num1++;
    } //不型
    public synchronized void   addMeethod2(){ //可以
        num2++;
    }

    public  void   addMeethod3(){ //可以
        synchronized(this){
            num3++;
        }
    }

    public  void   addMeethod4(){//不行,锁的还不够,只锁一个值不行
        synchronized(num4){
            num4++;
        }
    }

    public  void   addMeethod5(){
        synchronized(NumAction.class){ //锁类也可以
            num5++;
        }
    }

    public  void   addMeethod6(){     //不行
        num6++;
    }

    public  void   addMeethod7(){
        num7.incrementAndGet();
    } //原子可以
    public void Add100() {
        for (int i = 0; i < 1000; i++) {
            addMeethod1();
            addMeethod2();
            addMeethod3();
            addMeethod4();
            addMeethod5();
            addMeethod6();
            addMeethod7();
        }
    }
}
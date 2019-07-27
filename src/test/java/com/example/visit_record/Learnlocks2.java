package com.example.visit_record;


public class Learnlocks2 extends Thread{
    private static NumAction numaction=new NumAction();
    public void run() {
        numaction.Add100();
    }

    public static void testRun(){
        Learnlocks2 l1 = new Learnlocks2();
        Learnlocks2 l2 = new Learnlocks2();
        Learnlocks2 l3 = new Learnlocks2();
        new Thread(l1).start();
        new Thread(l2).start();
        new Thread(l3).start();
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(numaction.getNum1()+","+numaction.getNum2()+","+numaction.getNum3()+","+numaction.getNum4()+","+numaction.getNum5()+","+numaction.getNum6()+","+numaction.getNum7());

    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            testRun();
            numaction.Initializers(numaction);
        }
    }
}
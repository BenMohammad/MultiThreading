package com.benmohammad.multithreading.demo.vis;

public class VisibilityDemonstration {

    private static int sCount = 0;

    public static void main(String[] args) {

    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            int localValue = -1;
            while(true) {
                if(localValue != sCount) {
                    System.out.println("Consumer detected count: " + sCount);
                    localValue = sCount;
                }

                if(localValue > 5) {
                    break;
                }
            }
            System.out.println("Consumer terminating");
        }
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            while(sCount < 5) {
                int localValue = sCount;
                localValue++;
                System.out.println("Producer incrementing count to: " + sCount);
                sCount = localValue;
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e){
                    return;
                }
            }

            System.out.println("Producer terminating");
        }
    }
}

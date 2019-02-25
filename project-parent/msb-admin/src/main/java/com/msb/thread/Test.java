package com.msb.thread;

import com.msb.query.DictQueryParam;
import com.personal.common.utils.base.Qp2EwUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Test {
//    public static void main(String[] args) throws InterruptedException {
//        Thread t1 = new Thread() {
//            @Override
//            public void run() {
//                //while在try中，通过异常中断就可以退出run循环
//                try {
//                    while (true) {
//                        System.out.println("not be interrupted value :"+this.isInterrupted());
//                        //当前线程处于阻塞状态，异常必须捕捉处理，无法往外抛出
//                        TimeUnit.SECONDS.sleep(2);
//                    }
//                } catch (InterruptedException e) {
//                    System.out.println("Interrupted When Sleep");
//                    boolean interrupt = this.isInterrupted();
//                    // 中断状态被复位
//                    System.out.println("interrupt:"+interrupt);
//                }
//            }
//        };
//        t1.start();
//        TimeUnit.SECONDS.sleep(2);
//        //中断处于阻塞状态的线程
//        t1.interrupt();
//
//        /**
//         * 输出结果:
//         Interruted When Sleep
//         interrupt:false
//         */
//    }

    public static void main(String[] args) {
        DictQueryParam param = new DictQueryParam();
        param.setPid("test-pid");
        Map<String,String> test = new HashMap<>();
        param.setMaps(test);
        Qp2EwUtils.qp2ew(param);
    }
}

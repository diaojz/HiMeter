package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 压测工具类
 */
public class ConcurrentTestUtil {

    /**
     * 压力测试方法
     *
     * @param url           需要压测的网址URL
     * @param requestNum    压测的次数
     * @param concurrentNum 并发次数
     * @throws Exception
     */
    public static void doRequest(String url,
                                 int requestNum,
                                 int concurrentNum) throws Exception {
        //创建一个线程池
        ExecutorService executor = Executors.newFixedThreadPool(concurrentNum);
        //存放请求响应时间的列表
        List<Long> timeList = new ArrayList<>();
        //记录开始时间（方便计算总时长）
        long startTime = System.currentTimeMillis();
        //计时器
        CountDownLatch latch = new CountDownLatch(requestNum);
        //发起网络请求
        HttpUtil.sendGet(url, null);

        //循环发起请求
        for (int i = 0; i < requestNum; i++) {
            executor.execute(() -> {
                long start = System.currentTimeMillis();
                HttpUtil.sendGet(url, null);
                latch.countDown();
                timeList.add(System.currentTimeMillis() - start);
            });
        }
        latch.await();
        executor.shutdown();
        //对数据进行排序
        Collections.sort(timeList);
        //计算请求总时长
        long totalCostTime = System.currentTimeMillis() - startTime;

        //打印
        System.out.println("压测的网址: " + url);
        System.out.println("压测总次数: " + requestNum);
        System.out.println("并发线程数: " + concurrentNum);
        System.out.println("总花费时间（毫秒）: " + totalCostTime);
        System.out.println("平均花费时间（毫秒）: " + ((double) totalCostTime / requestNum));
        System.out.println("95%时间（毫秒）: " + timeList.get(95 * requestNum / 100));
        System.out.println("---------------------------");
        for (int i = 0; i < requestNum; i++) {
            long time = timeList.get(i);
            System.out.println("第" + i + "次： " + time + "毫秒");
        }
        System.out.println("---------------------------");
    }
}

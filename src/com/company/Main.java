package com.company;

public class Main {

    public static void main(String[] args) {
        try {
            ConcurrentTestUtil.doRequest("https://www.jd.com", 100, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

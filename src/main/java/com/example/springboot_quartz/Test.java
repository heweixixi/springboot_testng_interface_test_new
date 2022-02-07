package com.example.springboot_quartz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Test {

    static final int LENGTH = 0x8FFFFFF;//128M
    static FileChannel fc;


  /* private static void symmetricScramble(CharBuffer buffer){
       while (buffer.hasRemaining()){
           buffer.mark();
           System.out.println(buffer.position());
           char c = buffer.get();
           System.out.println(c);
           char c1 = buffer.get();
           buffer.reset();
           System.out.println(buffer.position());
           buffer.put(c1).put(c);
           buffer.position(buffer.limit());
       }
   }*/

    private static class LockAndModify extends Thread {
        private ByteBuffer buffer;
        private int start;
        private int end;

        LockAndModify(ByteBuffer bbf, int start, int end) {
            this.start = start;
            this.end = end;
            bbf.limit(end);
            bbf.position(start);
            this.buffer = bbf;
            start();
        }

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) throws IOException {
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
//        CharBuffer charBuffer = byteBuffer.asCharBuffer();
//        charBuffer.put("UsingBuffers");
//        charBuffer.flip();
//        charBuffer.rewind();
//        System.out.println(charBuffer.position());
//        symmetricScramble(charBuffer);
//        System.out.println(charBuffer.rewind());
//        symmetricScramble(charBuffer);
//        System.out.println(charBuffer.rewind());
//        int num = 6234;
//        System.out.println(num%10);
//        System.out.println(num/10%10);
//        System.out.println(num/1000%10);
        /*FutureTask<Integer> task = new FutureTask<>(() -> {
            int i = 0;
            for (; i < 50; i++) {
                System.out.println(Thread.currentThread().getName() + "的线程执行体内的循环变量i的值为：" + i);
            }
            return i;
        });*/
        Random random = new Random(47);
        System.out.println(random.nextInt(10));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(new Test());
    }
}
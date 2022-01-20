package com.example.lib;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/*
FileDescriptor��"�ļ�������".

FileDescriptor���Ա�������ʾ���ŵ��ļ�,���ŵ��׽��ֵ�.

��FileDescriptor��ʾ�ļ���˵,��FileDescriptor��ʾĳ�ļ�ʱ,���ǿ���ͨ�׵Ľ�FileDescriptor���ɸ��ļ�.
����,���ǲ���ֱ��ͨ��FIleDescriptor�Ը��ļ����в���;����Ҫͨ��FIleDescriptor�Ը��ļ����в���,
����Ҫ����FileDescriptor��Ӧ��FileOutputStream,�ٶ��ļ����в���.

in,out,err�Ľ���
 1.in:��׼����(����)��������
 2.out:��׼���(��Ļ)��������
 3.err:��׼�������(��Ļ)��������
����������ԭ����÷�������,����ͨ��out������������о�

out�����ú�ԭ��
outʱ��׼���(��Ļ)��������,��������ʲô������?

���ǿ���ͨ�����,out �ʹ����˱�׼���(��Ļ).������Ҫ�����Ϣ����Ļ,�ȿ���ͨ��out������,����,out ��û���ṩ�����Ϣ����Ļ�Ľӿ�,��ô����?

�ܼ�,���Ǵ���Out��Ӧ��"���������",Ȼ��ͨ��"�����"��write()�ȽӿھͿ��������Ϣ����Ļ��ȥ��.

 */
public class Test {
    // �ο���https://www.jianshu.com/p/57e292378415
    // �ο���https://www.cnblogs.com/zhangj-ymm/p/9943597.html
    // �ο���https://cloud.tencent.com/developer/article/1513524
    private static final String mFileName = "D:\\\\mNDK\\\\NDK_VIP\\\\ndk52_Serial\\\\project\\\\ddd.txt";

    public static void main(String[] args) throws IOException {
        System.out.println(2);
        // testStandFD();
        // testWrite();
        testRead();
    }

    // �ó����Ч�� �ȼ��� System.out.println(OutText);
    public static void testStandFD() throws IOException {
        // TODO ��ʽһ
        /*
        �������:AAABBBCCC
        Ϊ�˷������ǵĲ���,java���Ѿ�Ϊ���Ƿ�װ����������Ļ�������Ϣ�Ľӿ�:ͨ��System.out
        �������Ĵ����ͬ��:System.out.print("AAABBBCCC").
         */
        FileOutputStream out1 = new FileOutputStream(FileDescriptor.out);
        out1.write("AAABBBCCC".getBytes());
        out1.close();

        // TODO ��ʽ��
        FileOutputStream out2 = new FileOutputStream(FileDescriptor.out);
        PrintStream print = new PrintStream(out2);
        print.write("GGGGGG".getBytes());
        print.close();
    }

    // FileDescriptorд�뵽�ļ�ʾ������
    private static void testWrite() throws IOException {
        // �½�file��ӦFileOutPutStream����
        FileOutputStream fout = new FileOutputStream(mFileName);
        // ���file��Ӧ��FileDescripto����
        FileDescriptor fd = fout.getFD();
        // ����FileDescriptor����fileOutPutStream����
        fout.write("Derry888".getBytes());
        fout.close();
    }

    // FileDescriptor��ȡ�ļ�����ʾ������
    private static void testRead() throws IOException {
        // �½��ļ���file.txt����Ӧ��FileInputStream����
        FileInputStream fis = new FileInputStream(mFileName);
        // ��ȡ�ļ���file.txt����Ӧ�ġ��ļ���������
        FileDescriptor fd = fis.getFD();
        // ���ݡ��ļ���������������FileInputStream������
        FileInputStream fiss = new FileInputStream(fd);
        System.out.println("in1.read():" + (char) fiss.read());
        System.out.println("in2.read():" + (char) fiss.read());
        System.out.println("in3.read():" + (char) fiss.read());
        fiss.close();
    }
}

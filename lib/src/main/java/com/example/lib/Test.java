package com.example.lib;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/*
FileDescriptor是"文件描述符".

FileDescriptor可以被用来表示开放的文件,开放的套接字等.

当FileDescriptor表示文件来说,当FileDescriptor表示某文件时,我们可以通俗的将FileDescriptor看成该文件.
但是,我们不能直接通过FIleDescriptor对该文件进行操作;若需要通过FIleDescriptor对该文件进行操作,
则需要创建FileDescriptor对应的FileOutputStream,再对文件进行操作.

in,out,err的介绍
 1.in:标准输入(键盘)的描述符
 2.out:标准输出(屏幕)的描述符
 3.err:标准错误输出(屏幕)的描述符
他们三个的原理和用法都类似,下面通过out来进行深入的研究

out的作用和原理
out时标准输出(屏幕)的描述符,但是他有什么作用呢?

我们可以通俗理解,out 就代表了标准输出(屏幕).如我们要输出信息到屏幕,既可以通过out来操作,但是,out 有没有提供输出信息到屏幕的接口,怎么办呢?

很简单,我们创建Out对应的"输出流对象",然后通过"输出流"的write()等接口就可以输出信息到屏幕上去了.

 */
public class Test {
    // 参考：https://www.jianshu.com/p/57e292378415
    // 参考：https://www.cnblogs.com/zhangj-ymm/p/9943597.html
    // 参考：https://cloud.tencent.com/developer/article/1513524
    private static final String mFileName = "D:\\\\mNDK\\\\NDK_VIP\\\\ndk52_Serial\\\\project\\\\ddd.txt";

    public static void main(String[] args) throws IOException {
        System.out.println(2);
        // testStandFD();
        // testWrite();
        testRead();
    }

    // 该程序的效果 等价于 System.out.println(OutText);
    public static void testStandFD() throws IOException {
        // TODO 方式一
        /*
        程序输出:AAABBBCCC
        为了方便我们的操作,java早已经为我们封装好了能再屏幕上输出信息的接口:通过System.out
        因此上面的代码等同于:System.out.print("AAABBBCCC").
         */
        FileOutputStream out1 = new FileOutputStream(FileDescriptor.out);
        out1.write("AAABBBCCC".getBytes());
        out1.close();

        // TODO 方式二
        FileOutputStream out2 = new FileOutputStream(FileDescriptor.out);
        PrintStream print = new PrintStream(out2);
        print.write("GGGGGG".getBytes());
        print.close();
    }

    // FileDescriptor写入到文件示例程序
    private static void testWrite() throws IOException {
        // 新建file对应FileOutPutStream对象
        FileOutputStream fout = new FileOutputStream(mFileName);
        // 获得file对应的FileDescripto对象
        FileDescriptor fd = fout.getFD();
        // 根据FileDescriptor创建fileOutPutStream对象
        fout.write("Derry888".getBytes());
        fout.close();
    }

    // FileDescriptor读取文件内容示例程序
    private static void testRead() throws IOException {
        // 新建文件“file.txt”对应的FileInputStream对象
        FileInputStream fis = new FileInputStream(mFileName);
        // 获取文件“file.txt”对应的“文件描述符”
        FileDescriptor fd = fis.getFD();
        // 根据“文件描述符”创建“FileInputStream”对象
        FileInputStream fiss = new FileInputStream(fd);
        System.out.println("in1.read():" + (char) fiss.read());
        System.out.println("in2.read():" + (char) fiss.read());
        System.out.println("in3.read():" + (char) fiss.read());
        fiss.close();
    }
}

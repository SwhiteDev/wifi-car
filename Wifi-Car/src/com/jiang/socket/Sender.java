package com.jiang.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender {

	
	public static void input(Socket socket) throws IOException{
        //获取输入流，并读取服务器端的响应信息
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String info = null;
        while((info = br.readLine()) != null){
            System.out.println("我是客户端，服务器说："+info);
        }
        //关闭资源
        br.close();
        is.close();
	}
	
	public static void output(Socket socket, String message) throws IOException{

        //获取输出流，向服务器端发送信息
        OutputStream os = socket.getOutputStream();//字节输出流
        PrintWriter pw = new PrintWriter(os);//将输出流包装为打印流
        pw.write(message);
        pw.flush();
        socket.shutdownOutput();//关闭输出流
        pw.close();
        os.close();
	}
}

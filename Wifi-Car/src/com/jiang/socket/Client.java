package com.jiang.socket;

import java.net.Socket;

public class Client {
	
	private static Socket socket = null;
	
	/**
	 * 建立连接
	 * @param address ip地址
	 * @param port 端口
	 * @throws Exception
	 */
	public static Socket connect(String address, int port) throws Exception{
        //1.创建客户端Socket，指定服务器地址和端口
        socket = new Socket(address, port);
        return socket;
	}
	
	
	/**
	 * 断开连接
	 * @throws Exception
	 */
	public static void disConnect() throws Exception{
        //关闭Socket
        socket .close();
        System.out.println("断开连接");
	}
}

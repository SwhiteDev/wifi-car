package com.jiang.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	 private int port = 2000;
	 private ServerSocket serverSocket;

	 public Server() throws Exception{
		 serverSocket = new ServerSocket(port, 5);
		 System.out.println("服务器启动!");
	 }
	 
	 public void service(){
		 while(true){
			 Socket socket = null;
			 try {
				 socket = serverSocket.accept();
				 System.out.println("New connection accepted "+
						 socket.getInetAddress()+":"+socket.getPort());
				 InputStream is = socket.getInputStream();
				 BufferedReader br = new BufferedReader(new InputStreamReader(is));
				 String info = null;
				 while((info = br.readLine()) != null){
					 OutputStream os = socket.getOutputStream();
					 PrintWriter pw = new PrintWriter(os);
					 pw.write("客户端说："+info);
					 pw.close();
					 os.close();
	            }
			 } catch (IOException e) {
				 e.printStackTrace();
			 }finally{
				 if(socket != null){
					 try {
						 socket.close();
					 } catch (IOException e) {
						 e.printStackTrace();
					 }
				 }
			 }
		 }
	}

		 
	public static void main(String[] args) throws Exception{
		 Server server = new Server();
		 Thread.sleep(60);
		 server.service();
	}
}

package com.jiang.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.jiang.socket.Client;
import com.jiang.socket.Sender;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MyController implements Initializable{


	@FXML
	private TextField address;
	@FXML
	private TextField port;
	@FXML
	private TextField speed;
	@FXML
	private Label message;
	
//	Socket socket = null;
	
//	Pattern speedPattern = "^[0-9]$";

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   // When user click on myButton
   // this method will be called.
   public void showDateTime(ActionEvent event) {
       System.out.println("Button Clicked!");

	   Date now= new Date();
	
	   DateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
	   String dateTimeString = df.format(now);
	    // Show in VIEW
	   address.setText(dateTimeString);

   }
   
   /**
    * Connect按钮
    * @param event
    */
   public void connect(ActionEvent event){
	   String address = this.address.getText();
	   int port = Integer.parseInt(this.port.getText());
	   try {
		   Socket socket = Client.connect(address, port);
		   message.setText("连接成功");
		   System.out.println("ok");
		   Sender.output(socket, "sb");
		   Thread.sleep(3000);
		   Sender.input(socket);
	   } catch (Exception e) {
		   e.printStackTrace();
		   message.setText("连接失败");
	   }
	   this.address.setEditable(false);
	   this.port.setEditable(false);
   }
   
   /**
    * Disconnect按钮
    * @param event
    */
   public void disConnect(ActionEvent event){
	   try {
		   Client.disConnect();
		   message.setText("已断开连接");
	   } catch (Exception e) {
		   e.printStackTrace();
		   message.setText("断开连接失败");
	   }
	   address.setEditable(true);
	   port.setEditable(true);
   }
   
   public void move(Socket socket){
	   int speed = Integer.parseInt(this.speed.getText());
	   try {
		Sender.output(socket, speed+"");
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("输出失败");
	}
   }
   
   /**
    * Motor Control "+"按钮  ：增加速度
    * @param event
    */
   public void clickPlus(ActionEvent event){
	   if(Integer.parseInt(speed.getText()) >= 5){
		   message.setText("已达最高速度");
	   }else{
		   speed.setText(Integer.parseInt(speed.getText())+1+"");
	   }
   }
   
   
   /**
    * Motor Control "-"按钮
    * @param event
    */
   public void clickMinus(ActionEvent event){
	   if(Integer.parseInt(speed.getText()) <= 0){
		   message.setText("速度不能为负"); 
	   }else{
		   speed.setText(Integer.parseInt(speed.getText())-1+"");
	   }
   }
}

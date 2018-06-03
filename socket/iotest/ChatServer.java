package iotest;

import java.net.*;
import java.util.*;
import java.io.*;
public class ChatServer {
	private static  ArrayList<Socket> list=new ArrayList<Socket>();
	public static void main(String[] args){
		ChatServer cs=new ChatServer();
		try {
			ServerSocket serversocket=new ServerSocket(8888);
			System.out.println("我是服务端(*^__^*) ,正在等待连接……");
			/*
             *  启动发消息的线程
             *  因为是一个服务端对应多个客户端，因此发送消息的线程只需要一个，
             *  不需要随着客户端的个数增加而增加 
             */
			//表示可以连接多客户端
			while(true) {
				Socket socket=serversocket.accept();
				list.add(socket);
				for(int i=0;i<list.size();i++)
					System.out.println(i);
				System.out.println("客户端"+socket.getInetAddress()+"连接成功");
				//服务端启动接收消息的线程
				new Thread(cs.new ServerRecieveThread(socket)).start();
			}
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}
	class ServerRecieveThread implements Runnable {
		private Socket socket;
		private String recieveMsg;
		public ServerRecieveThread(Socket socket) {
		        this.socket = socket;
		    }
			public String getRecieveMsg() {
		        return recieveMsg;
		    }
		@Override
		public void run() {
			try {
				InputStream ips = socket.getInputStream();
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        while (true) {
		        	int t = ips.read();
		            // 收到换行符为止
		            while (t != '\n') {
		            	bos.write(t);
		                t = ips.read();
		                }
		            byte[] b = bos.toByteArray();
		            recieveMsg = new String(b);
		            System.out.println(recieveMsg);
		            bos.reset();//清空缓冲区	
		         //发送消息给各个客户端
		            	if(! recieveMsg.isEmpty())
		            	{                         
		            		 recieveMsg = recieveMsg+"---"+new Date(System.currentTimeMillis())+"\n";
		                    //通过for循环依次取出所有已经连上的socket对象，将服务端发出的消息写到输入
		                 for (int i=0;i<list.size();i++) {
		                     OutputStream ops = list.get(i).getOutputStream();
		                     ops.write( recieveMsg.getBytes());
		                     
		                     ops.flush();
		                 	}
		                 recieveMsg=null;
		            	}
		            	 if(socket.isClosed())
		 		        	System.out.println(socket.getInetAddress()+"关闭连接");
		            }
		    } catch (IOException e) {
		            e.printStackTrace();
		    }
			
		}
	}
}
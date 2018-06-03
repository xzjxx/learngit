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
			System.out.println("���Ƿ����(*^__^*) ,���ڵȴ����ӡ���");
			/*
             *  ��������Ϣ���߳�
             *  ��Ϊ��һ������˶�Ӧ����ͻ��ˣ���˷�����Ϣ���߳�ֻ��Ҫһ����
             *  ����Ҫ���ſͻ��˵ĸ������Ӷ����� 
             */
			//��ʾ�������Ӷ�ͻ���
			while(true) {
				Socket socket=serversocket.accept();
				list.add(socket);
				for(int i=0;i<list.size();i++)
					System.out.println(i);
				System.out.println("�ͻ���"+socket.getInetAddress()+"���ӳɹ�");
				//���������������Ϣ���߳�
				new Thread(cs.new ServerRecieveThread(socket)).start();
			}
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
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
		            // �յ����з�Ϊֹ
		            while (t != '\n') {
		            	bos.write(t);
		                t = ips.read();
		                }
		            byte[] b = bos.toByteArray();
		            recieveMsg = new String(b);
		            System.out.println(recieveMsg);
		            bos.reset();//��ջ�����	
		         //������Ϣ�������ͻ���
		            	if(! recieveMsg.isEmpty())
		            	{                         
		            		 recieveMsg = recieveMsg+"---"+new Date(System.currentTimeMillis())+"\n";
		                    //ͨ��forѭ������ȡ�������Ѿ����ϵ�socket���󣬽�����˷�������Ϣд������
		                 for (int i=0;i<list.size();i++) {
		                     OutputStream ops = list.get(i).getOutputStream();
		                     ops.write( recieveMsg.getBytes());
		                     
		                     ops.flush();
		                 	}
		                 recieveMsg=null;
		            	}
		            	 if(socket.isClosed())
		 		        	System.out.println(socket.getInetAddress()+"�ر�����");
		            }
		    } catch (IOException e) {
		            e.printStackTrace();
		    }
			
		}
	}
}
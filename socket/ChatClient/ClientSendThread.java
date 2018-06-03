package ChatClient;

import java.io.OutputStream;
import java.net.Socket;
import ChatFriend.ChatRoom;

public class ClientSendThread implements Runnable {
	Socket socket;
	ChatRoom chr;
	int flag;
	public ClientSendThread(Socket socket, ChatRoom chr) {
		this.socket = socket;
		this.chr = chr;
		this.flag=0;
	}
	@Override
	public void run() {
		 try {

			 chr.ops = socket.getOutputStream(); //获取客户端发来的信息        
	            while (true) {
	                if(chr.sendMsg!=null){
	                    byte[] b = chr.sendMsg.getBytes();
	                    chr.ops.write(b);
	                    chr.ops.flush(); //刷新，清空缓冲区            
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	}

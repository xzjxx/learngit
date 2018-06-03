package ChatClient;
import java.io.*;
import java.net.*;
import java.util.*;

import ChatFriend.ChatRoom;
import Common.User;
public class ClientRecieveThread implements Runnable {
	private Socket socket;
    private ChatRoom chr;//传入聊天面板对象，调用getArea方法获取sendArea对象
    public ClientRecieveThread(Socket socket,ChatRoom chr) {
        this.socket = socket;
        this.chr = chr;
    }
	@Override
	public void run() {
		try {
            InputStream ips = socket.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (true) {
                int t = ips.read();
                while (t != '\n') {
                    bos.write(t);
                    t = ips.read();
                }
                byte[] b = bos.toByteArray();
                //收到服务端传来的消息
                String msg = new String(b);
                System.out.println("客户端接收消息："+msg);
                //将消息显示在消息面板上
                chr.getArea().appendText(msg+"\n"); 
                //将服务端的消息接收到并显示在面板后，清空输入流，下次收到消息后就不会显示上次已经收到的消息
                bos.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}

package ChatClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import ChatFriend.ChatRoom;
import Common.User;

public class ChatClient {
	int flag=0;
    public ChatClient(User u) {
        try {
            Socket socket = new Socket("183.219.205.28",8888);
            //Socket socket = new Socket("127.0.0.1",8888);
            System.out.println("���ǿͻ���O(��_��)O~");
            ChatRoom chr = new ChatRoom(u); //�������Һ��ѽ���               
            // ��������Ϣ���߳�
            new Thread(new ClientRecieveThread(socket, chr)).start();
            // ��������Ϣ���߳� 
        	new Thread(new ClientSendThread(socket, chr)).start();
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
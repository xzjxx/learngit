package ChatClient;
import java.io.*;
import java.net.*;
import java.util.*;

import ChatFriend.ChatRoom;
import Common.User;
public class ClientRecieveThread implements Runnable {
	private Socket socket;
    private ChatRoom chr;//�������������󣬵���getArea������ȡsendArea����
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
                //�յ�����˴�������Ϣ
                String msg = new String(b);
                System.out.println("�ͻ��˽�����Ϣ��"+msg);
                //����Ϣ��ʾ����Ϣ�����
                chr.getArea().appendText(msg+"\n"); 
                //������˵���Ϣ���յ�����ʾ������������������´��յ���Ϣ��Ͳ�����ʾ�ϴ��Ѿ��յ�����Ϣ
                bos.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}

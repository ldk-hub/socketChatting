package muti_network;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {

	private ArrayList<MultiServerThread> list;
	private Socket socket;
	
	//서버쪽 소켓통신 포트번호 5000번으로 설정하여 실행
	public MultiServer() throws IOException{
		list = new ArrayList<MultiServerThread>();
		ServerSocket serverSocket = new ServerSocket(5000);
		MultiServerThread mst = null;
		boolean isStop = false;
		while(!isStop) {
			System.out.println("Server ready...");
			socket = serverSocket.accept();
			mst = new MultiServerThread(this);
			list.add(mst);
			Thread t= new Thread(mst);
			t.start();
		}
	}
	
	public void Ddos() {
		System.exit(0);
	}
	
	public ArrayList<MultiServerThread> getList(){
		return list;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public static void main(String arg[])throws IOException{
		new MultiServer();
	}
}

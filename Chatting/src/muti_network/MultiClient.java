package muti_network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiClient implements ActionListener {

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JFrame jframe, login1; //창
	private JTextField jtf, idc, pass; //전송, 아이디, 비번창
	private JTextArea jta, jlo; // 배경창, 로그인창 타자
	private JLabel jlb1, jlb2, jID, jPW; //창에서 유저아이디와 아이피주소를 표시해주는라벨
	private JPanel jp1, jp2, jp3, jp4; //버튼등 담아서 프레임에 붙이는 바구니
	private String ip; //로그인 할수 있는 아이피
	private String id; //로그인 아이디
	private JButton jbtn, jbtn1, jexit; // 전송버튼, 로그인, 종료 버튼
	public boolean changepower = false;
	public boolean saypower = false;
	private boolean login = false;
	
	public MultiClient() {
		//자바 내부에서 지원하는 프레임
		jframe = new JFrame("Multi Chatting");
		login1 = new JFrame("Login");
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(true);
		progressBar.setBounds(32, 303, 195, 14);
		
		jtf = new JTextField(20);
		idc = new JPasswordField(20);
		pass = new JTextField(20);
		
		jta = new JTextArea(43, 43) {
			{
				setOpaque(false);
			}
			public void paintComponent(Graphics g) {
				//g.drawImage(img, 0, 0, null); 백그라운드 키티이미지 삽입
				super.paintComponent(g);
			}
		};
		jlo = new JTextArea(30, 30);
		jlb1 = new JLabel("테스트 채팅창") {
			{
				setOpaque(false);
			}
		};
		jlb2 = new JLabel("IP : " + ip) {
			{
				setOpaque(true);
			}
		};
		jID = new JLabel("IP"); //ID라벨
		jPW = new JLabel("PW"); //PW라벨
		jbtn = new JButton("Enter");//채팅전송 버튼
		jbtn1 = new JButton("Login"); //로그인 버튼
		jexit = new JButton("exit"); //종료 버튼
		jp1 = new JPanel(); //바구니
		jp2 = new JPanel();
		jp3 = new JPanel(); //로그인 화면
		jp4 = new JPanel();
		jbtn.setFont(new Font("HY엽서L", Font.PLAIN, (int) 20));
	      jlb1.setFont(new Font("HY엽서L", Font.PLAIN, (int) 15));
	      jlb1.setBackground(Color.lightGray);
	      jlb2.setBackground(Color.lightGray);
	      jlb2.setFont(new Font("HY엽서L", Font.PLAIN, (int) 15));
	      
	      jID.setFont(new Font("HY엽서L", Font.PLAIN, (int) 30));
	      jID.setHorizontalAlignment(jID.CENTER);
	      jPW.setFont(new Font("HY엽서L", Font.PLAIN, (int) 30));
	      jPW.setHorizontalAlignment(jPW.CENTER);
	      
	      idc.setFont(new Font("HY엽서L", Font.PLAIN, (int) 30));
	      idc.setBackground(Color.WHITE);
	      pass.setFont(new Font("HY엽서L", Font.PLAIN, (int) 30));
	      pass.setBackground(Color.WHITE);
	      jbtn1.setBackground(Color.lightGray);
	      jbtn1.setFont(new Font("HY엽서L", Font.PLAIN, (int) 30));
	      jexit.setBackground(Color.lightGray);
	      jexit.setFont(new Font("HY엽서L", Font.PLAIN, (int) 30));
	      jbtn.setBackground(Color.lightGray);
	      jlo.setBackground(Color.lightGray);
	      
	      jp1.setLayout(new BorderLayout());
	      jp2.setLayout(new BorderLayout());
	      jp3.setLayout(new GridLayout(3,2,10,10));
	
	      jp1.add(jbtn, BorderLayout.EAST); 
	      jp1.add(jtf, BorderLayout.CENTER);
	      jp2.add(jlb1, BorderLayout.CENTER);
	      jp2.add(jlb2, BorderLayout.EAST);
	      
	      jp1.setBackground(Color.lightGray);
	      jp2.setBackground(Color.lightGray);
	      jp3.setBackground(Color.lightGray);
	      jp3.add(jID);
	      jp3.add(idc);
	      jp3.add(jPW);
	      jp3.add(pass);
	      jp3.add(jbtn1);
	      jp3.add(jexit);
	      jframe.add(jp1, BorderLayout.SOUTH);
	      jframe.add(jp2, BorderLayout.NORTH);
	      login1.add(jp3, BorderLayout.EAST);
	      login1.add(jp4, BorderLayout.EAST);
	      //이거뭔지 조사
	      JScrollPane jsp = new JScrollPane(jta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	      jframe.add(jsp, BorderLayout.CENTER);
	      JScrollPane jsp1 = new JScrollPane(jlo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	      login1.add(jp3, BorderLayout.CENTER);
	      
	      jtf.addActionListener(this);
	      jbtn.addActionListener(this);
	      jexit.addActionListener(this);
	      
	      //이것도 조사
	      jframe.addWindowListener(new WindowAdapter() {
	    	  public void windowClosing(WindowEvent e) {
	    		  try {
	    		  oos.writeObject(id + "#exit");
	    		  } catch (IOException ee) {
	    			  ee.printStackTrace();
	    		  }
	    		  System.exit(0);
	    	  }
	    	  public void windowOpened(WindowEvent e) {
	    		  jtf.requestFocus();
	    	  }
	      });
	      //이거뭔지
	      jbtn1.addActionListener(this);
	      //이거옵션뭔지
	      jta.setEditable(false);
	      Toolkit tk = Toolkit.getDefaultToolkit();
	      Dimension d = tk.getScreenSize();
	      int screenHeight = d.height;
	      int screenWidth = d.width;
	      //스크린 크기 설정
	      jframe.pack();
	      jframe.setLocation((screenWidth - jframe.getWidth()) / 2, (screenHeight - jframe.getHeight()) / 2);
	      jframe.setResizable(false);
	      jframe.setVisible(false);
	      
	      login1.pack();
	      login1.setSize(800, 300);
	      login1.setLocation((screenWidth - jframe.getWidth()) / 2, (screenHeight - jframe.getHeight()) / 2);
	      login1.setResizable(false);
	      login1.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		String msg = jtf.getText();
		
		String str = e.getActionCommand();
		
		if(str.equals("Login")) {
			jframe.setVisible(true);
			login1.setVisible(false);
			
			ip = idc.getText();
			id = pass.getText();
		}
		
		if(str.equals("exit")) {
			System.exit(0);
		}
		
		if(obj == jtf && changepower == true) {
			changepower = false;
			if(msg == null || msg.length() == 0) {
				JOptionPane.showMessageDialog(jframe, "글을쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
			}else {
				id = jtf.getText();
				jtf.setText("");
			}
		}else if (obj == jtf && saypower == true) {
			saypower = false;
			if(msg ==null || msg.length() == 0) {
				JOptionPane.showMessageDialog(jframe, "글을 쓰세요","경고", JOptionPane.WARNING_MESSAGE);
			}else {
				id = jtf.getText();
				jtf.setText("");
			}
		}
		
		if(obj == jtf) {
			if(msg == null || msg.length() == 0) {
				JOptionPane.showMessageDialog(jframe, "글을쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
			}else {
				try {
					oos.writeObject(id + "#" + msg);
				} catch(IOException ee) {
					ee.printStackTrace();
				}
				jtf.setText("");
			}
		}else if(obj == jbtn) {
			try {
				oos.writeObject(id + "#exit");
			}catch (IOException ee) {
				ee.printStackTrace();
			}
			System.exit(0);
		}
	}
	
	public void exit() {
		System.exit(0);
	}
	
	public void init() throws IOException{
		socket = new Socket("172.19.134.32", 5000); //접속할 해당 피시의 아이피를 기입
		System.out.println("connected...");
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		MultiClientThread ct = new MultiClientThread(this);
		Thread t = new Thread(ct);
		t.start();
	}
	
	public static void main(String args[]) throws IOException{
		JFrame.setDefaultLookAndFeelDecorated(true);
		MultiClient cc = new MultiClient();
		cc.init();
	}
	
	public ObjectInputStream getOis() {
		return ois;
	}
	
	public JTextArea getJta() {
		return jta;
	}
	
	public String getId() {
		return id;
	}
	
	public  void SetName(String a) {
		id = a;
	}
	
	public void Clear() {
		jta.setText(""); //초기화
		jtf.requestFocus();
	}
	
	public void My() {
		jta.setDisabledTextColor(Color.blue);
	}

}

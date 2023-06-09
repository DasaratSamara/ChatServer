import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class NetClient extends JFrame implements KeyListener {

	final String serverIP = "127.0.0.1";
	final int serverPort = 1234;

	JTextArea textArea;
	JScrollPane scrollPane;
	InputStreamReader in;
	PrintWriter out;

	NetClient() {
		//contentPane.add(button);
		super("Simple Chat client");
		this.setSize(400, 500);
		this.setDefaultCloseOperation(3);
		this.textArea = new JTextArea();
		this.textArea.setBackground(Color.BLACK);
		this.textArea.setForeground(Color.WHITE);
		this.textArea.setEditable(false);
		this.textArea.setMargin(new Insets(10, 10, 10, 10));
		this.scrollPane = new JScrollPane(this.textArea);
		this.add(this.scrollPane);
//		this.connect();




		// Подсоединяемся к серверу
		connect();

	}

	void connect() {
		try {
			Socket socket = new Socket(serverIP, serverPort);
			in = new InputStreamReader(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream());
			textArea.addKeyListener(this);
		} catch (IOException e) {
			textArea.setForeground(Color.RED);
			textArea.append("Server " + serverIP + " port " + serverPort + " " + "" + "NOT AVAILABLE");
			e.printStackTrace();
		}
		new Thread() {
			// в отдельном потоке
			// принимаем символы от сервера
			public void run() {
				while (true) {
					try {
						addCharToTextArea((char) (in.read()));
					} catch (IOException e) {
						textArea.setForeground(Color.RED);
						textArea.append("\nCONNECTION ERROR");
						e.printStackTrace();
						return;
					}
				}
			};
		}.start();

	}

	public static void main(String[] args) {
		new NetClient().setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {}
	@Override 
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// отправляем напечатанный символ в сеть и на экран
		out.print(arg0.getKeyChar());
		out.flush();
		
		System.out.print((int)(arg0.getKeyChar()));
		addCharToTextArea(arg0.getKeyChar());
	}

	void addCharToTextArea(char c) {
		textArea.append(c + "");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

}

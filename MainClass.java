import java.net.ServerSocket;
import java.net.Socket;

public class MainClass {

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(4321);
		System.out.println("waiting for connection...");
		Socket socket;
		while (true) {
			socket = serverSocket.accept();
            System.out.println("connected");
			HttpManager httpManager = new HttpManager(socket, "htmlText.html");
			httpManager.start();
			socket.close();
			System.out.println("end");
		}
	}

}
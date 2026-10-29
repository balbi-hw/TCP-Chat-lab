import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            System.out.println("서버에 연결되었습니다.");
        }
    }
}
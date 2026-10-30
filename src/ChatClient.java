import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            System.out.println("서버 연결 성공");

            OutputStream output = socket.getOutputStream();

            output.write("MSG hello\n".getBytes(StandardCharsets.UTF_8));
        }
    }
}
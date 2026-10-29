import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {

            OutputStream output = socket.getOutputStream();

            System.out.println("Enter를 누르면 데이터를 전송합니다.");
            System.in.read();
            output.write(65);
            output.write(66);
            output.write(67);

            System.out.println("데이터 전송 완료");
        }
    }
}
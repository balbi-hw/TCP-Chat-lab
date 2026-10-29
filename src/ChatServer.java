import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("서버 시작");

            while (true) {
                try (Socket socket = server.accept()) {
                    System.out.println("연결 성공!");

                    // Socket 으로부터 InputStream 얻기
                    InputStream input = socket.getInputStream();
                    
                    // InputStream 에서 데이터 1바이트 읽기
                    int val = input.read();
                    
                    // 읽은 값 출력하기
                    System.out.println(val);

                }
            }
        }
    }
}
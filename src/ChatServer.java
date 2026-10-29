import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("서버 시작");

            while (true) {
                // accept 호출
                Socket socket = server.accept();

                // 연결 성공 출력
                System.out.println("연결 성공!");

                // Socket 닫기
                socket.close();
            }
        }
    }
}
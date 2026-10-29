import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("서버 시작");
            
            // 클라이언트 연결 기다리기
            // accept() 의 반환읠 Socket 변수에 저장
            Socket socket = server.accept();

            // 연결 성공 메시지 출력
            System.out.println("연결 성공!");

        } finally {
            System.out.println("연결 종료");
        }

        System.out.println("서버 종료");
    }
}
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("서버 시작");

            while (true) {
                Socket socket = server.accept();
                System.out.println("연결 성공!");

                Thread worker = new Thread(() -> {
                    try (socket) {
                        
                        // Socket 으로부터 InputStream 얻기
                        InputStream input = socket.getInputStream();
                        
                        // InputStream 에서 데이터 1바이트 읽기
                        int val = input.read();
                        
                        // 스레드 이름과 읽은 값 출력하기
                        System.out.println(Thread.currentThread().getName() + " : " + val);
                    } catch (IOException e) {
                        System.out.println("읽기 오류: " + e.getMessage());
                    }
                });

                worker.start();

                
            }
        }
    }
}
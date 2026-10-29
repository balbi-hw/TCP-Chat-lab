import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {

            OutputStream output = socket.getOutputStream();

            output.write("안녕하세요\n".getBytes(StandardCharsets.UTF_8));
            output.write("반갑습니다\n".getBytes(StandardCharsets.UTF_8));


            // 데이터 도착과 메시지 완성의 차이
            // OutputStream output = socket.getOutputStream();

            // // 1. 줄바꿈 없이 문자열 전송
            // output.write("안녕하세요".getBytes(StandardCharsets.UTF_8));

            // System.out.println("데이터 전송 완료");
            // System.out.println("Enter를 누르면 줄바꿈을 전송합니다.");

            // // 2. 사용자 입력 대기
            // System.in.read();

            // // 3. 줄바꿈 전송
            // output.write("\n".getBytes(StandardCharsets.UTF_8));
        }
    }
}
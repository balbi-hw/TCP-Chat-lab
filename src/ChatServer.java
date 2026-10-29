import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("서버 시작");

            while (true) {
                Socket socket = server.accept();
                System.out.println("연결 성공!");

                Thread worker = new Thread(() -> {
                    try (socket) {

                        BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8
                            )
                        );
                        
                        while (true) {

                            // readLine() 으로 문자열 읽기
                            String val = reader.readLine();

                            if (val == null) {
                                break;
                            }

                            // "QUIT" 이면 반복 종료
                            if (val.equals("QUIT")) {
                                System.out.println("연결을 종료합니다.");
                                break;
                            }

                            // "MSG " 로 시작한다면 메시지 본문만 추출하여 출력
                            if (val.startsWith("MSG ")) {
                                String line = val.substring(4);
                                if (!line.isEmpty()) {
                                    System.out.println(val.substring(4));
                                    continue;
                                };
                                continue;
                            }

                            // 그 외의 명령이라면 "알 수 없는 명령 출력"
                            System.out.println("알 수 없는 명령");
                        }
                        
                    } catch (IOException e) {
                        System.out.println("읽기 오류: " + e.getMessage());
                    }
                });

                worker.start();
                
            }
        }
    }
}
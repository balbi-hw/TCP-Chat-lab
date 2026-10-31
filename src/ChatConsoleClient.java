
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatConsoleClient {

    public static void main(String[] args)
            throws IOException, InterruptedException {

        try (Socket socket = new Socket("127.0.0.1", 5000)) {

            System.out.println("서버 연결 완료");
            System.out.println("종료하려면 /quit 입력");

            Thread receiver = new Thread(() -> {
                try {
                    BufferedReader serverReader =
                            new BufferedReader(
                                new InputStreamReader(
                                    socket.getInputStream(),
                                    StandardCharsets.UTF_8
                                )
                            );

                    while (true) {
                        // TODO 1:
                        // 서버에서 한 줄 읽기
                        String line = serverReader.readLine();

                        // TODO 2:
                        // null이면 반복 종료
                        // MSG 로 시작하면 본문 출력
                        if (line == null) {
                            break;
                        }

                        if (line.startsWith("MSG ")) {
                            String output = line.substring(4);
                            
                            if (!output.isEmpty()) {
                                System.out.println("메시지 수신: " + output);
                                continue;
                            }

                            continue;
                        }
                    }

                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        System.out.println(
                            "수신 오류: " + e.getMessage()
                        );
                    }
                } finally {
                    System.out.println("수신 스레드 종료");
                }
            }, "receiver");

            receiver.start();

            BufferedReader console =
                    new BufferedReader(
                        new InputStreamReader(
                            System.in,
                            StandardCharsets.UTF_8
                        )
                    );

            OutputStream output = socket.getOutputStream();

            try {
                while (true) {
                    String input = console.readLine();

                    // TODO 3:
                    // input == null 이면 종료
                    // input이 "/quit"이면 "QUIT\n" 전송 후 종료
                    if (input == null) {
                        break;                        
                    }

                    if (input == "/quit") {
                        output.write("QUIT\n".getBytes(StandardCharsets.UTF_8));
                    }

                    // TODO 4:
                    // 빈 메시지는 건너뛰기
                    // 일반 메시지는 "MSG <본문>\n" 전송
                    if (input.isEmpty()) {
                        continue;
                    }

                    String data = "MSG " + input + "\n";
                    output.write(data.getBytes(StandardCharsets.UTF_8));
                }
            } finally {
                socket.close();
                receiver.join();
            }
        }

        System.out.println("채팅 클라이언트 종료");
    }
}

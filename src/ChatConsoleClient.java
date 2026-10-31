
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
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
                        String line = serverReader.readLine();

                        System.out.println("[수신 원문] " + line);

                        if (line == null) {
                            break;
                        }

                        if (line.startsWith("MSG ")) {
                            String output = line.substring(4);
                            
                            if (!output.isEmpty()) {
                                System.out.println("메시지 수신: " + output);
                            }
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

            // BufferedReader console =
            //         new BufferedReader(
            //             new InputStreamReader(
            //                 System.in,
            //                 StandardCharsets.UTF_8
            //             )
            //         );

            // 콘솔 인코딩 문제 해결을 위한 임시 코드 - 원인 파악 중
            BufferedReader console =
                    new BufferedReader(
                        new InputStreamReader(
                            System.in,
                            Charset.forName("MS949")
                        )
                    );

            OutputStream output = socket.getOutputStream();

            try {
                while (true) {
                    String input = console.readLine();

                    if (input == null) {
                        break;                        
                    }

                    if (input.equals("/quit")) {
                        output.write("QUIT\n".getBytes(StandardCharsets.UTF_8));
                        break;
                    }

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

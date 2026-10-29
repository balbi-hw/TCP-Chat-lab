import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

    private static final List<Socket> clients = 
            new CopyOnWriteArrayList<>();

    private static void broadcast(Socket sender, String message) {
        int recipientIndex = 0;

        for (Socket client : clients) {

            // sender 와 client 가 같은 Socket 이면 건너뛰기
            if (sender == client) {
                continue;
            };

            recipientIndex++;

            try {
                System.out.println(
                    recipientIndex + "빈 수신자 전송 시작"
                );

                if (recipientIndex == 2) {
                    Thread.sleep(3000);
                };

                // client.getOutputStream() 으로 출력 스트림 얻기
                OutputStream output = client.getOutputStream();
                    
                // "MSG " + message + "\n" 을 UTF-8 바이트로 변환해 전송
                String data = "MSG " + message + "\n";            
                output.write(
                    data.getBytes(StandardCharsets.UTF_8)
                );
                // IOException 처리
                // } catch (IOException e) {
                //     System.out.println("출력 오류: " + e.getMessage());

                System.out.println("전송 완료");

            } catch (IOException e) {
                System.out.println("전송 오류: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("서버 시작");

            while (true) {
                Socket socket = server.accept();
                System.out.println("연결 성공!");

                clients.add(socket);

                System.out.println("접속자 수: " + clients.size());

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
                                    broadcast(socket, line);
                                    continue;
                                };

                                continue;
                            }

                            // 그 외의 명령이라면 "알 수 없는 명령 출력"
                            System.out.println("알 수 없는 명령");
                        }
                        
                    } catch (IOException e) {
                        System.out.println("읽기 오류: " + e.getMessage());
                    } finally {
                        clients.remove(socket);
    
                        System.out.println("접속자 수: " + clients.size());
                    }
                });
                worker.start();
            }
        }
    }
}
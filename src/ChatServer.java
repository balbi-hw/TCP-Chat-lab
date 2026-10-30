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

    private static final List<ClientSession> clients = 
            new CopyOnWriteArrayList<>();

    private static final Object registryLock = new Object();

    private static void broadcast(ClientSession sender, String message) {

        for (ClientSession client : clients) {

            // 송신자 제외
            if (client == sender) {
                continue;                
            }

            ClientSession.EnqueueResult result = client.enqueue(message);

            switch (result) {
                case ACCEPTED -> {
                    System.out.println("메시지가 Queue에 전송되었습니다.");
                }
                
                case FULL -> {
                    System.out.println("Queue 가 가득 찼습니다.");
                    // 해당 세션 자원 반환
                    client.close();
                }
                    
                case CLOSED -> {
                    System.out.println("세션이 닫혀있습니다.");    
                }

                case NOT_READY -> {
                    System.out.println("아직 준비되지 않은 session 입니다.");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(5000)) {
            System.out.println("서버 시작");

            while (true) {
                Socket socket = server.accept();
                System.out.println("연결 성공!");

                ClientSession session = new ClientSession(socket,
                    closedSession -> {
                        synchronized (registryLock) {
                            clients.remove(closedSession);
                        }
                    }
                );

                boolean started;

                synchronized (registryLock) {
                    started = session.startWriter();

                    if (started) {
                        clients.add(session);
                    }                    
                }

                if (!started) {
                    session.close();
                    continue;
                }


                Thread worker = new Thread(() -> {
                    try {

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
                                    broadcast(session, line);
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
                        session.close();
    
                    }
                });
                worker.start();
            }
        }
    }
}

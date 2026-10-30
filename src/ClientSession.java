
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientSession {

    private final Socket socket;

    private final BlockingQueue<String> outgoing =
            new ArrayBlockingQueue<>(10);

    private Thread writerThread;

    public ClientSession(Socket socket) {
        this.socket = socket;
    }

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public boolean enqueue(String message) {
        // TODO 1:
        // outgoing.offer()로 메시지 추가
        // 성공 여부 반환
        // System.out.println("메시지 추가 여부: " + outgoing.offer(message));

        if (closed.get()) {
            return false;
        }

        return outgoing.offer(message);
    }

    public void startWriter() {
        writerThread = new Thread(() -> {
            try {
                OutputStream output = socket.getOutputStream();

                while (true) {
                    // TODO 2:
                    // outgoing.take()로 메시지 꺼내기
                    String val = outgoing.take();
                    
                    // 실험용 지연
                    Thread.sleep(1000);

                    String line = "MSG " + val + "\n";

                    // TODO 3:
                    // UTF-8 바이트 배열로 변환하기
                    byte[] data = line.getBytes(StandardCharsets.UTF_8);

                    // TODO 4:
                    // output.write()로 전송하기
                    output.write(data);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                System.out.println("Writer 오류: " + e.getMessage());
            } finally {
                System.out.println("Writer 스레드 종료");
            }
        });

        writerThread.start();
    }

    public void stopWriter() {
        writerThread.interrupt();
    }

    public void close() {

        if (!closed.compareAndSet(false, true)) {
            return;
        }

        System.out.println("실제 종료 처리 시작");
    
        try (this.socket) {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("에러 발생: " + e.getMessage());
        }

        this.stopWriter();
    }
}

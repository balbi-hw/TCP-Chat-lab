
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class ClientSession {

    private final Socket socket;

    private final BlockingQueue<String> outgoing =
            new ArrayBlockingQueue<>(10);

    private Thread writerThread;

    public ClientSession(
        Socket socket,
        Consumer<ClientSession> onClosed
    ) {
        this.socket = socket;
        this.onClosed = onClosed;
    }

    // private final AtomicBoolean closed = new AtomicBoolean(false);

    private enum State {
        CREATED,
        ACTIVE,
        CLOSING,
        CLOSED
    }

    private State state = State.CREATED;

    private final Object stateLock = new Object();

    public enum EnqueueResult {
        ACCEPTED,
        FULL,
        NOT_READY,
        CLOSED,
    }

    private final Consumer<ClientSession> onClosed;

    public EnqueueResult enqueue(String message) {
        // TODO 1:
        // outgoing.offer()로 메시지 추가
        // 성공 여부 반환
        // System.out.println("메시지 추가 여부: " + outgoing.offer(message));

        synchronized (stateLock) {

            if (state == State.CREATED) {
                return EnqueueResult.NOT_READY;
            }

            if (state == State.CLOSING || state == State.CLOSED) {
                return EnqueueResult.CLOSED;
            }

            if (!outgoing.offer(message)) {
                return EnqueueResult.FULL;
            }

            return EnqueueResult.ACCEPTED;
        }
    }

    public boolean startWriter() {
        
        synchronized (stateLock) {
            // CREATED 가 아니면 실패
            if (state != State.CREATED) {
                return false;
            }
            
            writerThread = new Thread(() -> {
                try {
                    OutputStream output = socket.getOutputStream();
    
                    while (true) {
                        String val = outgoing.take();
    
                        String line = "MSG " + val + "\n";
    
                        byte[] data = line.getBytes(StandardCharsets.UTF_8);
    
                        output.write(data);
                    }
    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    System.out.println("Writer 오류: " + e.getMessage());
                } finally {
                    System.out.println("Writer 스레드 종료: Session 정리 요청");
    
                    close();
                }
            });

            writerThread.start();
    
            // state ACTIVE 로 변경
            state = State.ACTIVE;

            return true;
        }

    }

    public void stopWriter() {
        Thread t = writerThread;

        if (t != null && t != Thread.currentThread()) {
            t.interrupt();
        }
    }

    public void close() {

        synchronized (stateLock) {
            if (state == State.CLOSED || state == State.CLOSING) {
                return;
            }

            state = State.CLOSING;
        }

        System.out.println("실제 종료 처리 시작");
    
        try {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("에러 발생: " + e.getMessage());
            } finally {
                stopWriter();
            } 
        } finally {
            synchronized (stateLock) {
                state = State.CLOSED;
            }

            onClosed.accept(this);
        }
    }
}

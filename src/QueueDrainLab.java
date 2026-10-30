
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueDrainLab {

    private static final String STOP = "__STOP__";

    public static void main(String[] args)
            throws InterruptedException {

        BlockingQueue<String> queue =
                new ArrayBlockingQueue<>(3);

        Thread writer = new Thread(() -> {
            try {
                while (true) {
                    String message = queue.take();

                    // TODO 1:
                    // STOP 메시지면 반복 종료
                    if (STOP.equals(message)) {
                        break;
                    }

                    // 실제 전송을 가정한 처리 지연
                    Thread.sleep(300);

                    System.out.println("처리 완료: " + message);
                }
            } catch (InterruptedException e) {
                System.out.println("Writer 인터럽트 발생");
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Writer 종료");
                System.out.println("남은 Queue: " + queue);
            }
        });

        writer.start();

        queue.put("A");
        queue.put("B");
        queue.put("C");

        boolean graceful =
                args.length > 0 && args[0].equals("graceful");

        if (graceful) {
            // TODO 2:
            // STOP을 Queue에 추가
            queue.put(STOP);
        } else {
            // TODO 3:
            // writer 스레드에 interrupt 요청
            writer.interrupt();
        }

        writer.join();

        System.out.println("모든 작업 종료");
    }
}

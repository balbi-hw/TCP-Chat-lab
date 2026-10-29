
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueShutdownLab {

    public static void main(String[] args)
            throws InterruptedException {

        BlockingQueue<String> queue =
                new ArrayBlockingQueue<>(3);

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String message = queue.take();

                    // TODO 1:
                    // message가 "STOP"이면 break
                    if (message.equals("STOP")) {
                        System.out.println("종료합니다.");
                        break;
                    }

                    // TODO 2:
                    // 일반 메시지라면 출력
                    System.out.println(message);

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("Consumer 종료");
        }, "consumer");

        Thread producer = new Thread(() -> {
            try {
                queue.put("A");
                queue.put("B");

                // TODO 3:
                // STOP 메시지를 Queue에 추가
                // queue.put("STOP");

                System.out.println("Producer 종료");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");

        consumer.start();
        producer.start();

        consumer.join();
        producer.join();

        System.out.println("모든 작업 종료");
    }
}

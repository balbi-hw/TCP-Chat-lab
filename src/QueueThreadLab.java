
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueThreadLab {

    public static void main(String[] args)
            throws InterruptedException {

        BlockingQueue<String> queue =
                new ArrayBlockingQueue<>(3);

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    System.out.println("[Consumer] 메시지 대기");

                    // TODO 1: queue.take()로 메시지 꺼내기
                    String message = queue.take();

                    // TODO 2: 꺼낸 메시지 출력
                    System.out.println(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(1000);

                // TODO 3: put()을 이용해
                // A, B, C를 순서대로 추가
                queue.put("A");
                queue.put("B");
                queue.put("C");

                // TODO 4: 각 메시지 추가 후
                // Producer 로그 출력
                System.out.println("A 추가");
                System.out.println("B 추가");
                System.out.println("C 추가");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");

        consumer.start();
        producer.start();

        consumer.join();
        producer.join();

        System.out.println("모든 작업 완료");
    }
}

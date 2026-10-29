import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueLab {
    public static void main(String[] args)
            throws InterruptedException {

        BlockingQueue<String> queue =
                new ArrayBlockingQueue<>(3);

        // TODO 1: offer()를 이용해 A, B, C, D 추가
        // 각 offer()의 반환값을 출력
        System.out.println("A 추가: " + queue.offer("A"));
        System.out.println("B 추가: " + queue.offer("B"));
        System.out.println("C 추가: " + queue.offer("C"));
        System.out.println("D 추가: " + queue.offer("D"));
        
        // TODO 2: take()로 가장 먼저 들어온 메시지 꺼내기
        // 꺼낸 메시지를 출력
        System.out.println(queue.take());
        
        // TODO 3: D를 다시 추가하고 성공 여부 출력
        System.out.println("D 추가: " + queue.offer("D"));
    }
}
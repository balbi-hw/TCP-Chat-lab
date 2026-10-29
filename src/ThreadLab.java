public class ThreadLab {
    public static void main(String[] args) 
            throws InterruptedException {

        Thread worker = new Thread(() -> {
            System.out.println("Worker 실행");
        });

        worker.start();

        // Main, Worker 실행 순서 미보장 확인
        // Thread.sleep(100);

        // Main Worker 실행 순서 보장 방법
        // worker 가 종료될 때까지 Main 스레드가 대기한다.
        worker.join();

        System.out.println("Main 실행");
    }
}
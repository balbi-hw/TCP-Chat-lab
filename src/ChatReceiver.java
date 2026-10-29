import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatReceiver {
    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("127.0.0.1", 5000)) {

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            System.out.println("수신 대기 중");

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("수신: " + line);
            }
        }
    }
}
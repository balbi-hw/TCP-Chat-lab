
public class SessionStateLab {

    enum State {
        CREATED,
        ACTIVE,
        CLOSING,
        CLOSED
    }

    private State state = State.CREATED;
    private final Object lock = new Object();

    public boolean start() {
        synchronized (lock) {
            // TODO 1:
            // CREATED가 아니라면 false 반환
            if (state != State.CREATED) {
                return false;                
            }

            // TODO 2:
            // ACTIVE로 변경하고 true 반환
            state = State.ACTIVE;

            return true;
        }
    }

    public boolean beginClose() {
        synchronized (lock) {
            // TODO 3:
            // CREATED 또는 ACTIVE인 경우에만
            // CLOSING으로 변경 후 true 반환

            if (state == State.CREATED || state == State.ACTIVE) {
                state = State.CLOSING;
                
                return true;
            }

            // TODO 4:
            // 이미 CLOSING 또는 CLOSED라면 false 반환
            return false;
        }
    }

    public void finishClose() {
        synchronized (lock) {
            // TODO 5:
            // CLOSING인 경우에만 CLOSED로 변경
            if (state == State.CLOSING) {
                state = State.CLOSED;
            }
        }
    }

    public State getState() {
        synchronized (lock) {
            return state;
        }
    }

    // public static void main(String[] args) {

    //     SessionStateLab session = new SessionStateLab();

    //     System.out.println(session.getState());

    //     System.out.println(session.start());
    //     System.out.println(session.getState());

    //     System.out.println(session.beginClose());
    //     System.out.println(session.getState());

    //     session.finishClose();
    //     System.out.println(session.getState());

    // }
}

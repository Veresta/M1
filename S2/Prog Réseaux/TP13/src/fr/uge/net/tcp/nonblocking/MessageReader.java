package fr.uge.net.tcp.nonblocking;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MessageReader implements Reader<Message>{

    private enum State {
        DONE, WAITING_LOGIN, WAITING_STRING, ERROR, REFILL,
    };

    private State state = State.WAITING_LOGIN;
    private static final int BUFF_SIZE = 1024;

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private final ByteBuffer internalBuffer = ByteBuffer.allocate(BUFF_SIZE); // write-mode

    private final StringReader reader = new StringReader();
    private String login;
    private String text;
    @Override
    public ProcessStatus process(ByteBuffer bb) {
        if (state == State.DONE || state == State.ERROR) {
            throw new IllegalStateException();
        }

        if(state == State.WAITING_LOGIN){
            var statut = reader.process(bb);
            if(statut == ProcessStatus.DONE){
                login = reader.get();
                reader.reset();
                state = State.WAITING_STRING;
            }
        }
        if(state == State.WAITING_STRING){
            var statut = reader.process(bb);
            if(statut == ProcessStatus.DONE){
                text = reader.get();
                reader.reset();
                state = State.DONE;
            }
        }
        return ProcessStatus.DONE;
    }

    @Override
    public Message get() {
        if (state != State.DONE) {
            throw new IllegalStateException();
        }
        return new Message(login, text);
    }

    @Override
    public void reset() {
        state = State.WAITING_LOGIN;
        internalBuffer.clear();
    }
}

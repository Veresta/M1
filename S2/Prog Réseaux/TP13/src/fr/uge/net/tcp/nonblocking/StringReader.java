package fr.uge.net.tcp.nonblocking;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringReader implements Reader<String>{

    private enum State {
        DONE, WAITING_INT, WAITING_STRING, ERROR, REFILL,
    };

    private State state = State.WAITING_INT;
    private static final int BUFF_SIZE = 1024;

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private final ByteBuffer internalBuffer = ByteBuffer.allocate(BUFF_SIZE); // write-mode

    private final IntReader reader = new IntReader();
    private String message;

    @Override
    public ProcessStatus process(ByteBuffer bb) {
        if (state == State.DONE || state == State.ERROR) {
            throw new IllegalStateException();
        }

        if(state == State.WAITING_INT){
            var status = reader.process(bb);
            if(status == ProcessStatus.DONE){
                var size = reader.get();
                if(size < 0 || size > BUFF_SIZE){
                    return ProcessStatus.ERROR;
                }
                internalBuffer.limit(size);
                state = State.WAITING_STRING;
                reader.reset();
            }
        }
        bb.flip();
        try {
            if (bb.remaining() <= internalBuffer.remaining()) {
                internalBuffer.put(bb);
            } else {
                var oldLimit = bb.limit();
                bb.limit(internalBuffer.remaining());
                internalBuffer.put(bb);
                bb.limit(oldLimit);
            }
        } finally {
            bb.compact();
        }
        if (internalBuffer.hasRemaining()) {
            return ProcessStatus.REFILL;
        }
        state = State.DONE;
        internalBuffer.flip();
        message = UTF8.decode(internalBuffer).toString();
        return ProcessStatus.DONE;
    }

    @Override
    public String get() {
        if (state != State.DONE) {
            throw new IllegalStateException();
        }
        return message;
    }

    @Override
    public void reset() {
        state = State.WAITING_INT;
        internalBuffer.clear();
    }
}

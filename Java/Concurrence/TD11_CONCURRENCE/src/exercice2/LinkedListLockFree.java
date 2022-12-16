package exercice2;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class LinkedListLockFree<E> {
    private static final class Entry<E> {
        private final E element;
        private volatile Entry<E> next;

        Entry(E element) {
            this.element = element;
        }
    }

    private final Entry<E> head = new Entry<>(null); // fake first entry
    private final static VarHandle NEXT_HANDLER;

    static {
        var lookup = MethodHandles.lookup();
        try {
            NEXT_HANDLER = lookup.findVarHandle(Entry.class, "next", Entry.class);
        }catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    public void addLast(E element) {
        var oldValue = head;
        var newElem = new Entry<>(element);
        for(;;){
            if(NEXT_HANDLER.compareAndSet(oldValue, oldValue.next,null)){
                oldValue.next = newElem;
                return;
            }
            oldValue = oldValue.next;
        }
    }

    public int size() {
        var count = 0;
        for (var e = head.next; e != null; e = e.next) {
            count++;
        }
        return count;
    }

    private static Runnable createRunnable(LinkedListLockFree<String> list, int id) {
        return () -> {
            for (var i = 0; i < 10_000; i++) {
                list.addLast(id + " " + i);
            }
        };
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        var threadCount = 5;
        var list = new LinkedListLockFree<String>();
        var tasks = IntStream.range(0, threadCount).mapToObj(id -> createRunnable(list, id)).map(Executors::callable)
                .toList();
        var executor = Executors.newFixedThreadPool(threadCount);
        var futures = executor.invokeAll(tasks);
        executor.shutdown();
        for (var future : futures) {
            future.get();
        }
        System.out.println(list.size());
    }
}

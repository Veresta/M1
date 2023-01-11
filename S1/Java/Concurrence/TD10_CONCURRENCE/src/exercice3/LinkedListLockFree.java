package exercice3;

import com.sun.source.tree.UsesTree;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class LinkedListLockFree<T> {

    private record Link<T>(T value, Link<T> next){
        private Link {
            Objects.requireNonNull(value);
        }
    }

    private final AtomicReference<Link<T>> head = new AtomicReference<>();

    /**
     * Add the non-null value at the start of the list
     *
     * @param value value added to the list
     */
    public void addFirst(T value){
        Objects.requireNonNull(value);
        for(;;) {
            var oldHead = head.get();  // lecture volatile
            var newHead = new Link<>(value, oldHead);
            if (head.compareAndSet(oldHead, newHead)) {
                return;
            }
        }
    }

    /**
     * applies the consumer the elements of the list in order
     *
     * @param consumer consumer applied to the whole list
     */
    public void forEach(Consumer<? super T> consumer){
        Objects.requireNonNull(consumer);
        Stream.iterate(head.get(), Objects::nonNull, e -> e.next).forEach(e -> consumer.accept(e.value));
    }

    /**
     * Removes the first value of the list and returns it if the list not empty. If the list is empty, returns null.
     * @return the value at the start of the list if the list is not empty and null if the list is empty
     */
    public T pollFirst(){
        if(head.get() == null){
            return null;
        }
        var val = head.get();
        return head.getAndSet(val.next).value;
    }

    public static void main(String[] args) {
        var list = new LinkedListLockFree<String>();
        list.addFirst("Noel");
        list.addFirst("papa");
        list.addFirst("petit");
        list.forEach(System.out::println);
        System.out.println(list.pollFirst());
        list.forEach(System.out::println);
    }
}

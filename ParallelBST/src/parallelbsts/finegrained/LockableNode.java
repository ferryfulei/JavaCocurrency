package parallelbsts.finegrained;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockableNode<E extends Comparable<E>> {

    E element;
    LockableNode<E> left;
    LockableNode<E> right;
    private Lock lock = new ReentrantLock();

    public LockableNode(E element) {
        this.element = element;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

}

package parallelbsts.coarsegrained;

import parallelbsts.sequential.LinkedNodeBSTNoRecursion;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseBST<E extends Comparable<E>> extends LinkedNodeBSTNoRecursion<E> {

    private final Lock lock = new ReentrantLock();

    @Override
    public boolean add(E element) {
        lock.lock();
        try {
            return super.add(element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(E element) {
        lock.lock();
        try {
            return super.remove(element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(E element) {
        lock.lock();
        try {
            return super.contains(element);
        } finally {
            lock.unlock();
        }
    }
}

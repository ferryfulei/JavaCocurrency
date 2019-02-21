package parallelbsts;

public interface BST<E extends Comparable> {
    boolean add(E element);
    boolean remove(E element);
    boolean contains(E element);
}

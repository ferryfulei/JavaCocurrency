package parallelbsts.finegrained;

import parallelbsts.BST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineBST<E extends Comparable<E>> implements BST<E> {

    private LockableNode<E> root = null;
    private Lock rootLock = new ReentrantLock();

    @Override
    public boolean add(E element) {
        LockableNode<E> curr = null;
        LockableNode<E> parent = null;
        rootLock.lock();
        if (root == null) {
            root = new LockableNode<>(element);
            rootLock.unlock();
        } else {
            curr = root;
            curr.lock();
            rootLock.unlock();
            int compare = 0;
            while (true) {
                parent = curr;
                compare = curr.element.compareTo(element);
                if (compare == 0) {
                    curr.unlock();
                    return false;
                } else {
                    curr = (compare > 0) ? curr.left : curr.right;
                }
                if (curr == null) {
                    break;
                } else {
                    curr.lock();
                    parent.unlock();
                }
            }
            if (compare > 0) {
                parent.left = new LockableNode<>(element);
            } else {
                parent.right = new LockableNode<>(element);
            }
            parent.unlock();
        }
        return true;
    }

    @Override
    public boolean remove(E element) {
        LockableNode<E> curr = null;
        LockableNode<E> parent = null;
        rootLock.lock();
        if (root == null) {
            rootLock.unlock();
            return false;
        } else {
            curr = root;
            parent = curr;
            curr.lock();
            int compare = curr.element.compareTo(element);
            if (compare == 0) {
                LockableNode<E> replacement = findReplacementNode(curr);
                if (replacement != null) {
                    replacement.left = curr.left;
                    replacement.right = curr.right;
                }
                root = replacement;
                curr.unlock();
                rootLock.unlock();
                return true;
            } else {
                curr.lock();
                rootLock.unlock();
                int parentCompare = compare;
                while (true) {
                    compare = curr.element.compareTo(element);
                    if (compare == 0) {
                        LockableNode<E> replacement = findReplacementNode(curr);
                        if (parentCompare > 0) {
                            parent.left = replacement;
                        } else {
                            parent.right = replacement;
                        }
                        if (replacement != null) {
                            replacement.left = curr.left;
                            replacement.right = curr.right;
                        }
                        curr.unlock();
                        parent.unlock();
                        return true;
                    } else {
                        parent.unlock();
                        parent = curr;
                        curr = (compare > 0) ? curr.left : curr.right;
                        parentCompare = compare;
                    }
                    if (curr == null) break;
                    else curr.lock();
                }
            }
        }
        parent.unlock();
        return false;
    }

    private LockableNode<E> findReplacementNode(LockableNode<E> node) {
        if (node.left != null) {
            return findMaxInLeftSubTree(node);
        } else if (node.right != null) {
            return findMinInRightSubTree(node);
        } else {
            return null;
        }
    }

    private LockableNode<E> findMaxInLeftSubTree(LockableNode<E> subtree) {
        LockableNode<E> parent = subtree;
        LockableNode<E> curr = subtree.left;
        curr.lock();
        while (curr.right != null) {
            if (parent != subtree) {
                parent.unlock();
            }
            parent= curr;
            curr = curr.right;
            curr.lock();
        }
        if (curr.left != null) {
            curr.left.lock();
        }
        if (parent == subtree) {
            parent.left = curr.left;
        } else {
            parent.right = curr.left;
            parent.unlock();
        }
        if (curr.left != null) {
            curr.left.unlock();
        }
        curr.unlock();
        return curr;
    }

    private LockableNode<E> findMinInRightSubTree(LockableNode<E> node) {
        LockableNode<E> parent = node;
        LockableNode<E> curr = node.right;
        curr.lock();
        while (curr.left != null) {
            if (parent != node) {
                parent.unlock();
            }
            parent = curr;
            curr = curr.left;
            curr.lock();
        }
        if (curr.right != null) {
            curr.right.lock();
        }
        if (parent == node) {
            parent.right = curr.right;
        } else {
            parent.left = curr.right;
            parent.unlock();
        }
        if (curr.right != null) {
            curr.right.unlock();
        }
        curr.unlock();
        return curr;
    }

    @Override
    public boolean contains(E element) {
        LockableNode<E> curNode = null;
        LockableNode<E> parentNode = null;
        rootLock.lock();
        if (root == null) {
            rootLock.unlock();
            return false;
        } else {
            curNode = root;
            curNode.lock();
            rootLock.unlock();
            while (curNode != null) {
                parentNode = curNode;
                if (curNode.element.compareTo(element) > 0) {
                    curNode = curNode.left;
                } else if (curNode.element.compareTo(element) < 0) {
                    curNode = curNode.right;
                } else {
                    curNode.unlock();
                    return true;
                }
                if (curNode == null) {
                    break;
                } else {
                    curNode.lock();
                    parentNode.unlock();
                }
            }
        }
        parentNode.unlock();
        return false;
    }
}

package parallelbsts.sequential;

import parallelbsts.BST;

public class LinkedNodeBSTNoRecursion<E extends Comparable<E>> implements BST<E> {

    protected Node<E> root;



    @Override
    public boolean add(E element) {
        if (root == null) {
            root = new Node<E>(element);
            return true;
        }
        Node<E> curr = root;
        Node<E> parent = null;
        while (curr != null) {
            parent = curr;
            if (curr.element.compareTo(element) == 0) {
                return false;
            } else if (curr.element.compareTo(element) > 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (parent.element.compareTo(element) > 0) {
            parent.left = new Node<>(element);
        } else {
            parent.right = new Node<>(element);
        }
        return true;
    }

    @Override
    public boolean remove(E element) {
        Node<E> curr = root;
        Node<E> parent = null;
        while (curr != null && curr.element.compareTo(element) != 0) {
            parent = curr;
            if (curr.element.compareTo(element) > 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        if (curr == null) {
            return false;
        }
        if (parent == null) {
            root = deleteNode(root);
        } else if (parent.element.compareTo(element) > 0) {
            parent.left = deleteNode(parent.left);
        } else {
            parent.right = deleteNode(parent.right);
        }
        return true;
    }

    private Node<E> findMinNode(Node<E> subtree) {
        Node<E> curr = subtree;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }

    private Node<E> removeMinNode(Node<E> subtree) {
        Node<E> curr = subtree;
        Node<E> parent = null;
        while (curr.left != null) {
            parent = curr;
            curr = curr.left;
        }
        if (parent == null) {
            return curr.right;
        } else {
            parent.left = curr.right;
            return subtree;
        }
    }

    private Node<E> removeMaxNode(Node<E> subtree) {
        Node<E> curr = subtree;
        Node<E> parent = null;
        while (curr.right != null) {
            parent = curr;
            curr = curr.right;
        }
        if (parent == null) {
            return curr.left;
        } else {
            parent.right = curr.left;
            return subtree;
        }
    }

    private Node<E> findMaxNode(Node<E> subtree) {
        Node<E> curr = subtree;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr;
    }

    private Node<E> deleteNode(Node<E> node) {
        if (node.right != null) {
            Node<E> replacementNode =findMinNode(node.right);
            replacementNode.right = removeMinNode(node.right);
            replacementNode.left = node.left;
            return replacementNode;
        } else if (node.left != null) {
            Node<E> replacementNode = findMaxNode(node.left);
            replacementNode.right = node.right;
            replacementNode.left = removeMaxNode(node.left);
            return replacementNode;
        }
        return null;
    }

    @Override
    public boolean contains(E element) {
        Node<E> curr = root;
        while (curr != null) {
            if (curr.element.compareTo(element) == 0) {
                return true;
            } else if (curr.element.compareTo(element) > 0){
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return false;
    }

    static class Node<E> {

        private E element;
        private Node<E> left;
        private Node<E> right;

        public Node(E element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return "N[" + element + "]";
        }
    }
}

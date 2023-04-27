package entities;

import java.util.Objects;

public class Node<T> {


    public Node<T> prev;
    public T data;
    public Node<T> next;

    public Node(Node<T> prev, T data, Node<T> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(prev, node.prev) && Objects.equals(data, node.data) && Objects.equals(next, node.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prev, data, next);
    }
}

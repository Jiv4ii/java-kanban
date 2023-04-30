package logic;

import entities.Node;
import entities.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList {
    private Node<Task> first;
    private Node<Task> last;
    private int size;
    private final HashMap<Integer,Node<Task>> map = new HashMap<>();


    public int getSize() {
        return size;
    }

    void linkLast(Task element) {
        final Node<Task> oldLast = last;
        final Node<Task> newNode = new Node<>(oldLast, element, null);
        last = newNode;
        if (oldLast == null) {
            first = newNode;
        }
        else {
            oldLast.setNext(newNode);
        }
        size++;

    }

    void add(Task element){
        linkLast(element);
        removeNode(map.get(element.getId()));
        map.put(element.getId(),last);
    }

    void removeNode(Node<Task> x) {
        if (x != null) {

            Node<Task> next = x.getNext();
            Node<Task> prev = x.getPrev();

            if (prev == null) {
                first = next;
            } else {
                prev.setNext(next);
            }

            if (next == null) {
                last = prev;
            } else {
                next.setPrev(prev);
            }
            size--;
        }
    }


    public List<Task> getTasks() {
        ArrayList<Task> list = new ArrayList<>();
        Node<Task> link = first;
        while (!link.equals(last)){
            list.add(link.getData());
            link = link.getNext();
        }
        list.add(last.getData());
        return list;
    }

    public Node<Task> getNode(Task task){
        return map.get(task.getId());
    }
}

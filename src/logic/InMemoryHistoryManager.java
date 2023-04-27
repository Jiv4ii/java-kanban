package logic;

import entities.Task;
import entities.Node;

import java.util.ArrayList;

import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {


    private final CustomLinkedList history;

    public InMemoryHistoryManager() {
        history = new CustomLinkedList();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public void remove(Task task) {
        history.removeNode(history.map.get(task.getId()));
    }
}

class CustomLinkedList {
    private Node<Task> first;
    private Node<Task> last;
    public int size;
    public HashMap<Integer,Node<Task>> map = new HashMap<>();





    void linkLast(Task e) {
        final Node<Task> oldLast = last;
        final Node<Task> newNode = new Node<>(oldLast, e, null);
        last = newNode;
        if (oldLast == null)
            first = newNode;
        else
            oldLast.next = newNode;
        size++;

    }

    void add(Task element){
        linkLast(element);
        removeNode(map.get(element.getId()));
        map.put(element.getId(),last);
    }

    void removeNode(Node<Task> x) {
        if (x != null) {

            Node<Task> next = x.next;
            Node<Task> prev = x.prev;

            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.data = null;
            size--;
        }
        }


    public ArrayList<Task> getTasks() {
         ArrayList<Task> list = new ArrayList<>();
        Node<Task> link = last;
        while (!link.equals(first)){
            list.add(link.data);
            link = link.prev;
        }
        list.add(first.data);
        return list;
    }

}


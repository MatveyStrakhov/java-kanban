package manager;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    //private int size = 0;

    @Override
    public void historyAdd(Task task) {
        historyMap.put(task.getTaskID(), linkLast(task));
    }

    @Override
    public void historyRemove(int taskID) {
        if (historyMap.containsKey(taskID)) {
            if (historyMap.get(taskID).getNext() == null) {
                tail = tail.getPrev();
                historyMap.get(taskID).getPrev().setNext(null);
            } else if (historyMap.get(taskID).getPrev() == null) {
                head = head.getNext();
                historyMap.get(taskID).getNext().setPrev(null);
            } else {
                historyMap.get(taskID).getPrev().setNext(historyMap.get(taskID).getNext().getPrev());
                historyMap.get(taskID).getNext().setPrev(historyMap.get(taskID).getPrev().getNext());
            }
            historyMap.remove(taskID);
        }

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    private Node<Task> linkLast(Task task) {
        if (getTasks().isEmpty()) {
            head = new Node<>(task, null, null);
            return head;
        } else if (getTasks().size() == 1) {
            tail = new Node<>(task, head, null);
            tail.getPrev().setNext(tail);
            return tail;
        } else {
            Node<Task> newTail = new Node<>(task, tail, null);
            newTail.getPrev().setData(tail.getData());
            newTail.getPrev().setNext(newTail);
            tail = newTail;
            return newTail;
        }
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> currNode = head;
        while (currNode != null) {
            if (historyMap.containsValue(currNode)) {
                tasks.add(currNode.getData());
            }
            currNode = currNode.getNext();
        }
        return tasks;
    }
}


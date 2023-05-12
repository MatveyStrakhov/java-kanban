package manager;

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
            if (historyMap.get(taskID).next == null) {
                tail = tail.prev;
                historyMap.get(taskID).prev.next = null;
            } else if (historyMap.get(taskID).prev == null) {
                head = head.next;
                historyMap.get(taskID).next.prev = null;
            } else {
                historyMap.get(taskID).prev.next = historyMap.get(taskID).next.prev;
                historyMap.get(taskID).next.prev = historyMap.get(taskID).prev.next;
            }
            historyMap.remove(taskID);
        }

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    public Node<Task> linkLast(Task task) {
        if (getTasks().isEmpty()) {
            head = new Node<>(task, null, null);
            return head;
        } else if (getTasks().size() == 1) {
            tail = new Node<>(task, head, null);
            tail.prev.next = tail;
            return tail;
        } else {
            Node<Task> newTail = new Node<>(task, tail, null);
            newTail.prev.data = tail.data;
            newTail.prev.next = newTail;
            tail = newTail;
            return newTail;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> currNode = head;
        while (currNode != null) {
            if (historyMap.containsValue(currNode)) {
                tasks.add(currNode.data);
            }
            currNode = currNode.next;
        }
        return tasks;
    }
}


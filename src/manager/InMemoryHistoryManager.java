package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> hystoryMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void historyAdd(Task task) {
        hystoryMap.put(task.getTaskID(), linkLast(task));
    }

    @Override
    public void historyRemove(int taskID) {
        if (hystoryMap.containsKey(taskID)) {
            if (hystoryMap.get(taskID).next == null) {
                tail = tail.prev;
                hystoryMap.get(taskID).prev.next = null;
                hystoryMap.remove(taskID);
            } else if (hystoryMap.get(taskID).prev == null) {
                head = head.next;
                hystoryMap.get(taskID).next.prev = null;
                hystoryMap.remove(taskID);
            } else {
                hystoryMap.get(taskID).prev.next = hystoryMap.get(taskID).next.prev;
                hystoryMap.remove(taskID);
            }
        }

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    public Node<Task> linkLast(Task task) {
        if (size == 0) {
            head = new Node<>(task, null, null);
            size++;
            return head;
        } else if (size == 1) {
            tail = new Node<>(task, head, null);
            tail.prev.next = tail;
            size++;
            return tail;
        } else if (size <= 10) {
            Node<Task> newTail = new Node<>(task, tail, null);
            newTail.prev.data = tail.data;
            newTail.prev.next = newTail;
            tail = newTail;
            size++;
            return newTail;
        } else {
            Node<Task> newTail = new Node<>(task, tail, null);
            newTail.prev.data = tail.data;
            newTail.prev.next = newTail;
            tail = newTail;
            head = head.next;
            return newTail;
        }
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> currNode = head;
        while (currNode != null) {
            if (hystoryMap.containsValue(currNode)) {
                tasks.add(currNode.data);
            }
            currNode = currNode.next;
        }
        return tasks;
    }
}


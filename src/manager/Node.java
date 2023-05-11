package manager;

public class Node<T> {
    public T data;
    public Node<T> prev;
    public Node<T> next;
    public int nodeId;
    public Node(T data, Node<T> prev, Node<T> next){
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

}

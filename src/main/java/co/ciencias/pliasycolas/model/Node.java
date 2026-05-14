package co.ciencias.pliasycolas.model;

public class Node<T> {
    private Node<T> next;
    private T value;
    private String name;

    public Node() {
        name = "";
        next = null;
        value = null;
    }
    public Node<T> getNext() {
        return next;
    }
    public void setNext(Node<T> next) {
        this.next = next;
    }
    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return "Node{" +
               "name='" + name + '\'' +
               ", value=" + value +
               '}';
    }
    
    
}
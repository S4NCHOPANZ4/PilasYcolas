package co.ciencias.pliasycolas.model;

public class Stack {
    private Node<Character> top;
    
    public Stack(){
        top = null;
    }
    
    public void push(Character value){
        Node<Character> newNode = new Node();
        newNode.setValue(value);
        newNode.setNext(top);
        this.top = newNode;
    }
    
    public Node<Character> pop(){
        if(isEmpty()){
            return null;
        }
        Node<Character> deleted = this.top;
        this.top = this.top.getNext();
        return deleted;
    }
    
    public Node<Character> peek(){
        return top;
    }
    
    public Boolean isEmpty(){
        if(top == null){
            return true;
        }
        return false;
    }
    
}

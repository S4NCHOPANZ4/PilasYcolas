package co.ciencias.pliasycolas.model;

public class Queue {
    private Node<Integer> front;
    private Node<Integer> back;
    
    public Queue(){
        this.front = null;
        this.back = null;
    }
    
    public void inQueue(int value, String name){
        Node<Integer> newNode = new Node();
        newNode.setName(name);
        newNode.setValue(value);
        if(isEmpty()){
            this.front = newNode;
            this.back = newNode;
        }
        else{
            this.back.setNext(newNode);
            this.back = newNode;
        }
    }
    
    
    //Busca poner siempre los nodos en orden 1 <- 2 <- 3 <- 4 <- 5  (basandonosen la formula de tiempo promedio de finalizacion)
                                          // ^              ^
                                          //front           back
    public void prioInQueue(int value, String name){
        Node<Integer> newNode = new Node();
        newNode.setName(name);
        newNode.setValue(value);
        if(isEmpty()){
            this.front = newNode;
            this.back = newNode;
            return;
        }
        
        if(value < front.getValue()){
            newNode.setNext(front);
            this.front = newNode;
            return;
        }
        
        Node<Integer> current = this.front;
        while(current.getNext() != null && value > current.getNext().getValue() ){
           current = current.getNext();
        }
        newNode.setNext(current.getNext());
        current.setNext(newNode);
        
        if(newNode.getNext() == null){
            this.back = newNode;
        }
        
    }
    
    public Node<Integer> deQueue(){
        if(isEmpty()){
            return null;
        }
        Node<Integer> deletedValue = front;
        this.front = this.front.getNext();
        
        if(this.front == null){
            this.back = null;
        }
        
        return deletedValue;
    
    }
    
    public boolean isEmpty(){
        return this.front == null;
    }
    
    
}

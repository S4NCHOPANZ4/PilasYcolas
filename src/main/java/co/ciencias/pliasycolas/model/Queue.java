package co.ciencias.pliasycolas.model;

/**
 * Representa una estructura de datos de tipo Cola (Queue).
 */
public class Queue {
    
    private Node<Integer> front;        /** Referencia al primer nodo */
    private Node<Integer> back;       /** Referencia al último nodo*/
    
    
    /**
     * Constructor .
     * Inicializa una cola vacía con los punteros front y back en {@code null}.
     */
    public Queue(){
        this.front = null;
        this.back = null;
    }
    
    /**
     * Inserta un nuevo elemento al final de la cola (FIFO).
     * 
     * @param value Valor entero que se almacenará en el nodo.
     * @param name Nombre o identificador para el nuevo nodo.
     */
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
    
    /**
     * Inserta un nuevo elemento en orden de menor a mayor según su valor.
     * los valores menores al principio.
     * 
     * @param value Valor entero que determina la prioridad.
     * @param name Nombre o identificador para el nuevo nodo.
     */
    public void prioInQueue(int value, String name){
        Node<Integer> newNode = new Node();
        newNode.setName(name);
        newNode.setValue(value);
        if(isEmpty()){
            this.front = newNode;
            this.back = newNode;
            return;
        }
        
        if(value < front.getValue()){   // si  nuevo valor es menor que el que está al frente 
            newNode.setNext(front);
            this.front = newNode;
            return;
        }
        
        Node<Integer> current = this.front;
        while(current.getNext() != null && value > current.getNext().getValue() ){      // Recorre la cola  para encontrar la posición 
           current = current.getNext();
        }
        newNode.setNext(current.getNext());
        current.setNext(newNode);
        
        if(newNode.getNext() == null){      // Actualiza el back si el nodo quedó al final
            this.back = newNode;
        }
        
    }
    
    /**
     * Elimina y retorna el nodo que se encuentra al frente de la cola.
     * 
     * @return El {@link Node} que estaba al frente, o {@code null} si la cola está vacía.
     */
    public Node<Integer> deQueue(){
        if(isEmpty()){
            return null;
        }
        Node<Integer> deletedValue = front;
        this.front = this.front.getNext();
        
        if(this.front == null){         // Si la cola queda vacía, el puntero back también 
            this.back = null;
        }
        
        return deletedValue;
    
    }
    
    /**
     * Verifica si la cola esta vacia
     * 
     * @return {@code true} si la cola está vacía, {@code false} si no.
     */
    public boolean isEmpty(){
        return this.front == null;
    } 
    
}

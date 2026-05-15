package co.ciencias.pliasycolas.model;

/**
 * Representa una estructura de datos de tipo Pila (Stack) orientada a caracteres.
 * el primero en ingresar es el primero en ser retirado.
 */
public class Stack {
        
    private Node<Character> top;     //nodo superior de la pila
    
    /**
     * Constructor.
     * Inicializa una pila vacía con el tope apuntando a {@code null}.
     */
    public Stack(){
        top = null;        
    }
    
    /**
     * Agrega un nuevo carácter al tope de la pila.
     * 
     * @param value Carácter a almacenar en la pila.
     */
    public void push(Character value){
        Node<Character> newNode = new Node();
        newNode.setValue(value);
        newNode.setNext(top);
        this.top = newNode;
    }
    
    /**
     * Elimina y devuelve el nodo que se encuentra en el tope .
     * 
     * @return El {@link Node} removido, o {@code null} si la pila está vacía.
     */
    public Node<Character> pop(){
        if(isEmpty()){
            return null;
        }
        Node<Character> deleted = this.top;
        this.top = this.top.getNext();
        return deleted;
    }
    
    /**
     * Permite observar el nodo en el tope de la pila sin eliminarlo.
     * 
     * @return El {@link Node} actual en el tope, o {@code null} si está vacía.
     */
    public Node<Character> peek(){
        return top;
    }
    
    /**
     * Verifica si la pila contiene elementos o si está vacía.
     * 
     * @return {@code true} si el tope es nulo; {@code false} si no.
     */
    public Boolean isEmpty(){
        if(top == null){
            return true;
        }
        return false;
    }
    
}

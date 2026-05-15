package co.ciencias.pliasycolas.model;


/**
 * Representa un nodo genérico para pilas o colas.
 * Cada nodo contiene un valor de tipo genérico, una referencia al siguiente nodo y un nombre identificador.
 *
 * @param <T> El tipo de dato que almacenará el nodo.
 */
public class Node<T> {
    private Node<T> next;           /** siguiente nodo en la estructura. */
    private T value;                      /** dato almacenado en el nodo. */
    private String name;             /** Nombre del nodo. */
    
    /**
     * Constructor por defecto. 
     * Inicializa el nombre como una cadena vacía y las referencias de valor y siguiente como nulas.
     */
    public Node() {
        name = "";
        next = null;
        value = null;
    }
    
    // GETTERS 
    
    public Node<T> getNext() {
        return next;
    }
    
    public T getValue() {
        return value;
    }
    
    public String getName(){
        return name;
    }
    
    // SETTERS
    
    public void setNext(Node<T> next) {
        this.next = next;
    }
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    /// TO STRING
    
    /**
     * Devuelve una representación en cadena de texto del nodo, 
     * incluyendo su nombre y el valor que contiene.
     * @return Una cadena descriptiva de los atributos del nodo.
     */
    @Override
    public String toString() {
        return "Node{" +
               "name='" + name + '\'' +
               ", value=" + value +
               '}';
    }
    
    
}
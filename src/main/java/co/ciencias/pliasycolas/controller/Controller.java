package co.ciencias.pliasycolas.controller;
 
import co.ciencias.pliasycolas.model.Node;
import co.ciencias.pliasycolas.model.Queue;
import co.ciencias.pliasycolas.model.Stack;
import co.ciencias.pliasycolas.view.VistaGUI;
 
/**
 * Control para la interacción entre la interfaz gráfica y las Pilas y Colas 
 * Contiene la lógica de validación de expresiones y análisis de tiempos de procesamiento.
 *
 */
public class Controller {
 
    private final Stack    stack = new Stack();
    private final Queue    queue = new Queue();
    private final VistaGUI vc    = new VistaGUI(stack, queue, this);
 
    /**
     * Constructor del controlador. 
     * Inicia la ejecución de la lógica principal del programa.
     */
    public Controller() { run(); }
    
    /**
     * Valida si un string tiene símbolos de agrupación balanceados.
     * Utiliza una pila para verificar que cada paréntesis, corchete o llave de apertura 
     * tenga su correspondiente cierre en el orden correcto.
     * 
     * @param s La cadena de texto que contiene la expresión simbólica.
     * @return {@code true} si la expresión está balanceada, {@code false} de lo contrario.
     */
    public boolean symbolicEq(String s) {
        Stack st = new Stack();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ("([{".indexOf(c) != -1) {   // Si es un símbolo de apertura, se apila
                st.push(c);
            } else if (")]}".indexOf(c) != -1) {    // Si es un símbolo de cierre, se verifica el tope de la pila
                if (st.isEmpty()) return false;
                char top = st.peek().getValue();
                if ((top == '(' && c == ')') || (top == '[' && c == ']') || (top == '{' && c == '}')) st.pop();
                else return false;
            }
        }
        return st.isEmpty();
    }
    
    /**
     * Calcula y muestra el tiempo promedio de finalización de las tareas en una cola.
     * Utiliza una cola auxiliar para preservar la integridad de los datos originales 
     * mientras realiza el cálculo acumulativo.
     * 
     * @param q La cola de tareas .
     */
    public void avgCompTime(Queue q) {
        int sum = 0, count = 0;
        
        Queue auxQueue = new Queue(); // se crea una cola temporal
        
        while (!q.isEmpty()) {
            Node<Integer> n = q.deQueue();
            sum += n.getValue();
            count++;
            
            auxQueue.inQueue(n.getValue(),n.getName());  //Guarda la tarea en la cola temporal para no perder la cola
            
             vc.mostrarInformacion(n.getName() + " Tiempo Acumulado =" + sum);
        }
        
        while (!auxQueue.isEmpty()) {   //hasta que la auxiliar no este vacia, se devuelven las tareas a la original
            Node<Integer> aux = auxQueue.deQueue();  //se crea una tarea temporal
            q.inQueue(aux.getValue(),aux.getName());    // se va llenando la cola original nuevamente
        }
        
        vc.mostrarInformacion(" Total Tareas =" + count);
        if (count > 0)
            
            vc.mostrarInformacion("Tiempo medio: " + (double) sum / count);
    }
    
    /**
     * Método que inicializa la ejecución 
     */
    private void run() {
        Queue q = new Queue();
        avgCompTime(q);
    }
}

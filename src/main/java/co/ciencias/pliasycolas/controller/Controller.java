package co.ciencias.pliasycolas.controller;
 
import co.ciencias.pliasycolas.model.Node;
import co.ciencias.pliasycolas.model.Queue;
import co.ciencias.pliasycolas.model.Stack;
import co.ciencias.pliasycolas.view.VistaGUI;
 
public class Controller {
 
    private final Stack    stack = new Stack();
    private final Queue    queue = new Queue();
    private final VistaGUI vc    = new VistaGUI(stack, queue, this);
 
    public Controller() { run(); }
 
    public boolean symbolicEq(String s) {
        Stack st = new Stack();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ("([{".indexOf(c) != -1) {
                st.push(c);
            } else if (")]}".indexOf(c) != -1) {
                if (st.isEmpty()) return false;
                char top = st.peek().getValue();
                if ((top == '(' && c == ')') || (top == '[' && c == ']') || (top == '{' && c == '}')) st.pop();
                else return false;
            }
        }
        return st.isEmpty();
    }
 
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
 
    private void run() {
        Queue q = new Queue();
        avgCompTime(q);
    }
}

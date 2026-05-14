package co.ciencias.pliasycolas.controller;
import co.ciencias.pliasycolas.model.Node;
import co.ciencias.pliasycolas.model.Queue;
import co.ciencias.pliasycolas.model.Stack;
import co.ciencias.pliasycolas.view.VistaConsola;

public class Controller {
    VistaConsola vc = new VistaConsola();
    public Boolean symbolicEq(String data){
        Stack pila = new Stack();
        String opening = "[(";
        String closing = "])";
        for(int i = 0; i < data.length(); i++){
            char c = data.charAt(i);
            if(opening.indexOf(c) != -1) pila.push(c);
            else if(closing.indexOf(c) != -1){
                if(pila.isEmpty()) return false;
                char top = pila.peek().getValue();
                if(top == '[' && c == ']' || top == '(' && c == ')') pila.pop();
                else return false;
            }
        }
        return pila.isEmpty();
            // [()]  ->   [  -> pila: [ -> ( pila: [ ( ->  ) -> pila: [ -> ] -> pila: null -> pila vacia EXITO!
                       // ^ cima         ^ cima           ^ cima   
    }

    public void avgCompTime(Queue tasks){
        int acum = 0;
        int totalComp = 0;
        int n = 0;
        while(!tasks.isEmpty()){
            Node<Integer> task = tasks.deQueue();
            acum += task.getValue();
            n++;
            vc.mostrarInformacion(task.getName() + " termina en: " + acum);
        }
        double avg = (double) acum / n;
        
        vc.mostrarInformacion("Tiempo en para completar: " + avg);
    } 
    
    public void run(){ 
        vc.mostrarInformacion(symbolicEq("[()]")); 
        
        Queue tasks  = new Queue();
        tasks.prioInQueue(90, "A");
        tasks.prioInQueue(20, "B");
        tasks.prioInQueue(81, "C");
        tasks.prioInQueue(50, "D");
        
        avgCompTime(tasks);
    }
}


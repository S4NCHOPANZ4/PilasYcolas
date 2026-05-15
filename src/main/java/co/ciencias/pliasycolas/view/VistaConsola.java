package co.ciencias.pliasycolas.view;
import java.util.Scanner;

/**
 *  métodos básicos para la interacción con el usuario mediante la consola.
 */
public class VistaConsola {
    
    private Scanner sc;     /** Objeto Scanner para la lectura desde el teclado */
    
    /**
     * Constructor.
     * Inicializa la entrada de datos
     */
    public VistaConsola(){
        sc = new Scanner(System.in);
        
    }  
    
    /**
     * Imprime un objeto o mensaje en la consola estándar.
     * 
     * @param msj El objeto o mensaje que se desea mostrar al usuario.
     */
    public void mostrarInformacion(Object msj){
        System.out.println(msj);
    }
    
    /**
     * Solicita un dato al usuario a través de un mensaje y lee la respuesta desde la consola.
     * 
     * @param msj El mensaje descriptivo de lo que se solicita al usuario.
     * @return Una {@link String} con el dato ingresado por el usuario.
     */
    public String leerDato(String msj){
        System.out.println(msj);
        String dato = sc.nextLine();
        return dato;
    }
}
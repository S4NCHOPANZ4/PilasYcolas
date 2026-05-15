package co.ciencias.pliasycolas.view;
 
import co.ciencias.pliasycolas.controller.Controller;
import co.ciencias.pliasycolas.model.Queue;
import co.ciencias.pliasycolas.model.Stack;
 
import java.awt.*;
import javax.swing.*;
 
/**
 * interfaz gráfica de usuario para visualizar y manipular las estructuras de datos de Pilas y Colas.
 */
public class VistaGUI extends VistaConsola {
 
    private final StackPanel stackPanel;                /** Panel para la representación gráfica de la Pila. */
    private final QueuePanel queuePanel;            /** Panel para la representación gráfica de la Cola. */
    private final JTextArea  logArea;                       /** Área de texto para mostrar resultados de operaciones. */
    
    /**
     * Constructor de la vista gráfica.
     * Configura la ventana principal (JFrame), inicializa los paneles de las estructuras 
     * y establece la disposición de los componentes (Layout).
     * 
     * @param stack La instancia de la Pila a visualizar.
     * @param queue La instancia de la Cola a visualizar.
     * @param ctrl  Referencia al controlador para gestionar eventos.
     */
    public VistaGUI(Stack stack, Queue queue, Controller ctrl) {
        stackPanel = new StackPanel(stack, ctrl);
        queuePanel = new QueuePanel(queue, ctrl);
 
        logArea = new JTextArea(5, 55);     // Configuración del área de log
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        queuePanel.setLogFn(this::log);     // Inyección de la función de log en el panel de colas
 
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Salida"));
        
        // Organización de paneles centrales
        JPanel center = new JPanel(new BorderLayout(6, 6));     
        center.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
        center.add(stackPanel, BorderLayout.WEST);
        center.add(queuePanel, BorderLayout.CENTER);
        
        // Configuración del Frame
        JFrame frame = new JFrame("Pilas y Colas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(6, 6));
        frame.add(center,    BorderLayout.CENTER);
        frame.add(logScroll, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Muestra información al usuario actualizando la consola y  la GUI.
     * 
     * @param o Objeto o mensaje a mostrar.
     */
    @Override
    public void mostrarInformacion(Object o) {
        super.mostrarInformacion(o);
        SwingUtilities.invokeLater(() -> {
            log(String.valueOf(o));
            stackPanel.repaintCanvas();
            queuePanel.repaintCanvas();
        });
    }
    
    /**
     * Añade un mensaje al área de texto de la interfaz y desplaza el scroll al final.
     * 
     * @param msg El mensaje o registro de actividad a añadir.
     */
    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
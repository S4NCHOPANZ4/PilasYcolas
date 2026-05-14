package co.ciencias.pliasycolas.view;
 
import co.ciencias.pliasycolas.controller.Controller;
import co.ciencias.pliasycolas.model.Queue;
import co.ciencias.pliasycolas.model.Stack;
 
import java.awt.*;
import javax.swing.*;
 
public class VistaGUI extends VistaConsola {
 
    private final StackPanel stackPanel;
    private final QueuePanel queuePanel;
    private final JTextArea  logArea;
 
    public VistaGUI(Stack stack, Queue queue, Controller ctrl) {
        stackPanel = new StackPanel(stack, ctrl);
        queuePanel = new QueuePanel(queue, ctrl);
 
        logArea = new JTextArea(5, 55);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        queuePanel.setLogFn(this::log);
 
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Salida"));
 
        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));
        center.add(stackPanel, BorderLayout.WEST);
        center.add(queuePanel, BorderLayout.CENTER);
 
        JFrame frame = new JFrame("Pilas y Colas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(6, 6));
        frame.add(center,    BorderLayout.CENTER);
        frame.add(logScroll, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 
    @Override
    public void mostrarInformacion(Object o) {
        super.mostrarInformacion(o);
        SwingUtilities.invokeLater(() -> {
            log(String.valueOf(o));
            stackPanel.repaintCanvas();
            queuePanel.repaintCanvas();
        });
    }
 
    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
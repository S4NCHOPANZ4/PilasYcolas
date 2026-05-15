package co.ciencias.pliasycolas.view;
 
import co.ciencias.pliasycolas.controller.Controller;
import co.ciencias.pliasycolas.model.Node;
import co.ciencias.pliasycolas.model.Queue;
 
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
 
/**
 * Panel de la interfaz gráfica diseñado para la gestión visual de una Cola con Prioridad.
 *
 */
public class QueuePanel extends JPanel {
 
    private  Queue      queue;
    private  Controller ctrl;
    private  QueueCanvas canvas;
    private  JTextField  taskName;
    private  JTextField  taskTime;
 
    private Consumer<String> logFn;
    
    /**
     * Establece la función de registro para la salida de mensajes.
     * @param fn  recibirá los mensajes de log.
     */
    public void setLogFn(Consumer<String> fn) { this.logFn = fn; }
    
    /**
     * Envía un mensaje al log si la función ha sido definida.
     * @param msg Mensaje a registrar.
     */
    private void log(String msg) { if (logFn != null) logFn.accept(msg); }
    
    /**
     * Constructor de QueuePanel.
     * Configura la disposición de los componentes, inicializa el canvas y define
     * los eventos de los botones para encolar, desencolar y procesar.
     * 
     * @param queue Instancia de {@link Queue} .
     * @param ctrl  Instancia de {@link Controller} para procesos 
     */
    public QueuePanel(Queue queue, Controller ctrl) {
        this.queue = queue;
        this.ctrl  = ctrl;
 
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createTitledBorder("Cola (prioridad por tiempo)"));
 
        canvas = new QueueCanvas();
        canvas.setPreferredSize(new Dimension(400, 100));
 
        taskName = new JTextField(7);
        taskTime = new JTextField(5);
 
        JButton enqBtn  = new JButton("Encolar");
        JButton deqBtn  = new JButton("Desencolar");
        JButton procBtn = new JButton("Procesar todo");
 
        enqBtn.addActionListener(e -> {
            String name = taskName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
                taskName.requestFocus();
                return;
            }
            try {
                int v = Integer.parseInt(taskTime.getText().trim());
                queue.prioInQueue(v, name);
                taskName.setText(""); taskTime.setText("");
                canvas.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingresa un tiempo numérico válido.", "Tiempo inválido", JOptionPane.WARNING_MESSAGE);
            }
        });
 
        deqBtn.addActionListener(e -> {
            Node<Integer> n = queue.deQueue();
            log(n != null ? "Dequeue: " + n.getName() + " t=" + n.getValue() : "Cola vacía");
            canvas.repaint();
        });
 
        procBtn.addActionListener(e -> {
            ctrl.avgCompTime(queue);
            canvas.repaint();
        });
 
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        controls.add(new JLabel("Nombre*:")); controls.add(taskName);
        controls.add(new JLabel("Tiempo:"));  controls.add(taskTime);
        controls.add(enqBtn); controls.add(deqBtn);
 
        JPanel controls2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        controls2.add(procBtn);
 
        JPanel south = new JPanel(new BorderLayout());
        south.add(controls,  BorderLayout.NORTH);
        south.add(controls2, BorderLayout.SOUTH);
 
        add(canvas, BorderLayout.CENTER);
        add(south,  BorderLayout.SOUTH);
    }
    
    /**
     * Refresca la representación visual de la cola en el lienzo.
     */
    public void repaintCanvas() { canvas.repaint(); }
 
    // ── QueueCanvas ──────────────────────────────────────────────────────────
    
    /**
     * Clase interna para el renderizado gráfico de la cola.
     * Implementa {@code MouseMotionListener} y {@code MouseListener} para 
     * permitir la interacción directa con los nodos dibujados.
     */
    private class QueueCanvas extends JPanel {
        
        private int hoverIndex = -1;   //  elemento sobre el que está el mouse

        public QueueCanvas() {
            // Detectar movimiento
            addMouseMotionListener(new java.awt.event.MouseAdapter() {
                @Override
                
                public void mouseMoved(java.awt.event.MouseEvent e) {
                    checkHover(e.getPoint());
                }
            });

            // Listener para eliminar
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (hoverIndex != -1) {
                        eliminarNodoPorIndice(hoverIndex);
                        hoverIndex = -1;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hoverIndex = -1;
                    repaint();
                }
            });
        }
        
        /**
         * Determina si el mouse está sobre algún nodo de la cola.
         * @param p Punto de ubicación del mouse.
         */
        private void checkHover(Point p) {
            List<Node<Integer>> elems = queueSnapshot();
            int bW = 64, bH = 40, gap = 6;
            int sx = Math.max(8, (getWidth() - elems.size() * (bW + gap)) / 2);
            int y = (getHeight() - bH) / 2;

            int newHover = -1;
            for (int i = 0; i < elems.size(); i++) {
                int x = sx + i * (bW + gap);
                Rectangle rect = new Rectangle(x, y, bW, bH);
                if (rect.contains(p)) {
                    newHover = i;
                    break;
                }
            }

            if (newHover != hoverIndex) {
                hoverIndex = newHover;
                repaint();
            }
        }
        
        /**
         * Elimina un nodo específico de la cola basándose en su posición visual.
         * Reconstruye la cola omitiendo el elemento seleccionado.
         * 
         * @param index Posición del nodo a eliminar.
         */
        private void eliminarNodoPorIndice(int index) {
            List<Node<Integer>> tempElems = queueSnapshot();
            if (index >= 0 && index < tempElems.size()) {
                // Vacia la cola
                while (!queue.isEmpty()) queue.deQueue();

                // devuelve  todos menos  el eliminado
                for (int i = 0; i < tempElems.size(); i++) {
                    if (i != index) {
                        Node<Integer> n = tempElems.get(i);
                        queue.prioInQueue(n.getValue(), n.getName());
                    }
                }
            }
        }

        @Override
        /**
         * Renderiza los componentes de la cola, incluyendo nodos, flechas 
         * y etiquetas de FRONT/BACK.
         * 
         * @param g Contexto gráfico.
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            List<Node<Integer>> elems = queueSnapshot();
            int W = getWidth(), H = getHeight();
            int bW = 64, bH = 40, gap = 6;
            int sx = Math.max(8, (W - elems.size() * (bW + gap)) / 2);
            int y = (H - bH) / 2;

            if (elems.isEmpty()) {
                g.setColor(Color.GRAY);
                g.drawString("(vacía)", W / 2 - 20, H / 2);
                return;
            }

            for (int i = 0; i < elems.size(); i++) {
                int x = sx + i * (bW + gap);
                Node<Integer> nd = elems.get(i);

                if (i == hoverIndex) {
                    // hover de Botón borrar
                    g.setColor(new Color(255, 69, 58)); // Rojo más vibrante
                    g.fillRoundRect(x, y, bW, bH, 5, 5);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
                    FontMetrics fm = g.getFontMetrics();
                    g.drawString("BORRAR", x + (bW - fm.stringWidth("BORRAR")) / 2, y + (bH / 2) + 5);
                } else {
                    //Dibujo original
                    g.setColor(new Color(220, 220, 220));
                    g.fillRect(x, y, bW, bH);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(x, y, bW, bH);

                    g.setColor(Color.BLACK);
                    g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
                    String nm = nd.getName() != null ? nd.getName() : "?";
                    g.drawString(nm, x + 5, y + 15);

                    g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
                    g.drawString("t=" + nd.getValue(), x + 5, y + 30);
                }

                // flechas
                if (i < elems.size() - 1) {
                    g.setColor(Color.BLACK);
                    g.drawString("→", x + bW + 1, y + bH / 2 + 5);
                }
            }

            // Etiquetas Front/Back y n
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            g.drawString("FRONT", sx, y - 3);
            int lx = sx + (elems.size() - 1) * (bW + gap);
            g.drawString("BACK", lx + bW - 24, y - 3);
            g.drawString("n=" + elems.size(), 4, H - 4);
        }
        
 
          /**
         * Crea una captura (snapshot) del estado actual de la cola sin destruirla.
         * 
         * @return Una lista de nodos con los datos actuales de la cola.
         */
            private List<Node<Integer>> queueSnapshot() {
                
                    List<Node<Integer>> result = new ArrayList<>();
                    Queue auxQueue = new Queue(); // Cola temporal para no perder datos

                    // Pasamos de la original a la lista y a la auxiliar
                    while (!queue.isEmpty()) {
                        Node<Integer> n = queue.deQueue();
                        result.add(n);
                        auxQueue.prioInQueue(n.getValue(), n.getName()); 
                    }

                    //  Restauramos la original como estaba
                    while (!auxQueue.isEmpty()) {
                        Node<Integer> n = auxQueue.deQueue();
                        queue.prioInQueue(n.getValue(), n.getName());
                    }

            return result;
        }
    }
}
 
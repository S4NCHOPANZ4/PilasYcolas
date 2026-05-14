package co.ciencias.pliasycolas.view;
 
import co.ciencias.pliasycolas.controller.Controller;
import co.ciencias.pliasycolas.model.Node;
import co.ciencias.pliasycolas.model.Queue;
 
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
 
public class QueuePanel extends JPanel {
 
    private  Queue      queue;
    private  Controller ctrl;
    private  QueueCanvas canvas;
    private  JTextField  taskName;
    private  JTextField  taskTime;
 
    private Consumer<String> logFn;
    public void setLogFn(Consumer<String> fn) { this.logFn = fn; }
    private void log(String msg) { if (logFn != null) logFn.accept(msg); }
 
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
 
    public void repaintCanvas() { canvas.repaint(); }
 
    // ── QueueCanvas ──────────────────────────────────────────────────────────
    private class QueueCanvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            List<Node<Integer>> elems = queueSnapshot();
            int W = getWidth(), H = getHeight();
            int bW = 64, bH = 40, gap = 6;
            int sx = Math.max(8, (W - elems.size() * (bW + gap)) / 2);
            int y  = (H - bH) / 2;
 
            if (elems.isEmpty()) {
                g.setColor(Color.GRAY);
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
                g.drawString("(vacía)", W / 2 - 20, H / 2);
                return;
            }
 
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            g.drawString("FRONT", sx, y - 3);
 
            for (int i = 0; i < elems.size(); i++) {
                int x = sx + i * (bW + gap);
                Node<Integer> nd = elems.get(i);
                g.setColor(new Color(220, 220, 220));
                g.fillRect(x, y, bW, bH);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, bW, bH);
                g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
                String nm = nd.getName() != null ? nd.getName() : "?";
                FontMetrics fm = g.getFontMetrics();
                g.setColor(Color.BLACK);
                g.drawString(nm, x + (bW - fm.stringWidth(nm)) / 2, y + 15);
                g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
                fm = g.getFontMetrics();
                String vl = "t=" + nd.getValue();
                g.drawString(vl, x + (bW - fm.stringWidth(vl)) / 2, y + 30);
                if (i < elems.size() - 1) {
                    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                    g.drawString("→", x + bW + 1, y + bH / 2 + 5);
                }
            }
            int lx = sx + (elems.size() - 1) * (bW + gap);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            g.drawString("BACK",    lx + bW - 24, y - 3);
            g.drawString("n=" + elems.size(), 4, H - 4);
        }
 
        private List<Node<Integer>> queueSnapshot() {
            List<Node<Integer>> result = new ArrayList<>();
            List<int[]>  bkv = new ArrayList<>();
            List<String> bkn = new ArrayList<>();
            while (!queue.isEmpty()) {
                Node<Integer> n = queue.deQueue();
                result.add(n); bkv.add(new int[]{n.getValue()}); bkn.add(n.getName());
            }
            for (int i = 0; i < bkv.size(); i++) queue.inQueue(bkv.get(i)[0], bkn.get(i));
            return result;
        }
    }
}
 
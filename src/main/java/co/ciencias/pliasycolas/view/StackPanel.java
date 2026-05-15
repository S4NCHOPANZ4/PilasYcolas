package co.ciencias.pliasycolas.view;
 
import co.ciencias.pliasycolas.controller.Controller;
import co.ciencias.pliasycolas.model.Stack;
 
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
 
/**
 * Panel de la interfaz gráfica de la gestión y visualización de la Pila.
 * 
 */
public class StackPanel extends JPanel {
 
    private  Stack      stack;      
    private  Controller ctrl;
    private  StackCanvas canvas;
    private  JTextField  symInput;
    private  JLabel      symResult;
    
    /**
     * Constructor de StackPanel.
     * Configura el layout, los bordes, el lienzo de dibujo y los controles de usuario.
     * 
     * @param stack La instancia de {@link Stack} a representar.
     * @param ctrl  El {@link Controller} que procesará las expresiones.
     */
    public StackPanel(Stack stack, Controller ctrl) {
        this.stack = stack;
        this.ctrl  = ctrl;
 
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createTitledBorder("Pila / Verificar equilibrio"));
 
        canvas = new StackCanvas();
        canvas.setPreferredSize(new Dimension(160, 240));
 
        symInput  = new JTextField(16);
        symResult = new JLabel("—");
        JButton symBtn = new JButton("Comprobar");
 
        symBtn.addActionListener(e -> {
            String expr = symInput.getText().trim();
            if (expr.isEmpty()) return;
            boolean ok = ctrl.symbolicEq(expr);
            symResult.setText(ok ? "✓ Equilibrado" : "✗ No equilibrado");
            symResult.setForeground(ok ? new Color(0, 120, 0) : Color.RED);
            canvas.setHighlightExpr(expr);
            canvas.repaint();
        });
 
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        controls.add(new JLabel("Expresión:"));
        controls.add(symInput);
        controls.add(symBtn);
        controls.add(symResult);
 
        add(canvas,   BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
    }
    
    /**
     * Solicita la actualización visual del lienzo de dibujo de la pila.
     */
    public void repaintCanvas() { canvas.repaint(); }
 
    // ── StackCanvas ──────────────────────────────────────────────────────────
    
    /**
     * Clase interna privada que actúa como lienzo (Canvas) para dibujar la pila.
     * Representa visualmente cada elemento como un bloque apilado desde la base.
     */
    private class StackCanvas extends JPanel {
 
        private String highlightExpr = "";
        
        /**
         * Establece la expresión que debe ser procesada y dibujada.
         * @param expr Cadena de texto con símbolos.
         */
        public void setHighlightExpr(String expr) { this.highlightExpr = expr; }
        
        /**
         * Realiza el dibujo de los componentes de la pila en el panel.
         * Dibuja la base de la pila, los bloques de caracteres y el puntero "TOP".
         * 
         * @param g El contexto gráfico para realizar el dibujo.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int W = getWidth(), H = getHeight();
 
            if (!highlightExpr.isEmpty()) {
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
                g.setColor(Color.DARK_GRAY);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(highlightExpr, (W - fm.stringWidth(highlightExpr)) / 2, 18);
            }
 
            List<Character> sim = simulateStack(highlightExpr);
            int bW = 90, bH = 26, gap = 2;
            int x = (W - bW) / 2, baseY = H - 18;
 
            g.setColor(Color.DARK_GRAY);
            g.fillRect(x - 4, baseY, bW + 8, 4);
 
            if (sim.isEmpty()) {
                g.setColor(Color.GRAY);
                g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
                g.drawString("(vacía)", x + 18, baseY - 10);
                return;
            }
 
            for (int i = 0; i < sim.size(); i++) {
                int y = baseY - (i + 1) * (bH + gap);
                if (y < 24) break;
                boolean isTop = (i == sim.size() - 1);
                g.setColor(isTop ? new Color(180, 210, 255) : new Color(220, 220, 220));
                g.fillRect(x, y, bW, bH);
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, bW, bH);
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
                String lbl = String.valueOf(sim.get(i));
                FontMetrics fm = g.getFontMetrics();
                g.setColor(Color.BLACK);
                g.drawString(lbl, x + (bW - fm.stringWidth(lbl)) / 2,
                             y + (bH + fm.getAscent() - fm.getDescent()) / 2);
                if (isTop) {
                    g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
                    g.setColor(Color.DARK_GRAY);
                    g.drawString("TOP", x + bW + 4, y + bH / 2 + 4);
                }
            }
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            g.setColor(Color.GRAY);
            g.drawString("n=" + sim.size(), x, 34);
        }
        
        /**
         * Simula el comportamiento de una pila basándose en una cadena de caracteres.
         * Recorre la expresión agregando símbolos de apertura y eliminándolos si 
         * encuentran su pareja de cierre.
         * 
         * @param expr Expresión a simular.
         * @return Una lista de caracteres que representa el estado final de la pila.
         */
        private List<Character> simulateStack(String expr) {
            List<Character> result = new ArrayList<>();
            for (int i = 0; i < expr.length(); i++) {
                char c = expr.charAt(i);
                if ("([{".indexOf(c) != -1) {
                    result.add(c);
                } else if (")]}".indexOf(c) != -1 && !result.isEmpty()) {
                    char top = result.get(result.size() - 1);
                    if ((top == '(' && c == ')') || (top == '[' && c == ']') || (top == '{' && c == '}'))
                        result.remove(result.size() - 1);
                }
            }
            return result;
        }
    }
}
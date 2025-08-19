import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ModernButton extends JButton {
    private static final long serialVersionUID = 1L;

    private Color startColor;
    private Color endColor;
    private Color hoverStartColor;
    private Color hoverEndColor;
    private boolean isHovered = false;

    // Default constructor uses the standard blue theme
    public ModernButton(String text, ImageIcon icon) {
        this(text, icon,
             new Color(70, 130, 180), new Color(100, 149, 237), // Standard colors
             new Color(100, 149, 237), new Color(135, 206, 250)  // Standard hover colors
        );
    }
    
    // This is the required constructor that allows for custom colors
    public ModernButton(String text, ImageIcon icon, Color start, Color end, Color hoverStart, Color hoverEnd) {
        super(text);
        setIcon(icon);
        this.startColor = start;
        this.endColor = end;
        this.hoverStartColor = hoverStart;
        this.hoverEndColor = hoverEnd;
        
        setFont(new Font("Arial", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color sColor = isHovered ? hoverStartColor : startColor;
        Color eColor = isHovered ? hoverEndColor : endColor;

        GradientPaint gp = new GradientPaint(0, 0, sColor, 0, getHeight(), eColor);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        super.paintComponent(g);
        g2.dispose();
    }
}
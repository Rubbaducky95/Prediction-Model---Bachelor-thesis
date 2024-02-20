import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class NodePanel extends JPanel {
    private ArrayList<ArrayList<Node>> listOfNodeLists;
    private double scale; // Scale factor for drawing

    public NodePanel(ArrayList<ArrayList<Node>> listOfNodeLists, double scale) {
        this.listOfNodeLists = listOfNodeLists;
        this.scale = scale;
        this.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Arial", Font.BOLD, 12); // Choose an appropriate font and size
        g2d.setFont(font);
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN}; // Add more colors as needed

        for (int i = 0; i < listOfNodeLists.size(); i++) {
            g2d.setColor(colors[i % colors.length]);
            for (Node node : listOfNodeLists.get(i)) {
                // Adjust node size based on velocity
                int size = (int) (node.v * 10); // Example scaling: velocity multiplied by 10
                int x = (int) (node.x * scale) - size / 2; // Scale and center the node
                int y = (int) (node.y * scale) - size / 2; // Scale and center the node
                g2d.drawOval(x, y, size, size); // Use size for both width and height to keep the node circular

                // Draw the node ID inside the circle
                String idText = String.valueOf(node.timeStep);
                // Calculate width and height of the text to center it
                FontMetrics metrics = g2d.getFontMetrics(font);
                int textWidth = metrics.stringWidth(idText);
                int textHeight = metrics.getHeight();
                //g2d.setColor(Color.BLACK); // Text color
                g2d.drawString(idText, x + (size - textWidth) / 2, y + ((size - textHeight) / 2) + metrics.getAscent());
            }
        }
    }


}

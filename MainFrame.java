import javax.swing.*;
import java.util.ArrayList;

// Main Frame class with automatic size adjustment
class MainFrame extends JFrame {
    public MainFrame(ArrayList<ArrayList<Node>> listOfNodeLists, double scale) {
        setTitle("Node Painter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Calculate max dimensions based on nodes
        double maxX = 0, maxY = 0;
        for (ArrayList<Node> nodeList : listOfNodeLists) {
            for (Node node : nodeList) {
                maxX = Math.max(maxX, node.x * scale);
                maxY = Math.max(maxY, node.y * scale);
            }
        }
        int margin = 50; // Additional margin for aesthetics
        setSize((int) maxX + margin, (int) maxY + margin); // Use calculated size

        NodePanel panel = new NodePanel(listOfNodeLists, scale);
        add(panel);

        setLocationRelativeTo(null); // Center the window
    }
}

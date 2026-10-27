package tree;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.rutgers.cs112.BST.BSTNode;


public class Driver {
    private JFrame display;

    private Lanternflies lanternflies;
    private TreeDisplay bstDisplay;

    private static int PANEL_WIDTH = 750;
    private static int PANEL_HEIGHT = 600;

    public Driver(){
        display = new JFrame("Lanternflies");
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setLayout(new BorderLayout());

        lanternflies = new Lanternflies();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        bstDisplay = new TreeDisplay(lanternflies.getRoot(), PANEL_WIDTH, PANEL_HEIGHT);
        mainPanel.add(bstDisplay, BorderLayout.CENTER);
        mainPanel.setBackground(Color.BLUE);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        JButton add = new JButton("New random flower!");
        controlPanel.add(add);
        add.setBackground(Color.CYAN);

        JButton delete = new JButton("Delete lanternfly: ");
        controlPanel.add(delete);
        delete.setBackground(Color.RED);

        JSpinner buttonGCS = new JSpinner(new SpinnerNumberModel(25, 0, 100, 1));
        buttonGCS.addChangeListener((e) -> {
            int inHeatSig = (int) buttonGCS.getValue(); 
            Bug type = Bug.fromTemp(inHeatSig);
            if(type == Bug.BUTTERFLY){
                delete.setBackground(Color.MAGENTA);
                delete.setText("Delete Butterfly");
            } else if (type == Bug.EMPTY) {
                delete.setBackground(Color.GREEN);
                delete.setText("Delete Leaf");
            }  else if (type == Bug.LADYBUG) {
                delete.setBackground(Color.PINK);
                delete.setText("Delete Ladybug");
            } else if (type == Bug.LANTERNFLY) {
                delete.setBackground(Color.RED);
                delete.setText("Delete Lanternfly");
            } else {
                delete.setBackground(Color.LIGHT_GRAY);
            } 
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        controlPanel.add(buttonGCS);
        buttonGCS.setBackground(Color.RED);

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                int random = (int)(Math.random() * 40); 
                lanternflies.add(random); 
                bstDisplay.update(lanternflies.getRoot());
                bstDisplay.revalidate();
                bstDisplay.repaint();
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                int inHeatSig = (int) buttonGCS.getValue(); 
                lanternflies.remove(inHeatSig); 
                bstDisplay.update(lanternflies.getRoot());
                bstDisplay.revalidate();
                bstDisplay.repaint();
            }   
            
        });

        display.add(mainPanel);
        display.setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        display.pack();
        display.setVisible(true);

    }
 


     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Driver();

        });
    }

    
    public static class TreeDisplay extends JPanel {

        private int CANVAS_WIDTH = 1000;

        private int rootY = 10;
    private int NODE_SIZE = 36;  
    private int ROW_HEIGHT = 70; 
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        public Object drawTree(BSTNode<Flower> root, int depth, int index) {
            if (root == null) {
                return null;
            }

            int myX = (int) ((CANVAS_WIDTH * (index)) / (Math.pow(2, depth - 1) + 1));

            String label = root.getData().toString(); 
            String fillColor = "green";

            if(label.contains("🐞")){
                fillColor = "red";
            } else if (label.contains("🕷️")){
                fillColor = "pink";
            }
            else if(label.contains("🦋")){
                fillColor = "purple";
            }
            else if(label.contains("🌳")){
                fillColor = "brown";
            } else if(label.contains("🌺")){
                fillColor = "green";
            }
            
            String style = "fillColor=" + fillColor + ";strokeColor=black;fontColor=black";

            Object rootVertex = graph.insertVertex(parent, null, root.getData().getHeatSignature() + "°C\n" + root.getData().getIcon(), myX, depth * ROW_HEIGHT + rootY, NODE_SIZE,
                    NODE_SIZE,style);

            Object rightChildVertex = drawTree(root.getRight(), depth + 1, index * 2);

            if (rightChildVertex != null) {
                graph.insertEdge(parent, null, "R", rootVertex, rightChildVertex,
                        "startArrow=none;endArrow=none;strokeWidth=2.5;strokeColor=green");
            }

            Object leftChildVertex = drawTree(root.getLeft(), depth + 1, index * 2 - 1);

            if (leftChildVertex != null) {
                graph.insertEdge(parent, null, "L", rootVertex, leftChildVertex,
                        "startArrow=none;endArrow=none;strokeWidth=2.5;strokeColor=green");
            }

            return rootVertex;
        }

        /**
         * Redraw the whole tree
         *
         * @param root the root of tree to be drawn
         */
        public void update(BSTNode<Flower> root) {
            graph.getModel().beginUpdate();

            try {
                Object[] cells = graph.getChildCells(parent, true, false);
                graph.removeCells(cells, true);
                drawTree(root, 1, 1);

            } finally {
                graph.getModel().endUpdate();
            }
        }

        public TreeDisplay(BSTNode<Flower> root, int width, int height) {
            this.update(root);

            CANVAS_WIDTH = width;
 
            mxGraphComponent graphComponent = new mxGraphComponent(graph);
 
            graph.setCellsMovable(false);
            graph.setCellsResizable(false);
            graph.setCellsEditable(false);
            graph.setCellsDisconnectable(false);
            graph.setCellsBendable(false);
            graph.setConnectableEdges(false);
            graph.setAllowDanglingEdges(false);
            graph.setDropEnabled(false);

            graphComponent.setPageVisible(true);
            graphComponent.setPreferPageSize(true);
            graphComponent.setCenterPage(true); 
            graphComponent.setPageScale(1.5);
 
            int pageW = Math.max(700, width / 2);
            int pageH = Math.max(600, height / 2);
            PageFormat pf = graphComponent.getPageFormat();
            if (pf == null) {
                pf = new PageFormat();
            }
            Paper paper = pf.getPaper();
            if (paper == null) {
                paper = new Paper();
            }
            paper.setSize(pageW, pageH);
            paper.setImageableArea(0, 0, pageW, pageH);
            pf.setPaper(paper);
            graphComponent.setPageFormat(pf); 
 
            this.setLayout(new BorderLayout());
            this.add(graphComponent, BorderLayout.CENTER);
        }
    } 

}

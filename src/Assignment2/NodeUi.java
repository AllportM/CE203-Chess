package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

class NodeUi extends JFrame {
    Board chessBoard;
    JButton startGame;
    ButtonHandler buttonH;
    private MenuBar menu;

    public NodeUi(Node node) {
        buttonH = node.b.getUi().buttonH;
        menu = node.b.getUi().getMenu();
        setPreferredSize(new Dimension(800, 800));
        initChess(node.b);
        setJMenuBar(menu);
    }


    void initChess(Board b) {
        getContentPane().removeAll();
        setJMenuBar(menu);
        b.revertMove();
        chessBoard = b;
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(8, 8));
        for (int i = 0; i < b.board.length; i++) {
            for (Tile tile : chessBoard.board[i]) {
                board.add(tile);
            }
        }
        add(board);
        setSize(new Dimension(800, 800));
        setVisible(true);
        pack();
        repaint();
    }
}
//    Board chessBoard;
//    JButton startGame;
//    ButtonHandler buttonH;
//    LinkedList<Node> nodeL = new LinkedList<>();
//    JPanel menu = new JPanel();
//    JPanel view = new JPanel();
//
//    public NodeUi(Node n)
//    {
//        setPreferredSize(new Dimension(800, 830));
//        int i = 1;
//        while (i > 0)
//        {
//            if (n.parent == null) i = 0;
//            nodeL.add(n);
//            n = n.parent;
//        }
//        JButton next = new JButton("Next");
//        JButton prev = new JButton("Prev");
//        next.addActionListener(new NodeBH(this));
//        prev.addActionListener(new NodeBH(this));
//        menu.add(next);
//        menu.add(prev);
//        menu.setPreferredSize(new Dimension(800, 20));
//        add(menu, BorderLayout.SOUTH);
//        view.setLayout(new GridLayout(8, 8));
//        view.setPreferredSize(new Dimension(800, 800));
//        add(view, BorderLayout.CENTER);
//        initNode(nodeL.get(0));
//        setVisible(true);
//        pack();
//        repaint();
//        for (Node node: nodeL)
//        {
//            System.out.println(node.b);
//        }
//    }
//
//    void initNode(Node n)
//    {
//        view.removeAll();
//        for (int i = 0; i < n.b.board.length; i++)
//        {
//            for (Tile tile: n.b.board[i])
//            {
//                view.add(new Tile(tile));
//            }
//        }
//        repaint();
//    }
//}
//
//class NodeBH implements ActionListener
//{
//
//    int i = 0;
//    NodeUi ui;
//
//    public NodeBH(NodeUi ui){ this.ui = ui; }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        switch (((JButton)e.getSource()).getText())
//        {
//            case "Next:":
//                if (i != ui.nodeL.size()-1) i += 1;
//                ui.initNode(ui.nodeL.get(i));
//                break;
//            case "Prev":
//                if (i != 0) i -= 1;
//                ui.initNode(ui.nodeL.get(i));
//                break;
//
//        }
//    }
//}
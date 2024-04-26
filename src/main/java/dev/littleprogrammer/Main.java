package dev.littleprogrammer;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public Main() { initUI(); }

    private void initUI() {
        JFrame frame = new JFrame();
        Game game = new Game(); // Game instance

        frame.add(game); // Adds game to frame

        frame.setResizable(false); // No resizing my window
        frame.pack(); // Pack after adding components makes preferred size

        // Set icon
        List<Image> icons = new ArrayList<>();
        icons.add(loadIcon("/snakeIcon16.png"));
        icons.add(loadIcon("/snakeIcon24.png"));
        icons.add(loadIcon("/snakeIcon32.png"));
        icons.add(loadIcon("/snakeIcon48.png"));
        icons.add(loadIcon("/snakeIcon256.png"));
        frame.setIconImages(icons);

        // Title, center window, exit kill all threads, and visible
        frame.setTitle("Snake");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Requests focus so Game can receive key events
        game.requestFocusInWindow();
    }

    // This does cool things... I think
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> { new Main(); }); // Lambda :O
    }

    private Image loadIcon(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}

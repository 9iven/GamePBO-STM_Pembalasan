package main;

import gameengine.Game;
import javax.swing.JFrame;

// Entry point program
public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame("Disciplinary - Proyek Akhir PBO");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        Game gamePanel = new Game();

        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);

        window.setVisible(true);

        // Memulai game loop
        gamePanel.startGameLoop();
    }
}
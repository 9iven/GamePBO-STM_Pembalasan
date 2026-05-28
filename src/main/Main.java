package main;

import gameengine.Game;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame("STM Pembalasan - Proyek Akhir PBO");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Mengizinkan window di-resize agar centering dapat bekerja dinamis
        window.setResizable(true);

        Game gamePanel = new Game();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameLoop();
    }
}
package pck;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaCalculadora tela = new TelaCalculadora();
            tela.setVisible(true);
        });
    }
}

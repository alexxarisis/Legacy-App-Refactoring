package org.alexxarisis;

import org.alexxarisis.view.OpeningWindow;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new OpeningWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
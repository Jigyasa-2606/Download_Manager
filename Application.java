package me.groot.downloadmanager;

import me.groot.downloadmanager.database.Database;
import me.groot.downloadmanager.database.DatabaseSettings;
import me.groot.downloadmanager.gui.FrontPage;
import me.groot.downloadmanager.gui.Screen;

import javax.swing.*;
import java.io.File;

public class Application {

    public static void main(String[] args) {
        Database db = new Database(new DatabaseSettings(new File("database/database").getAbsolutePath(),"SA",""));
        db.migrate();
        db.create();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            Screen obj = new FrontPage(db);
            obj.initialize();
            obj.setVisible(true);
        });
    }
}

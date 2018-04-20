import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import Graphics.*;

public class Application {

    public static void main(String[] args) {
        JFrame f  = new JFrame("3DRenderer");
        f.setSize(900, 900);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        String shapeDataFilename = "face-shape.txt";
        String textureDataFilename = "face-texture.txt";
        ModelReader mr = new ModelReader();
        Model face = mr.read(shapeDataFilename, textureDataFilename);

//        View view = new View(face);
//
//        Container pane = f.getContentPane();
//        pane.add(view, BorderLayout.CENTER);
//
//        f.setVisible(true);
    }

}

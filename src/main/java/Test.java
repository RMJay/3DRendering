import Graphics.ModelReader;

import java.awt.*;
import java.io.FileReader;
import java.io.StreamTokenizer;

public class Test {


    public static void main(String[] args) {
        String shapeDataFilename = "new_shape.txt";
        String textureDataFilename = "new_tex.txt";

        ModelReader reader = new ModelReader();
        reader.read(shapeDataFilename, textureDataFilename);
    }

}

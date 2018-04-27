//import java.awt.*;
//import java.io.*;
//import java.util.*;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class TestReader {
//
//    private FileReader frShape;
//    private FileReader frTexture;
//    private StreamTokenizer inShape;
//    private StreamTokenizer inTexture;
//
//    private HashMap<Integer, Color> perVertexRGB = new HashMap<Integer, Color>();
//
//    public void read(String shapeDataFilename, String textureDataFilename) {
//
//
//        try {
////            List<String> shapes = new BufferedReader(new FileReader(shapeDataFilename)).lines().collect(Collectors.toList());
////            List<String> textures = new BufferedReader(new FileReader(textureDataFilename)).lines().collect(Collectors.toList());
////
////            int shapesPos = 0, texturesPos = 0;
////            while (shapesPos < shapes.size() && texturesPos < textures.size()) {
////
//////                System.err.println(textures.get(texturesPos));
//////                System.err.println(textures.get(texturesPos+1));
//////                System.err.println(textures.get(texturesPos+2));
//////                System.err.println(textures.get(texturesPos+3));
//////
//////                System.err.println(textures.get(texturesPos).split(" ").length);
////
////                List<Integer> ids = Arrays.stream(textures.get(texturesPos++).split(" "))
////                        .filter(str -> !str.isEmpty())
////                        .map(Integer::new)
////                        .collect(Collectors.toList());
////
////                List<Integer> v1 = Arrays.stream(textures.get(texturesPos++).split(" "))
////                        .filter(str -> !str.isEmpty())
////                        .map(Integer::new)
////                        .collect(Collectors.toList());
////                List<Integer> v2 = Arrays.stream(textures.get(texturesPos++).split(" "))
////                        .filter(str -> !str.isEmpty())
////                        .map(Integer::new)
////                        .collect(Collectors.toList());
////                List<Integer> v3 = Arrays.stream(textures.get(texturesPos++).split(" "))
////                        .filter(str -> !str.isEmpty())
////                        .map(Integer::new)
////                        .collect(Collectors.toList());
////
////                perVertexRGB.put(ids.get(0), new Color(v1.get(0), v1.get(1), v1.get(2)));
////                perVertexRGB.put(ids.get(1), new Color(v2.get(0), v2.get(1), v2.get(2)));
////                perVertexRGB.put(ids.get(2), new Color(v3.get(0), v3.get(1), v3.get(2)));
////
////                texturesPos++;
//////            }
////
////            frShape = new FileReader(shapeDataFilename);
////            inShape = new StreamTokenizer(frShape);
////
////            frTexture = new FileReader(textureDataFilename);
////            inTexture = new StreamTokenizer(frTexture);
//
////            int[] vertexIds = new int[3];
////            Color[] rgbValues = new Color[3];
////            for (int k = 0; k < 10; k++) {
//////           while (inTexture.ttype != inTexture.TT_EOF) {
////               for (int i = 0; i < 3; i++) {
////                   inTexture.nextToken();
////                   vertexIds[i] = (int) inTexture.nval;
////               }
////               for (int i = 0; i < 3; i++) {
////                   inTexture.nextToken();
////                   int red = (int) inTexture.nval;
////                   inTexture.nextToken();
////                   int green = (int) inTexture.nval;
////                   inTexture.nextToken();
////                   int blue = (int) inTexture.nval;
////                   rgbValues[i] = new Color(red, green, blue);
////               }
////               for (int i = 0; i < 3; i++) {
////                   perVertexRGB.put(vertexIds[i], rgbValues[i]);
////                   int red = rgbValues[i].getRed();
////                   int green = rgbValues[i].getGreen();
////                   int blue = rgbValues[i].getBlue();
////                   System.out.println(String.format("vertex: %d, r=%d, g=%d, b=%d", vertexIds[i], red, green, blue));
////               }
////               System.out.println();
////           }
//
////           Iterator<Map.Entry<Integer, Color>> iter = perVertexRGB.entrySet().iterator();
////           for (int k = 0; k < 40; k++) {
////               Map.Entry<Integer, Color> e = iter.next();
////               int id = e.getKey();
////               Color color = e.getValue();
////               System.out.println(String.format("vertex: %d, r=%d, g=%d, b=%d", id, color.getRed(), color.getGreen(), color.getBlue()));
////           }
//
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }
//
//}

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class YUVtoPPM {

    private Decoder decoder;

    private RGB[][] outputMatrix;

    public YUVtoPPM(Decoder decoder) {
        this.decoder = decoder;
        //outputMatrix = new RGB[decoder.getY().length][decoder.getY()[0].length];
        getRGB();
    }

    private void getRGB() {
        outputMatrix = new RGB[decoder.getY().length][decoder.getY()[0].length];
        for (int line = 0; line < decoder.getY().length; line++) {
            for (int column = 0; column < decoder.getY()[0].length; column++) {
                double Y = decoder.getY()[line][column];
                double U = decoder.getU()[line][column];
                double V = decoder.getV()[line][column];

                Double R = Y + 1.402 * (V - 128);
                Double G = Y - 0.344136 * (U - 128) - 0.714136 * (V - 128);
                Double B = Y + 1.7790 * (U - 128);

                if (R > 255) R = 255.0;
                if (G > 255) G = 255.0;
                if (B > 255) B = 255.0;
                outputMatrix[line][column] = new RGB(R.intValue(), G.intValue(), B.intValue());
            }
        }
    }

    public void outputPPM(RGB[][] inputMatrix, String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("P3");
        printWriter.println("800 600");
        printWriter.println("255");
        for (int line = 0; line < decoder.getY().length; line++) {
            for (int column = 0; column < decoder.getY()[0].length; column++) {
                printWriter.println(inputMatrix[line][column].getR());
                printWriter.println(inputMatrix[line][column].getG());
                printWriter.println(inputMatrix[line][column].getB());
            }
        }

        printWriter.close();
    }

    public RGB[][] getOutputMatrix() {
        return outputMatrix;
    }
}

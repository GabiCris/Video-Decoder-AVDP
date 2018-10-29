import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PPMReader {
    private String filename;

    private int width;
    private int height;
    private int maxValue;

    private RGB[][] inputMatrix;

    private double[][] Y;
    private double[][] U;
    private double[][] V;

    public PPMReader(String filename) {
        this.filename = filename;
        read();

        for (int line = 0; line < getHeight(); line++) {
            for (int column = 0; column < getWidth(); column++) {
                Y[line][column] = getInputMatrix()[line][column].getY();
                U[line][column] = getInputMatrix()[line][column].getU();
                V[line][column] = getInputMatrix()[line][column].getV();
            }
        }
    }

    public double[][] getY() {
        return Y;
    }

    public double[][] getU() {
        return U;
    }

    public double[][] getV() {
        return V;
    }


    private void read() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String st;
        try {
            String firstLine = br.readLine();
            br.readLine();

            String thirdLine = br.readLine();
            width = Integer.parseInt(thirdLine.split(" ")[0]);
            height = Integer.parseInt(thirdLine.split(" ")[1]);
            maxValue = Integer.parseInt(br.readLine());
            inputMatrix = new RGB[height][width];

            Y = new double[height][width];
            U = new double[height][width];
            V = new double[height][width];

            int line = 0;
            int column = 0;
            while ((st = br.readLine()) != null && line != height) {
                if (column == width) {
                    column = 0;
                    line ++;
                }

                inputMatrix[line][column] = new RGB(Integer.parseInt(st), Integer.parseInt(br.readLine()),
                        Integer.parseInt(br.readLine()));
                column ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public RGB[][] getInputMatrix() {
        return inputMatrix;
    }
}


public class RGB {
    private int R;
    private int G;
    private int B;
    private double Y;
    private double U;
    private double V;

    public RGB(int r, int g, int b) {
        R = r;
        G = g;
        B = b;
        Y = 0.299*R + 0.587*G + 0.114*B;
        U = 128 - 0.168736*R - 0.331264*G + 0.5*B;
        V = 128 + 0.5*R - 0.418688*G - 0.081312*B;
    }

    public double getY() {
        return Y;
    }

    public double getU() {
        return U;
    }

    public double getV() {
        return V;
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    @Override
    public String toString() {
        return "RGB{" +
                "R=" + R +
                ", G=" + G +
                ", B=" + B +
                '}';
    }
}

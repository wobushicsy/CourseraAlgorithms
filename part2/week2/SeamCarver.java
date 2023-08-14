import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private Picture picture;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        checkNull(picture);

        this.picture = new Picture(picture);
        width = this.picture.width();
        height = this.picture.height();
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("You can't call function by passing a null pointer");
        }
    }

    private void checkIndices(int x, int y) {
        if (x >= width || x < 0) {
            throw new IllegalArgumentException("Index x is out of bound");
        }
        if (y >= height || y < 0) {
            throw new IllegalArgumentException("Index y is out of bound");
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    private boolean checkBounder(int x, int y) {
        return x == 0 || x == width - 1 || y == 0 || y == height - 1;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkIndices(x, y);

        if (checkBounder(x, y)) {
            return 1000;
        }

        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        int rx = left.getRed() - right.getRed();
        int gx = left.getGreen() - right.getGreen();
        int bx = left.getBlue() - right.getBlue();
        int deltaXSquared = rx * rx + gx * gx + bx * bx;

        Color up = picture.get(x - 1, y);
        Color down = picture.get(x + 1, y);
        int ry = up.getRed() - down.getRed();
        int gy = up.getGreen() - down.getGreen();
        int by = up.getBlue() - down.getBlue();
        int deltaYSquared = ry * ry + gy * gy + by * by;

        return Math.sqrt(deltaXSquared + deltaYSquared);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkNull(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkNull(seam);
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}

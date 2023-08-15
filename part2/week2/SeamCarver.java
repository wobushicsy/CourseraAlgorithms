import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.awt.Color;

public class SeamCarver {

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        checkNull(picture);

        this.picture = new Picture(picture);
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("You can't call function by passing a null pointer");
        }
    }

    private void checkIndices(int x, int y) {
        if (x >= picture.width() || x < 0) {
            throw new IllegalArgumentException("Index x is out of bound");
        }
        if (y >= picture.height() || y < 0) {
            throw new IllegalArgumentException("Index y is out of bound");
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    private boolean checkBounder(int x, int y) {
        return x == 0 || x == picture.width() - 1 || y == 0 || y == picture.height() - 1;
    }

    private int getSquaredDelta(int x1, int y1, int x2, int y2) {
        Color color1 = picture.get(x1, y1);
        Color color2 = picture.get(x2, y2);
        int rx = color1.getRed() - color2.getRed();
        int gx = color1.getGreen() - color2.getGreen();
        int bx = color1.getBlue() - color2.getBlue();
        return rx * rx + gx * gx + bx * bx;
    }


    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        checkIndices(x, y);

        if (checkBounder(x, y)) {
            return 1000;
        }

        int deltaXSquared = getSquaredDelta(x - 1 , y, x + 1, y);
        int deltaYSquared = getSquaredDelta(x, y - 1, x, y + 1);

        return Math.sqrt(deltaXSquared + deltaYSquared);
    }

    private int getDigraphIndex(int x, int y) {
        int width = width();
        return (x - 1) * width + y + 1;
    }

    private Picture transpose(Picture pic) {
        int width = pic.width();
        int height = pic.height();
        Picture newPic = new Picture(height, width);
        for (int i = 0; i < height; i += 1) {
            for (int j = 0; j < width; j += 1) {
                newPic.set(i, j, pic.get(j, i));
            }
        }

        return newPic;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture pic = transpose(picture);
        SeamCarver sc = new SeamCarver(pic);

        return sc.findVerticalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int height = height();
        int width = width();
        double[][] energy = new double[height][width];
        for (int i = 0; i < height; i += 1) {
            for (int j = 0; j < width; j += 1) {
                energy[i][j] = energy(j, i);
            }
        }

        // create digraph
        int vertices = (height - 2) * width + 2;
        Digraph digraph = new Digraph(vertices);
        for (int i = 0; i < width; i += 1) {
            int index = getDigraphIndex(1, i);
            digraph.addEdge(0, index);
        }
        for (int i = 1; i < height - 2; i += 1) {
            for (int j = 0; j < width; j += 1) {
                int index = getDigraphIndex(i, j);
                digraph.addEdge(index, getDigraphIndex(i + 1, j));
                if (j == 0) {
                    digraph.addEdge(index, getDigraphIndex(i + 1, j + 1));
                } else if (j == width - 1) {
                    digraph.addEdge(index, getDigraphIndex(i + 1, j - 1));
                } else {
                    digraph.addEdge(index, getDigraphIndex(i + 1, j - 1));
                    digraph.addEdge(index, getDigraphIndex(i + 1, j + 1));
                }
            }
        }
        for (int i = 0; i < width; i += 1) {
            int index = getDigraphIndex(height - 2, i);
            digraph.addEdge(index, vertices - 1);
        }

        // get topological order
        Topological tp = new Topological(digraph);

        double[] discTo = new double[vertices];
        for (int i = 0; i < vertices; i += 1) {
            discTo[i] = Double.POSITIVE_INFINITY;
        }
        int[] edgeTo = new int[vertices];
        discTo[0] = 1000;
        edgeTo[0] = 0;

        // find acyclic shortest path
        for (int vertex : tp.order()) {
            double vertexEnergy = discTo[vertex];
            for (int neighbor : digraph.adj(vertex)) {
                int index = neighbor - 1 + width;
                int i = index / width;
                int j = index % width;
                double neighborEnergy = energy[i][j];
                if (vertexEnergy + neighborEnergy < discTo[neighbor]) {
                    discTo[neighbor] = vertexEnergy + neighborEnergy;
                    edgeTo[neighbor] = vertex;
                }
            }
        }

        int[] ans = new int[height];
        for (int pointer = vertices - 1; edgeTo[pointer] != pointer; pointer = edgeTo[pointer]) {
            int index = pointer - 1 + width;
            int i = index / width;
            int j = index % width;
            ans[i] = j;
        }

        ans[0] = ans[1];
        ans[ans.length - 1] = ans[ans.length - 2];

        return ans;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkNull(seam);

        Picture newPic = transpose(picture);
        SeamCarver sc = new SeamCarver(newPic);

        sc.removeVerticalSeam(sc.findVerticalSeam());

        this.picture = transpose(sc.picture());
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkNull(seam);

        int height = height();
        int width = width();
        Picture newPic = new Picture(width - 1, height);
        for (int i = 0; i < height; i += 1) {
            for (int j = 0; j < seam[i]; j += 1) {
                newPic.set(j, i, picture.get(j, i));
            }
            for (int j = seam[i] + 1; j < width; j += 1) {
                newPic.set(j - 1, i, picture.get(j, i));
            }
        }

        this.picture = newPic;
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture("3x4.png");
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Printing energy calculated for each pixel.\n");

        StdOut.println(sc.height());
        StdOut.println(sc.width());

        sc.findHorizontalSeam();

//        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
    }

}

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int[][] picture;
    private double[][] energy;
    private int width;
    private int height;

    private double computeEnergy(int x, int y) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return 1000.0;
        }
        int rgbUp = picture[x][y - 1];
        int rgbDown = picture[x][y + 1];
        int rgbLeft = picture[x - 1][y];
        int rgbRight = picture[x + 1][y];

        double rx = Math.pow((((rgbLeft >> 16) & 0xFF) - ((rgbRight >> 16) & 0xFF)), 2);
        double gx = Math.pow((((rgbLeft >> 8) & 0xFF)- ((rgbRight >> 8) & 0xFF)), 2);
        double bx = Math.pow((((rgbLeft >> 0) & 0xFF)- ((rgbRight >> 0) & 0xFF)), 2);

        double ry = Math.pow((((rgbUp >> 16) & 0xFF) - ((rgbDown >> 16) & 0xFF)), 2);
        double gy = Math.pow((((rgbUp >> 8) & 0xFF) - ((rgbDown >> 8) & 0xFF)), 2);
        double by = Math.pow((((rgbUp >> 0) & 0xFF) - ((rgbDown >> 0) & 0xFF)), 2);

        return Math.sqrt(rx + gx + bx + ry + gy + by);
    }

    private void transpose() {
        int temp = width;
        width = height;
        height = temp;

        int[][] picture2 = new int[width][height];
        double[][] energy2 = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture2[i][j] = picture[j][i];
                energy2[i][j] = energy[j][i];
            }
        }
        picture = picture2;
        energy = energy2;
    }

    private void relaxvertical(double[][] distTo, int[][] edgeTo, int x, int y) {
        if (distTo[x][y + 1] > distTo[x][y] + energy[x][y + 1]) {
            distTo[x][y + 1] = distTo[x][y] + energy[x][y + 1];
            edgeTo[x][y + 1] = x;
        }
        if (x > 0 && distTo[x - 1][y + 1] > distTo[x][y] + energy[x - 1][y + 1]) {
            distTo[x - 1][y + 1] = distTo[x][y] + energy[x - 1][y + 1];
            edgeTo[x - 1][y + 1] = x;
        }
        if (x < width - 1 && distTo[x + 1][y + 1] > distTo[x][y] + energy[x + 1][y + 1]) {
            distTo[x + 1][y + 1] = distTo[x][y] + energy[x + 1][y + 1];
            edgeTo[x + 1][y + 1] = x;
        }
    }

    private void checkSeam(int[] seam) {
        if (height <= 1 || seam == null || seam.length != width) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < width; i++) {
            if (seam[i] < 0 || seam[i] >= height) {
                throw new IllegalArgumentException();
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    public SeamCarver(Picture pic) {
        if (pic == null) throw new IllegalArgumentException();
        width = pic.width();
        height = pic.height();
        picture = new int[width][height];
        energy = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture[i][j] = pic.getRGB(i, j);
            }
        }
        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j++) {
                energy[i][j] = computeEnergy(i, j);
            }
        }
    }

    public Picture picture () {
        Picture pic = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j ++) {
                pic.setRGB(i, j, picture[i][j]);
            }
        }
        return pic;
    }

    public int[] findVerticalSeam() {
        int [] seam = new int[height];
        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (j == 0) distTo[i][j] = energy[i][j];
                else distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (int j = 0; j < height - 1; j++) {
            for (int i = 0; i < width; i++) {
                relaxvertical(distTo, edgeTo, i, j);
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < width; i++) {
            if (distTo[i][height - 1] < min) {
                min = distTo[i][height - 1];
                index = i;
            }
        }
        seam[height - 1] = index;
        for (int j = height - 2; j >= 0; j--) {
            seam[j] = edgeTo[seam[j + 1]][j + 1];
        }
        return seam;
    }

    public int[] findHorizontalSeam() {
        transpose();
        int[] temp = findVerticalSeam();
        transpose();
        return temp;
    }

    public void removeHorizontalSeam(int[] seam) {
        checkSeam(seam);
        int max = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < width; i++) {
            if (seam[i] < min) min = seam[i];
            if (seam[i] > max) max = seam[i];
            for (int j = seam[i]; j < height - 1; j++) {
                picture[i][j] = picture[i][j + 1];
            }
        }
        height--;
        if (min > 0) min--;
        if (max > height - 1) max = height - 1;
        for (int i = 0; i < width; i++) {
            for (int j = min; j <= max; j++) {
                energy[i][j] = computeEnergy(i, j);
            }
            for (int j = max + 1; j < height - 1; j++) {
                energy[i][j] = energy[i][j + 1];
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }

    public int width() {return width;}
    public int height() {return height;}
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) {
            throw new IllegalArgumentException();
        }
        return energy[x][y];
    }
    public static void main(String[] args) {

    }
}

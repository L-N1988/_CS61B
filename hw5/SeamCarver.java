import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    int width;
    int height;
    double[][] energy;
    double[][] reverseEnergy;
    Picture currentPic;

    public SeamCarver(Picture picture) {
        currentPic = picture;
        width = picture.width();
        height = picture.height();
        energy = new double[height][width];
        reverseEnergy = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energy[y][x] = calcEnergy(x, y, picture);
                reverseEnergy[x][y] = energy[y][x];
            }
        }
    }

    private double calcEnergy(int x, int y, Picture picture) {
        double deltaXSR;
        double deltaYSR;
        int upRow = (y - 1 < 0) ? height - 1: y - 1;
        int lowRow = (y + 1 < height) ? y + 1 : 0;
        int rightCol = (x + 1 < width) ? x + 1 : 0;
        int leftCol = (x - 1 < 0) ? width - 1 : x - 1;
        Color up = picture.get(x, upRow);
        Color low = picture.get(x, lowRow);
        Color right = picture.get(rightCol, y);
        Color left = picture.get(leftCol, y);
        deltaXSR = (right.getRed() - left.getRed()) * (right.getRed() - left.getRed())
                + (right.getGreen() - left.getGreen()) * (right.getGreen() - left.getGreen())
                + (right.getBlue() - left.getBlue()) * (right.getBlue() - left.getBlue());
        deltaYSR = (up.getRed() - low.getRed()) * (up.getRed() - low.getRed())
                + (up.getGreen() - low.getGreen()) * (up.getGreen() - low.getGreen())
                + (up.getBlue() - low.getBlue()) * (up.getBlue() - low.getBlue());
        return deltaXSR + deltaYSR;
    }

    // current picture
    public Picture picture() {
        return currentPic;
    }

    // width of current picture
    public int width() {
        width = currentPic.width();
        return width;
    }

    // height of current picture
    public int height() {
        height = currentPic.height();
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return energy(x, y, 0);
    }

    private double energy(int x, int y, int flag) {
        if (flag == 0) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return Double.MAX_VALUE;
            }
            return energy[y][x];
        } else {
            if (x < 0 || x >= height || y < 0 || y >= width) {
                return Double.MAX_VALUE;
            }
            return reverseEnergy[y][x];
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // reverse height and width to generate proper array
        // using flag == 1 to return reversed energy map
        return findSeam(height, width, 1);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(width, height, 0);
    }

    // find vertical seam actually
    private int[] findSeam(int width, int height, int flag) {
        int[] Seam = new int[height];
        int[] prevSeam = new int[height];
        double minCost = Double.MAX_VALUE;
        double cost;
        for (int x = 0; x < width; x++) {
            int y = 0;
            prevSeam[y] = x;
            cost = energy(x, y, flag);
            // explore next row
            y += 1;
            while (y < height) {
                prevSeam[y] = minEnergyV(prevSeam[y - 1], y, flag);
                cost += energy(prevSeam[y], y, flag);
                y += 1;
            }
            if (cost < minCost) {
                minCost = cost;
                System.arraycopy(prevSeam, 0, Seam, 0, height);
            }
        }
        return Seam;
    }

    /**
     * explore y row associate with x col to find minimal cost column
     * @param x, column
     * @param y, searching row
     * @return minimal cost's column
     */
    private int minEnergyV(int x, int y, int flag) {
        int minX;
        if (energy(x, y, flag) > energy(x + 1, y, flag)) {
            minX = x + 1;
        } else {
            minX = x;
        }
        if (energy(minX, y, flag) > energy(x - 1, y ,flag)) {
            return x - 1;
        } else {
            return minX;
        }
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        SeamRemover.removeHorizontalSeam(currentPic, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        SeamRemover.removeHorizontalSeam(currentPic, seam);
    }

}

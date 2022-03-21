import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private int picWidth;
    private int picHeight;
    private double[][] energy;
    private double[][] reverseEnergy;
    private Picture currentPic;

    public SeamCarver(Picture picture) {
        currentPic = new Picture(picture.width(), picture.height());
        picWidth = picture.width();
        picHeight = picture.height();
        energy = new double[picHeight][picWidth];
        reverseEnergy = new double[picWidth][picHeight];
        for (int x = 0; x < picWidth; x++) {
            for (int y = 0; y < picHeight; y++) {
                energy[y][x] = calcEnergy(x, y, picture);
                reverseEnergy[x][y] = energy[y][x];
            }
        }
    }

    private double calcEnergy(int x, int y, Picture picture) {
        double deltaXSR;
        double deltaYSR;
        int upRow = (y - 1 < 0) ? picHeight - 1 : y - 1;
        int lowRow = (y + 1 < picHeight) ? y + 1 : 0;
        int rightCol = (x + 1 < picWidth) ? x + 1 : 0;
        int leftCol = (x - 1 < 0) ? picWidth - 1 : x - 1;
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
        picWidth = currentPic.width();
        return picWidth;
    }

    // height of current picture
    public int height() {
        picHeight = currentPic.height();
        return picHeight;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return energy(x, y, 0);
    }

    private double energy(int x, int y, int flag) {
        if (flag == 0) {
            if (x < 0 || x >= picWidth || y < 0 || y >= picHeight) {
                return Double.MAX_VALUE;
            }
            return energy[y][x];
        } else {
            if (x < 0 || x >= picHeight || y < 0 || y >= picWidth) {
                return Double.MAX_VALUE;
            }
            return reverseEnergy[y][x];
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // reverse height and width to generate proper array
        // using flag == 1 to return reversed energy map
        return findSeam(picHeight, picWidth, 1);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(picWidth, picHeight, 0);
    }

    // find vertical seam actually
    // algorithm works like folding paper from top row by row until the bottom
    private int[] findSeam(int width, int height, int flag) {
        int[] seam = new int[height];
        double[][] accEnergy = new double[height][width];
        int[][] from = new int[height][width];
        // set all value to max to prepare to find minimal
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                accEnergy[j][i] = Double.MAX_VALUE;
            }
        }
        // initialize the first row
        for (int i = 0; i < width; i++) {
            accEnergy[0][i] = energy(i, 0, flag);
            from[0][i] = i;
        }
        int y = 1;
        int minCol;
        while (y < height) {
            for (int i = 0; i < width; i++) {
                // return minimal accumulative energy
                minCol = minAccEnergyV(i, y - 1, accEnergy);
                if (accEnergy[y][i] > energy(i, y, flag) + accEnergy[y - 1][minCol]) {
                    // store from column
                    from[y][i] = minCol;
                    // decrease energy
                    accEnergy[y][i] = energy(i, y, flag) + accEnergy[y - 1][minCol];
                }
            }
            y += 1;
        }
        minCol = 0;
        // find minimal cost accumulating energy in the last row
        for (int i = 0; i < width; i++) {
            if (accEnergy[height - 1][minCol] > accEnergy[height - 1][i]) {
                minCol = i;
            }
        }
        seam[height - 1] = minCol;
        // verbose but beautiful
        for (int i = 0; i < height - 1; i++) {
            int prevCol = from[height - 1 - i][seam[height - 1 - i]];
            seam[height - 2 - i] = prevCol;
        }
        return seam;
    }

    private int minAccEnergyV(int x, int y, double[][] accEnergy) {
        int minX;
        if (accEnergy(x, y, accEnergy) > accEnergy(x + 1, y, accEnergy)) {
            minX = x + 1;
        } else {
            minX = x;
        }
        if (accEnergy(minX, y, accEnergy) > accEnergy(x - 1, y, accEnergy)) {
            return x - 1;
        } else {
            return minX;
        }
    }

    private double accEnergy(int x, int y, double[][] accEnergy) {
        if (x < 0 || x >= accEnergy[0].length || y < 0 || y >= accEnergy.length) {
            return Double.MAX_VALUE;
        }
        return accEnergy[y][x];
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        currentPic = SeamRemover.removeHorizontalSeam(currentPic, seam);
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        currentPic = SeamRemover.removeVerticalSeam(currentPic, seam);
    }

}

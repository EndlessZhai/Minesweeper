package minesweeper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Tim Barber
 */
public class Grid {

    /*
     * 0 - Untouched - no mine
     * 1 - Safe
     * 2 - Mine
     * 3 - Flagged
     * 4 - Detonated Mine
     * 5 - Flagged mine
     */
    private int width;
    private int length;
    private int[][] playArea;
    private int[][] lastPlayArea;
    private static int[][] savedPlayArea;
    private int numberOfMines;

    public Grid() {
        this.width = 10;
        this.length = 10;
        this.playArea = new int[this.length][this.width];
        this.lastPlayArea = this.playArea;
        this.savedPlayArea = new int[this.length][this.width];
        for (int i = 0; i < this.length; i++) {
            Arrays.fill(this.savedPlayArea[i], 0);
        }
        this.numberOfMines = 10;
    }

    public Grid(int width, int length, int numMines) {
        this.width = width;
        this.length = length;
        this.playArea = new int[this.length][this.width];
        this.lastPlayArea = this.playArea;
        this.savedPlayArea = new int[this.length][this.width];
        for (int i = 0; i < this.length; i++) {
            Arrays.fill(this.savedPlayArea[i], 0);
        }
        this.numberOfMines = numMines;
        fillMines();
    }

    public void fillMines() {
        System.out.println("called");
        this.lastPlayArea = this.playArea;
        int timeSeed = Integer.valueOf(LocalDateTime.now().toString().trim().replaceAll("\\D", "").substring(0, 10));
        Random miner = new Random(timeSeed);
        ArrayList<Integer> xIndices = new ArrayList<>();
        ArrayList<Integer> yIndices = new ArrayList<>();
        System.out.println("inited");
        for (int i = 0; i < this.numberOfMines; i++) {
            System.out.println("Loop " + i);
            int tempX = -1;
            int tempY = -1;
            do {
                tempX = miner.nextInt(this.getWidth());
                tempY = miner.nextInt(this.getLength());
            } while ((xIndices.indexOf(tempX) > -1 && xIndices.indexOf(tempX) == yIndices.indexOf(tempY)) || tempX < 0 || tempY < 0);
            xIndices.add(tempX);
            yIndices.add(tempY);
        }
        System.out.println("looped");
        // now we have lists of mine positions
        for (int i = 0; i < this.numberOfMines; i++) {
            this.playArea[yIndices.get(i)][xIndices.get(i)] = 2;
        }
        System.out.println("done");
    }

    public int getWidth() {
        return this.width;
    }

    public int getLength() {
        return this.length;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setNumMines(int amt) {
        this.numberOfMines = amt;
    }

    public int getNumMines() {
        return this.numberOfMines;
    }

    public boolean isUntouched(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 0 || this.playArea[yPos][xPos] == 2;
    }

    public boolean isFlagged(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 3 || this.playArea[yPos][xPos] == 5;
    }

    public boolean isMine(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 2 || this.playArea[yPos][xPos] == 4 || this.playArea[yPos][xPos] == 5;
    }

    public boolean isDetonated(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 4;
    }

    public boolean isInactiveMine(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 2;
    }

    public boolean isClicked(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 1;
    }

    public boolean isSafe(int xPos, int yPos) {
        return !isMine(yPos, xPos);
    }

    public void flag(int xPos, int yPos) {
        this.lastPlayArea = this.playArea;
        if (this.playArea[yPos][xPos] == 0) {// untouched
            this.playArea[yPos][xPos] = 3; // flag
        } else if (this.playArea[yPos][xPos] == 3) { //already flagged
            this.playArea[yPos][xPos] = 0; // untouched
        } else if (this.playArea[yPos][xPos] == 2) { // mine
            this.playArea[yPos][xPos] = 5; // flagged mine
        } else if (this.playArea[yPos][xPos] == 5) { //already flagged mine
            this.playArea[yPos][xPos] = 2; // unflagged mine
        }
    }

    public int[][] getPlayArea() {
        return this.playArea;
    }

    public int[][] getSavedPlayArea() {
        return this.savedPlayArea;
    }

    public void savePlayArea() {
        this.savedPlayArea = this.playArea;
    }

    public void revertToSaved() {
        this.lastPlayArea = this.playArea;
        this.playArea = this.savedPlayArea;
    }

    public void clear() {
        this.lastPlayArea = this.playArea;
        this.playArea = new int[this.length][this.width];
    }

    public void setCells(int xPosition, int yPosition, int[][] cells) {
        this.lastPlayArea = this.playArea;
        int[][] newArea = this.playArea;
        for (int y = yPosition; y < yPosition + cells.length; y++) {
            for (int x = xPosition; x < xPosition + cells[0].length; x++) {
                //System.out.println("x: " + x + ", xPosition: " + xPosition + "\ny: " + y + ", yPosition: " + yPosition);
                newArea[y][x] = cells[y - yPosition][x - xPosition];
            }
        }
        this.playArea = newArea;
    }

    public void setCell(int x, int y, boolean value) {
        this.lastPlayArea = this.playArea;
        if (value) {
            this.playArea[y][x] = 1;
        } else {
            this.playArea[y][x] = 0;
        }
    }

    public int getCell(int x, int y) {
        return this.playArea[y][x];
    }

    public int safeCheck(int xPos, int yPos) {
        try {
            return this.playArea[yPos][xPos];
        } catch (ArrayIndexOutOfBoundsException b) {
            return -1;
        }
    }

    public void click(int x, int y) {
        this.lastPlayArea = this.playArea;
        switch (this.playArea[y][x]) {
            case 0:
                this.playArea[y][x] = 1;
                // begin clicking all the other zeroes .... yay...
                if (safeCheck(x - 1, y) == 0) {
                    System.out.println(safeCheck(x - 1, y));
                    click(x - 1, y);
                }
                if (safeCheck(x + 1, y) == 0) {
                    click(x + 1, y);
                }
                if (safeCheck(x, y - 1) == 0) {
                    click(x, y - 1);
                }
                if (safeCheck(x, y + 1) == 0) {
                    click(x, y + 1);
                }
                break;
            case 1:
                break;
            case 2:
                this.playArea[y][x] = 4;
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    public void setPlayArea(int[][] newPlayArea) {
        this.playArea = newPlayArea;
    }

    public int getNeighbors(int xPos, int yPos) {
        System.out.println("xPos: " + xPos + ", yPos: " + yPos + ", length: " + this.length + ", width: " + this.width);
        int neighbors = 0;
        //check neighbors (with bounds checks)
        if (xPos > 0) {
            if (yPos > 0) {
                if (isMine(xPos - 1, yPos - 1)) { // if upper left
                    neighbors++;
                }
            }
        }

        if (yPos > 0) {
            if (isMine(xPos, yPos - 1)) { // if upper
                neighbors++;
            }
        }

        if (xPos < (this.length - 1)) {
            if (yPos > 0 && isMine(xPos + 1, yPos - 1)) { // if upper right
                neighbors++;
            }
        }

        if (xPos > 0 && yPos < (this.length - 1)) {
            if (isMine(xPos - 1, yPos + 1)) { // if lower left
                neighbors++;
            }
        }

        if (yPos < (this.length - 1)) {
            if (isMine(xPos, yPos + 1)) { // if lower
                neighbors++;
            }
        }

        if (yPos < (this.length - 1) && xPos < (this.width - 1)) {
            if (isMine(xPos + 1, yPos + 1)) { // if lower right
                neighbors++;
            }
        }

        if (xPos > 0) {
            if (isMine(xPos - 1, yPos)) {// if left
                neighbors++;
            }
        }

        if (xPos < (this.length - 1)) {
            if (isMine(xPos + 1, yPos)) { // if right
                neighbors++;
            }
        }
        return neighbors;

    }

    public boolean isZero(int xPos, int yPos) {
        return this.playArea[yPos][xPos] == 0;
    }

    @Override
    public String toString() {
        String output = "";
        for (int y = 0; y < this.width; y++) {
            for (int x = 0; x < this.length; x++) {
                output += String.valueOf(this.playArea[y][x]);
                output += " ";
            }
            output += "\n";
        }
        return output;
    }
}
/*
 * The MIT License
 *
 * Copyright (c) 2018 Tim Barber.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"),
 * to deal in the Software without restriction, including without limitation
 * the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or
 * sell copies of the Software, and to permit persons to whom the Software
 * is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER
 * DEALINGS IN THE SOFTWARE.
 */

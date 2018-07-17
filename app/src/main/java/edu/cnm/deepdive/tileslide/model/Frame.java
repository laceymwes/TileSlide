package edu.cnm.deepdive.tileslide.model;

import static android.content.ContentValues.TAG;

import android.util.Log;
import java.util.Arrays;
import java.util.Random;

public class Frame {

  private int size;
  private Random rng;
  private Tile[][] start;
  private Tile[][] tiles;
  private Tile[][] win;
  private int moves;

  public Frame(int size, Random rng) {
    this.size = size;
    this.rng = rng;
    start = new Tile[size][size];
    tiles = new Tile[size][size];
    win = new Tile[size][size];
    for (int i = 0; i < size * size - 1; i++) {
      tiles[i / size][i % size] = new Tile(i);
    }
    tiles[size - 1][size - 1] = null;
    for (int i = 0; i < tiles.length; i++){
      System.arraycopy(tiles[i], 0, win[i], 0, tiles[i].length);
    }
    scramble();
  }

  public Frame(int size, Integer[] tileNums, Integer[] startNums, Random rng) {
    this.size = size;
    this.rng = rng;
    this.start = new Tile[size][size];
    this.tiles = new Tile[size][size];
    setTiles(tileNums);
    setStart(startNums);
  }

  public boolean hasWon() {
    return (Arrays.deepEquals(tiles, win));
  }

  public void reset() {
    copy(start, tiles);
    moves = 0;
  }

  public void scramble() {
    shuffle();
    if (!isParityEven()) {
      swapRandomPair();
    }
    copy(tiles, start);
    moves = 0;
  }

  public Tile[][] getStart() {
    return start;
  }

  public Tile[][] getTiles() {
    return tiles;
  }

  private void setStart(Integer[] startNums) {
    for (int i = 0; i < startNums.length; i++) {
      start[i / size][i % size] =
          (startNums[i] == null) ?
              null : new Tile(startNums[i]);
    }
  }

  private void setTiles(Integer[] tileNums) {
    for (int i = 0; i < tileNums.length; i++) {
      tiles[i / size][i % size] =
        (tileNums[i] == null) ?
         null : new Tile(tileNums[i]);
    }
  }

  public int getMoves() {
    return moves;
  }

  // Checks all surrounding tiles of current location in GridView
  public boolean move(int row, int col) {
    return move(row, col, row - 1, col)
        || move(row, col, row, col + 1)
        || move(row, col, row + 1, col)
        || move(row, col, row, col - 1);
  }

  private boolean move(int fromRow, int fromCol, int toRow, int toCol) {
    if (
        tiles[fromRow][fromCol] != null
        && toRow >= 0
        && toRow < size
        && toCol >= 0
        && toCol < size
        && tiles[toRow][toCol] == null // if location being considered is empty
    ) {
      swap(tiles, fromRow, fromCol, toRow, toCol);
      ++moves;
      return true;
    }
    return false;
  }

  private void copy(Tile[][] source, Tile[][] dest) {
    for (int row = 0; row < size; row++) {
      System.arraycopy(source[row], 0, dest[row], 0, size);
    }
  }

  private int distanceHome(int row, int col) {
    Tile tile = tiles[row][col];
    int homeRow;
    int homeCol;
    if (tile != null) {
      homeRow = tile.getNumber() / size;
      homeCol = tile.getNumber() % size;
    } else {
      homeRow = size - 1;
      homeCol = size - 1;
    }
    return Math.abs(row - homeRow) + Math.abs(col - homeCol);
  }

  private void shuffle() {
    for (int toPosition = size * size - 1; toPosition >= 0; toPosition--) {
      int toRow = toPosition / size;
      int toCol = toPosition % size;
      int fromPosition = rng.nextInt(toPosition + 1);
      if (fromPosition != toPosition) {
        int fromRow = fromPosition / size;
        int fromCol = fromPosition % size;
        swap(tiles, fromRow, fromCol, toRow, toCol);
      }
    }
  }

  private boolean isParityEven() {
    int sum = 0;
    Tile[][] work = new Tile[size][size];
    copy(tiles, work);
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (tiles[row][col] == null) {
          sum += distanceHome(row, col);
          break;
        }
      }
    }
    for (int fromRow = 0; fromRow < size; fromRow++) {
      for (int fromCol = 0; fromCol < size; fromCol++) {
        int fromPosition = fromRow * size + fromCol;
        int toPosition = (work[fromRow][fromCol] != null)
            ? work[fromRow][fromCol].getNumber() : size * size - 1;
        while (toPosition != fromPosition) {
          int toRow = toPosition / size;
          int toCol = toPosition % size;
          swap(work, fromRow, fromCol, toRow, toCol);
          sum++;
          toPosition = (work[fromRow][fromCol] != null)
              ? work[fromRow][fromCol].getNumber() : size * size - 1;
        }
      }
    }
    return (sum & 1) == 0;
  }

  private void swapRandomPair() {
    int fromPosition = rng.nextInt(size * size);
    while (tiles[fromPosition / size][fromPosition % size] == null) {
      fromPosition = rng.nextInt(size * size);
    }
    int fromRow = fromPosition / size;
    int fromCol = fromPosition % size;
    int toPosition = rng.nextInt(size * size);
    while (toPosition == fromPosition
        || tiles[toPosition / size][toPosition % size] == null) {
      toPosition = rng.nextInt(size * size);
    }
    int toRow = toPosition / size;
    int toCol = toPosition % size;
    swap(tiles, fromRow, fromCol, toRow, toCol);
  }

  private void swap(Tile[][] tiles, int fromRow, int fromCol, int toRow, int toCol) {
    Tile temp = tiles[toRow][toCol];
    tiles[toRow][toCol] = tiles[fromRow][fromCol];
    tiles[fromRow][fromCol] = temp;
  }

}

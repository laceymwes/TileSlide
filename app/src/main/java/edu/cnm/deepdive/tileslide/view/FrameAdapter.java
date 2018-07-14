package edu.cnm.deepdive.tileslide.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.model.Tile;

public class FrameAdapter extends ArrayAdapter<Tile> {

  private int size;
  private Bitmap[] tileImages; // tiles from image
  private Bitmap noTileImage; // empty space
  private Frame frame;
  private Tile[] tiles;
  private int imageID;

  public FrameAdapter(@NonNull Context context, @NonNull Frame frame, @NonNull int imageID) {
    super(context, R.layout.tile_item);
    this.frame = frame;
    size = frame.getTiles().length;
    tiles = new Tile[size * size];
    this.imageID = imageID;
    copyModelTiles();
    addAll(tiles);
    sliceBitmap();
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.tile_item, null, false);
    }
    Tile tile = getItem(position);
    TileView tileView = convertView.findViewById(R.id.tile_image);
    if (tile != null) {
      tileView.setImageBitmap(tileImages[tile.getNumber()]);
    } else {
      tileView.setImageBitmap(noTileImage);
    }
    return convertView;
  }

  @Override
  public void notifyDataSetChanged() {
    copyModelTiles();
    setNotifyOnChange(false);
    // TODO Possible modify this if we want to add animation.
    clear();
    addAll(tiles);
    super.notifyDataSetChanged();
  }

  private void copyModelTiles() {
    Tile[][] source = frame.getTiles();
    for (int row = 0; row < size; row++) {
      System.arraycopy(source[row], 0, tiles, row * size, size);
    }
  }

  public int getImageID() {
    return imageID;
  }

  private void sliceBitmap() {
    Drawable drawable = ContextCompat.getDrawable(getContext(), imageID);
    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
    tileImages = new Bitmap[size * size];
    int imageWidth = bitmap.getWidth();
    int imageHeight = bitmap.getHeight();
    for (int i = 0; i < tileImages.length; i++) {
      int row = i / size;
      int col = i % size;
      tileImages[i] = Bitmap.createBitmap(bitmap, col * imageWidth / size, row * imageHeight / size,
          imageWidth / size, imageHeight / size);
    }
    noTileImage = Bitmap.createBitmap(tileImages[tileImages.length - 1]);
    noTileImage.eraseColor(ContextCompat.getColor(getContext(), R.color.puzzleBackground));
  }
}

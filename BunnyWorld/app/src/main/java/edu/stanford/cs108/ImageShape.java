package edu.stanford.cs108;

public class ImageShape extends Shape {
    String imageName;

    public ImageShape(String imageName,
                      String shapeName,
                      boolean isHidden,
                      boolean isMovable,
                      boolean isInventory,
                      String shapeScript,
                      float x, float y,
                      float width, float height) {
        super(shapeName, isHidden, isMovable, isInventory, shapeScript, x, y, width, height);
        this.imageName = imageName;
    }
}

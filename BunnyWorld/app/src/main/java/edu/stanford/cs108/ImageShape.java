package edu.stanford.cs108;

public class ImageShape extends Shape {
    String imageName;

    public ImageShape(String imageName,
                      String shapeName,
                      boolean isHidden,
                      boolean isMovable,
                      boolean isInventory,
                      String shapeScript) {
        super(shapeName, isHidden, isMovable, isInventory, shapeScript);
        this.imageName = imageName;
    }
}

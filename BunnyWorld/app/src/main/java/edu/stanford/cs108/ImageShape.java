package edu.stanford.cs108;

public class ImageShape extends Shape {
    String imageName;

    public ImageShape(String imageName,
                      String shapeName,
                      String page,
                      boolean isHidden,
                      boolean isMovable,
                      String shapeScript) {
        super(shapeName, page, isHidden, isMovable, shapeScript);
        this.imageName = imageName;
    }
}

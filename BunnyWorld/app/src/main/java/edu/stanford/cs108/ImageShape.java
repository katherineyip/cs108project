package edu.stanford.cs108;

public class ImageShape extends Shape {
    String imageName;

    public ImageShape(String imageName,
                      String shapeName,
                      //Page page,
                      boolean isHidden,
                      boolean isMovable,
                      String shapeScript) {
        super(shapeName, isHidden, isMovable, shapeScript);
        this.imageName = imageName;
    }
}

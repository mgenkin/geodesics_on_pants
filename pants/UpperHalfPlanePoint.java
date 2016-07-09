public class UpperHalfPlanePoint extends ComplexNumber{
  public UpperHalfPlanePoint(double realPart,double imagPart){
    if (realPart >= 0.0){
      this.super(realPart, imagPart);
    } else{
      throw IllegalArgumentException("Real part of upper half-plane point cannot be negative");
    }
  }
}
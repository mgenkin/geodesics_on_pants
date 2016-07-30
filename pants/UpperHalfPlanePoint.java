public class UpperHalfPlanePoint extends ComplexNumber{
  public UpperHalfPlanePoint(double realPart,double imagPart){
    super(realPart, imagPart);
    // if (imagPart < 0.0 - 10*Math.ulp(0.0)){
    //   throw new IllegalArgumentException("Imaginary part of upper half-plane point cannot be negative, you wanted "+realPart+", "+imagPart);
    // }
  }
  public UpperHalfPlanePoint(ComplexNumber cn){
    this(cn.realPart, cn.imagPart);
  }
}
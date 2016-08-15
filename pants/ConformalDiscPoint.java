public class ConformalDiscPoint extends ComplexNumber{
  public ConformalDiscPoint(double realPart, double imagPart){
    super(realPart, imagPart);
    double sqNorm = Math.pow(realPart, 2)+Math.pow(imagPart, 2);
    // if (sqNorm > 1.0+10*Math.ulp(1.0)){
    //   throw new IllegalArgumentException("Norm of conformal disc point cannot be more than 1.0, you wanted "+realPart+", "+imagPart+" with sqnorm "+sqNorm);
    // }
  }
  public ConformalDiscPoint(ComplexNumber cn){
    this(cn.realPart, cn.imagPart);
  }
}
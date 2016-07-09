public class ConformalDiscPoint extends ComplexNumber{
  public ConformalDiscPoint(double realPart,double imagPart){
    if (Math.pow(realPart, 2)+Math.pow(imagPart, 2) <=1){
      this.super(realPart, imagPart);
    } else{
      throw IllegalArgumentException("Norm of conformal disc point cannot be more than 1.0");
    }
  }
}
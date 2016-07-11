public class LinearFractionalIsometry{
  public ComplexNumber a;
  public ComplexNumber b;
  public ComplexNumber c;
  public ComplexNumber d;
  public LinearFractionalIsometry(ComplexNumber a, ComplexNumber b, ComplexNumber c, ComplexNumber d){
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  public ComplexNumber apply(ComplexNumber z){
    // z -> (az+b)/(cz+d)
    ComplexNumber numerator = z.times(a).plus(b);
    ComplexNumber denominator = z.times(c).plus(d);
    return numerator.times(denominator.multInverse());
  }
}
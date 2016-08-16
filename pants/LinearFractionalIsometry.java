public class LinearFractionalIsometry{
  public ComplexNumber a;
  public ComplexNumber b;
  public ComplexNumber c;
  public ComplexNumber d;
  public boolean conjugate;

  public LinearFractionalIsometry(ComplexNumber a, ComplexNumber b, ComplexNumber c, ComplexNumber d, boolean conjugate){
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.conjugate = conjugate;
  }
  public LinearFractionalIsometry(ComplexNumber a, ComplexNumber b, ComplexNumber c, ComplexNumber d){
    this(a, b, c, d, false);
  }
  public LinearFractionalIsometry(double a, double b, double c, double d){
    this(new ComplexNumber(a, 0.0), new ComplexNumber(b, 0.0), new ComplexNumber(c, 0.0), new ComplexNumber(d, 0.0), false);
  }
  public LinearFractionalIsometry compose(LinearFractionalIsometry other){
    // multiply matrices
    // does this transformation AFTER other transformation
    if(this.conjugate){
      ComplexNumber a = this.a.times(other.a.conj()).plus(this.b.times(other.b.conj()));
      ComplexNumber b = this.a.times(other.b.conj()).plus(this.b.times(other.d.conj()));
      ComplexNumber c = this.c.times(other.a.conj()).plus(this.d.times(other.c.conj()));
      ComplexNumber d = this.c.times(other.b.conj()).plus(this.d.times(other.d.conj()));
      return new LinearFractionalIsometry(a, b, c, d, true);
    } else{
      ComplexNumber a = this.a.times(other.a).plus(this.b.times(other.b));
      ComplexNumber b = this.a.times(other.b).plus(this.b.times(other.d));
      ComplexNumber c = this.c.times(other.a).plus(this.d.times(other.c));
      ComplexNumber d = this.c.times(other.b).plus(this.d.times(other.d));
      return new LinearFractionalIsometry(a, b, c, d);
    }
  }
  public ComplexNumber apply(ComplexNumber z){
    if(this.conjugate){
      // z_conj -> (a*z_conj+b)/(c*z_conj+d)
      ComplexNumber numerator = z.conj().times(a).plus(b);
      ComplexNumber denominator = z.conj().times(c).plus(d);
      return numerator.times(denominator.multInverse());

    } else{
      // z -> (az+b)/(cz+d)
      ComplexNumber numerator = z.times(a).plus(b);
      ComplexNumber denominator = z.times(c).plus(d);
      return numerator.times(denominator.multInverse());
    }
  }
  public LinearFractionalIsometry inverse(){
    //from https://en.wikipedia.org/wiki/M%C3%B6bius_transformation
    return new LinearFractionalIsometry(this.d, this.b.times(-1.0), this.c.times(-1.0), this.a);
  }
}
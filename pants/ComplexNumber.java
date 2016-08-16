public class ComplexNumber{
  public double realPart;
  public double imagPart;
  public ComplexNumber(double realPart,double imagPart){
    this.realPart = realPart;
    this.imagPart = imagPart;
  }
  public ComplexNumber(double realPart){
    this.realPart = realPart;
    this.imagPart = 0.0;
  }
  public String toString(){
    return ""+this.realPart+"+"+this.imagPart+"i";
  }
  public ComplexNumber plus(ComplexNumber other){
    //complex addition
    double realPart = this.realPart+other.realPart;
    double imagPart = this.imagPart+other.imagPart;
    return new ComplexNumber(realPart, imagPart);
  }
  public ComplexNumber plus(double other){
    return this.plus(new ComplexNumber(other, 0.0));
  }
  public ComplexNumber times(ComplexNumber other){
    //complex multiplication
    double realPart = (this.realPart * other.realPart) - (this.imagPart * other.imagPart);
    double imagPart = (this.realPart * other.imagPart) + (this.imagPart * other.realPart);
    return new ComplexNumber(realPart, imagPart);
  }
  public ComplexNumber times(double other){
    return new ComplexNumber(this.realPart*other, this.imagPart*other);
  }
  public ComplexNumber multInverse(){
    double squareNorm = this.squareNorm();
    double realPart = this.realPart / squareNorm;
    double imagPart = - this.imagPart / squareNorm;
    return new ComplexNumber(realPart, imagPart);
  }  
  public ComplexNumber conj(){
    return new ComplexNumber(this.realPart, -this.imagPart);
  }
  public double squareNorm(){
    return Math.pow(this.realPart, 2)+Math.pow(this.imagPart, 2);
  }
  public double norm(){
    return Math.sqrt(this.squareNorm());
  }
  public double squareDist(ComplexNumber other){
    return Math.pow(this.realPart - other.realPart, 2)+Math.pow(this.imagPart - other.imagPart, 2);
  }
  public double[] wedgeProduct(ComplexNumber pt){
    // finding coefficients (ax+by+c0) of the plane through the lines defined by this point and the other point
    //  in the linear model of R3
    double x1 = this.realPart;
    double y1 = this.imagPart;
    double x2 = pt.realPart;
    double y2 = pt.imagPart;
    double[] abcArray = new double[3];
    abcArray[0] = y1 - y2;
    abcArray[1] = x2 - x1;
    abcArray[2] = x1*y2 - x2*y1;
    return abcArray;
  }
  public ComplexNumber sqrt(){
    // returns principal square root
    if (this.imagPart == 0.0 && this.realPart < 0.0){
      return new ComplexNumber(0.0, Math.sqrt(-this.realPart));
    } else {
      // sqrt(z) = sqrt(norm(z))*(z+norm(z)/norm(z+norm(z)))
      double r = this.norm();
      return (this.plus(r).times(1.0/this.plus(r).norm()).times(Math.sqrt(r)));
    }
  }
}
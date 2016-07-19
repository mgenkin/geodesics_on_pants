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
  public ComplexNumber times(ComplexNumber other){
    //complex multiplication
    double realPart = (this.realPart * other.realPart) - (this.imagPart * other.imagPart);
    double imagPart = (this.realPart * other.imagPart) + (this.imagPart * other.realPart);
    return new ComplexNumber(realPart, imagPart);
  }
  public ComplexNumber multInverse(){
    double squareNorm = this.squareNorm();
    double realPart = this.realPart / squareNorm;
    double imagPart = - this.imagPart / squareNorm;
    return new ComplexNumber(realPart, imagPart);
  }
  public double squareNorm(){
    return Math.pow(this.realPart, 2)+Math.pow(this.imagPart, 2);
  }
  public double squareDist(ComplexNumber other){
    return Math.pow(this.realPart - other.realPart, 2)+Math.pow(this.imagPart - other.imagPart, 2);
  }
}
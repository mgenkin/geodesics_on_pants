public class HPoint{
  public UpperHalfPlanePoint halfPlanePt;
  public ConformalDiscPoint confDiscPt;
  public ProjectiveDiscPoint projDiscPt;

  public HPoint(UpperHalfPlanePoint halfPlanePt){
    this.halfPlanePt = halfPlanePt;
    this.confDiscPt = toConformalDisc(halfPlanePt);
    this.projDiscPt = toProjectiveDisc(halfPlanePt);
  }
  public HPoint(ConformalDiscPoint confDiscPt){
    this.confDiscPt = confDiscPt;
    this.halfPlanePt = toHalfPlane(confDiscPt);
    this.projDiscPt = toProjectiveDisc(confDiscPt);
  }
  public HPoint(ProjectiveDiscPoint projDiscPt){
    this.projDiscPt = projDiscPt;
    this.halfPlanePt = toHalfPlane(projDiscPt);
    this.confDiscPt = toConformalDisc(projDiscPt);
  }
  public HPoint(double realPart, double imagPart){
    // passing two doubles assumes it's an upper half plane point
    this(new UpperHalfPlanePoint(realPart, imagPart));
  }
  public HPoint(ComplexNumber cn){
    // assumes it's in the upper half plane
    this(cn.realPart, cn.imagPart);
  }
  public ConformalDiscPoint toConformalDisc(UpperHalfPlanePoint halfPlanePt){
    //z -> (z-i)/(iz-1)
    ComplexNumber a = new ComplexNumber(1.0, 0.0);
    ComplexNumber b = new ComplexNumber(0.0, -1.0);
    ComplexNumber c = new ComplexNumber(0.0, 1.0);
    ComplexNumber d = new ComplexNumber(-1.0, 0.0);
    LinearFractionalIsometry toCD = new LinearFractionalIsometry(a, b, c, d);
    return new ConformalDiscPoint(toCD.apply(halfPlanePt));
  }
  public ConformalDiscPoint toConformalDisc(ProjectiveDiscPoint projDiscPt){
    //x -> x/(1+sqrt(1-x^2-y^2)), y -> y/(1+sqrt(1-x^2-y^2))
    double x = projDiscPt.realPart / (1 + Math.sqrt(1-projDiscPt.squareNorm()));
    double y = projDiscPt.imagPart / (1 + Math.sqrt(1-projDiscPt.squareNorm()));
    return new ConformalDiscPoint(x, y);
  }
  public UpperHalfPlanePoint toHalfPlane(ConformalDiscPoint confDiscPt){
    //z -> (-z+i)/(-iz+1)  
    ComplexNumber a = new ComplexNumber(-1.0, 0.0);
    ComplexNumber b = new ComplexNumber(0.0, 1.0);
    ComplexNumber c = new ComplexNumber(0.0, -1.0);
    ComplexNumber d = new ComplexNumber(1.0, 0.0);
    LinearFractionalIsometry toHP = new LinearFractionalIsometry(a, b, c, d);
    return new UpperHalfPlanePoint(toHP.apply(confDiscPt));
  }
  public UpperHalfPlanePoint toHalfPlane(ProjectiveDiscPoint projDiscPt){
    //to conformal disc, then half plane from there
    ConformalDiscPoint pt = toConformalDisc(projDiscPt);
    return toHalfPlane(pt);
  }
  public ProjectiveDiscPoint toProjectiveDisc(ConformalDiscPoint confDiscPt){
    //x -> 2*x/(1+x^2+y^2), y -> 2*y/(1+x^2+y^2)
    double x = 2*confDiscPt.realPart / (1 + confDiscPt.squareNorm());
    double y = 2*confDiscPt.imagPart / (1 + confDiscPt.squareNorm());
    return new ProjectiveDiscPoint(x, y);
  }
  public ProjectiveDiscPoint toProjectiveDisc(UpperHalfPlanePoint halfPlanePt){
    //to conformal disc, then projective disc from there
    ConformalDiscPoint pt = toConformalDisc(halfPlanePt);
    return toProjectiveDisc(pt);
  }
} 
public class HPoint{
  public UpperHalfPlanePoint halfPlanePt;
  public ConformalDiscPoint confDiscPt;
      //J: We will also want a ProjectiveDiscPoint
      //J: It will offer certain advantages which we will need. For example, when drawing our geodesic, we will have to intersect
      //J: it with sides of various translated hexagons. If the geodesics in the model are represented by circular arcs, 
      //J: we will have to intersect arcs of circles, which means solving a system of quadratic equations.
      //J: In the projective model, intersecting geodesics is the same as intersecting lines, which is easy.
      //J: It is easy to go between the two disc models, at the level of points, using a radius scaling (see wikipedia page).
  public HPoint(UpperHalfPlanePoint halfPlanePt){
    this.halfPlanePt = halfPlanePt;
    this.confDiscPt = toConformalDisc(halfPlanePt);
  }
  public HPoint(ConformalDiscPoint confDiscPt){
    this.confDiscPt = confDiscPt;
    this.halfPlanePt = toHalfPlane(confDiscPt);
  }
  public HPoint(double realPart, double imagPart){
    // passing two doubles assumes it's an upper half plane point
    this(new UpperHalfPlanePoint(realPart, imagPart));
  }
  public HPoint(ComplexNumber cn){
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
  public UpperHalfPlanePoint toHalfPlane(ConformalDiscPoint confDiscPt){
    //z -> (-z+i)/(-iz+1)  
    ComplexNumber a = new ComplexNumber(-1.0, 0.0);
    ComplexNumber b = new ComplexNumber(0.0, 1.0);
    ComplexNumber c = new ComplexNumber(0.0, -1.0);
    ComplexNumber d = new ComplexNumber(1.0, 0.0);
    LinearFractionalIsometry toHP = new LinearFractionalIsometry(a, b, c, d);
    return new UpperHalfPlanePoint(toHP.apply(confDiscPt));
  }
} 
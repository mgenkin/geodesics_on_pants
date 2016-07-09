public class HLine{
  public HPoint idealPt1;
  public HPoint idealPt2;
  public double[] halfPlaneCenterRadius = new double[2]; // center and radius of the circle whose arc is the geodesic in the upper half plane
  public double[] confDiscCenterRadius = new double[4];  // center(x,y) and radius of the circle whose arc is the geodesic in the conformal disc, and arc angle
  public HLine(HPoint hpt1, HPoint hpt2){
    UpperHalfPlanePoint pt1 = hpt1.halfPlanePt;
    UpperHalfPlanePoint pt2 = hpt2.halfPlanePt;
    // find the two ideal points (on the x-axis) whose geodesic passes through pt1 and pt2
    // first, I will find the center of the circle whose arc is that geodesic
    double center = (pt2.squareNorm() - pt1.squareNorm()) / 2*(pt2.realPart - pt1.realPart);
    // now I will find the radius of that circle
    double distance = Math.sqrt(Math.pow((pt1.realPart - center), 2) + Math.pow(pt1.imagPart, 2));
    this.halfPlaneCenterRadius[0] = center;
    this.halfPlaneCenterRadius[1] = distance;
    // the ideal points are the ones where the circle hits the x-axis (on its diameter)
    this.idealPt1 = new HPoint(center+distance, 0.0);
    this.idealPt2 = new HPoint(center-distance, 0.0);
    // to draw this in the 
    this.
  }
  public double[] drawHP(){
    
  }
  public double[] drawCD(){
    
  }
}
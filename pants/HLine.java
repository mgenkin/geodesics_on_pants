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
        //J: I don't understand the above formula. I thought you have to intersect the perpendicular bisector of segment pt1/pt2 with the x-axis?
    // now I will find the radius of that circle
    double distance = Math.sqrt(Math.pow((pt1.realPart - center), 2) + Math.pow(pt1.imagPart, 2));
    this.halfPlaneCenterRadius[0] = center;
    this.halfPlaneCenterRadius[1] = distance;
    // the ideal points are the ones where the circle hits the x-axis (on its diameter)
    this.idealPt1 = new HPoint(center+distance, 0.0);
    this.idealPt2 = new HPoint(center-distance, 0.0);
    // to draw this in the 
    this.confDiscCenterRadius = confDiscArray(this.idealPt1, this.idealPt2);
  }
  public double[] confDiscArray(HPoint idealPt1, HPoint idealPt2){
    // separated this into its own method because it's a lot of computation
    // returns information needed for drawing the line in the conformal disc
    double[] outArray = new double [4];
    double u1 = idealPt1.confDiscPt.realPart;
    double u2 = idealPt1.confDiscPt.imagPart; 
    double v1 = idealPt2.confDiscPt.realPart;
    double v2 = idealPt2.confDiscPt.imagPart;
    // derived from formula found here: https://en.wikipedia.org/wiki/Poincar%C3%A9_disk_model
    double a = (u2*(v1*v1+v2*v2)-v2*(u1*u1+u2*u2)+u2-v2)/(u1*v2-u2*v1);
    double b = (v1*(u1*u1+u2*u2)-u1*(v1*v1+v2*v2)+v1-u2)/(u1*v2-u2*v1);
        //J: I think the formulas for a and b are off by a sign. In completing the square, we should get (x - center_x)^2 + ... etc.
    outArray[0] = a; // center x
    outArray[1] = b; // center y
    outArray[2] = Math.sqrt(1+a*a+b*b); // circle radius
        //J: Isn't the circle radius the distance from (a,b) to idealPt1 and idealPt2? It is true that it should only depend on the norm of (a,b), but what is the derivation?
        //J: I'm skeptical of this formula because as (a,b) approaches to having norm 1, the circle radius should go to 0
    outArray[3] = 2*Math.asin(Math.sqrt(Math.pow(u1-v1, 2)+Math.pow(u2-v2, 2))/2*outArray[2]); // arc angle
        //J: This arc angle isn't quite enough to draw the thing... I think we need both the starting absolute angle and ending absolute angle
    return outArray;
  }
}

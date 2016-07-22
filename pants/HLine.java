public class HLine{
  public HPoint idealPt1;
  public HPoint idealPt2;
  public ComplexNumber projPolarPt;
  public double[] halfPlaneArray = new double[2]; // half plane drawing info, this computation is done right in the constructor
  public double[] confDiscArray = new double[5];  // conformal disc drawing info
  public double[] projDiscArray = new double[4];  // projective disc drawing info
  public HLine(HPoint hpt1, HPoint hpt2){
    UpperHalfPlanePoint pt1 = hpt1.halfPlanePt;
    UpperHalfPlanePoint pt2 = hpt2.halfPlanePt;
    // find the two ideal points (on the x-axis) whose geodesic passes through pt1 and pt2
    // first, I will find the center of the circle whose arc is that geodesic
    double diffX = pt2.realPart-pt1.realPart;
    double diffY = pt2.imagPart-pt1.imagPart;
    double center = (diffY/(-diffX))*(diffY/2 - pt2.imagPart) + pt1.realPart + diffX/2;
    // now I will find the radius of that circle
    double distance = Math.sqrt(Math.pow((pt1.realPart - center), 2) + Math.pow(pt1.imagPart, 2));
    this.halfPlaneArray[0] = center;
    this.halfPlaneArray[1] = distance;
    // the ideal points are the ones where the circle hits the x-axis (on its diameter)
    this.idealPt1 = new HPoint(center+distance, 0.0);
    this.idealPt2 = new HPoint(center-distance, 0.0);
    // calculate drawing info
    this.confDiscArray = confDiscArray(this.idealPt1, this.idealPt2);
    this.projDiscArray = projDiscArray(this.idealPt1, this.idealPt2);
    // polar point in projective model is at the Euclidean center of the conformal model's geodesic
    this.projPolarPt = new ComplexNumber(this.confDiscArray[0], this.confDiscArray[1]);
  }
  public double[] projDiscArray(HPoint idealPt1, HPoint idealPt2){
    // information needed to draw the line in the projective disc
    // just returns the two ideal points (x1,y1,x2,y2), endpoints of the line
    double[] outArray = new double[4];
    outArray[0] = idealPt1.projDiscPt.realPart;
    outArray[1] = idealPt1.projDiscPt.imagPart;
    outArray[2] = idealPt2.projDiscPt.realPart;
    outArray[3] = idealPt2.projDiscPt.imagPart;
    return outArray;
  }

  public double[] confDiscArray(HPoint idealPt1, HPoint idealPt2){
    // information needed to draw the line in the conformal disc
    // returns circle center (x,y), radius, and absolute angle (starting and ending)
    double[] outArray = new double [4];
    double u1 = idealPt1.confDiscPt.realPart;
    double u2 = idealPt1.confDiscPt.imagPart; 
    double v1 = idealPt2.confDiscPt.realPart;
    double v2 = idealPt2.confDiscPt.imagPart;
    // derived from formula found here: https://en.wikipedia.org/wiki/Poincar%C3%A9_disk_model
    double a = (u2-v2)/(u1*v2-u2*v1);
    double b = -(u1-v1)/(u1*v2-u2*v1);
    outArray[0] = -a; // center xb
    outArray[1] = -b; // center y
    outArray[2] = Math.sqrt(a*a+b*b-1); // circle radius
    // outArray[3] = 0; // arc angle, not using this yet, just drawing the whole circle
    return outArray;
  }
  public HLine perpendicularThrough(HPoint point){
    // this is easiest in the projective model
    // Take a line from the polar point to this line, then find the ideal points where it intersects the unit circle.
    // I did a bunch of nasty equations, let's see how badly I messed them up
    
    double x1 = point.projDiscPt.realPart;
    double y1 = point.projDiscPt.imagPart;
    double x2 = this.projPolarPt.realPart;
    double y2 = this.projPolarPt.imagPart;

    // Use the equations (y-y1) =  m(x-x1) and x^2+y^2=1 and solve the resulting quadratic
    double m = (y2 - y1) / (x2 - x1);
    // Coefficients of the quadratic in x
    double a = 1+m*m;
    double b = 2*(y1*m-m*m*x1);
    double c = m*m*x1*x1+y1*y1-2*y1*m*x1-1;
    // Two solutions using the quadratic formula
    double pt1_x = (-b + Math.sqrt(b*b - 4*a*c))/(2*a);
    double pt1_y = 1 - pt1_x*pt1_x;
    double pt2_x = (-b - Math.sqrt(b*b - 4*a*c))/(2*a);
    double pt2_y = 1 - pt2_x*pt2_x;

    // double pt1_x = (m*m*x1 - m*y1 + Math.sqrt(-x1*x1*m*m-2*x1*y1*m*m*m+2*x1*y1*m*m+2*x1*y1-y1*y1+m*m+1))/(1+m*m);
    // double pt1_y = 1 - pt1_x*pt1_x;
    // double pt2_x = (m*m*x1 - m*y1 - Math.sqrt(-x1*x1*m*m-2*x1*y1*m*m*m+2*x1*y1*m*m+2*x1*y1-y1*y1+m*m+1))/(1+m*m);
    // double pt2_y = 1 - pt2_x*pt2_x;
    ProjectiveDiscPoint pt1 = new ProjectiveDiscPoint(pt1_x, pt1_y);
    ProjectiveDiscPoint pt2 = new ProjectiveDiscPoint(pt2_x, pt2_y);
    return new HLine(new HPoint(pt1), new HPoint(pt2));
  }
  public HPoint markDistance(HPoint onLinePt, double distance){
    // using conformal disc, transform point to center, then mark out ln(distance) along the line, then transform back.
    // TODO implement this
    return new HPoint(0.0,0.0);
  }
}
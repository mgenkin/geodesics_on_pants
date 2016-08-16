public class HLine{
  public HPoint idealPt1;
  public HPoint idealPt2;
  public ProjectiveDiscPoint projPolarPt;
  public double[] halfPlaneArray = new double[2]; // half plane drawing info, this computation is done right in the constructor
  public double[] confDiscArray = new double[5];  // conformal disc drawing info
  public double[] projDiscArray = new double[4];  // projective disc drawing info
  public HIsometry reflectionAcross;

  public HLine(ProjectiveDiscPoint pt1, ProjectiveDiscPoint pt2){
    // find the two ideal points of the line through pt1, pt2
    double[] abcArray = ((ComplexNumber)pt1).wedgeProduct((ComplexNumber)pt2);
    double a = abcArray[0];
    double b = abcArray[1];
    double c = abcArray[2];
    double norm = Math.sqrt(a*a+b*b-c*c);
    double scale = a*a+b*b;
    double x1 = (-a*c - b*norm)/scale;
    double y1 = (-b*c + a*norm)/scale;
    double x2 = (-a*c + b*norm)/scale;
    double y2 = (-b*c - a*norm)/scale;
    this.idealPt1 = new HPoint(new ProjectiveDiscPoint(x1,y1));
    this.idealPt2 = new HPoint(new ProjectiveDiscPoint(x2,y2));
    this.confDiscArray = confDiscArray(this.idealPt1, this.idealPt2);
    this.halfPlaneArray = halfPlaneArray(this.idealPt1, this.idealPt2);
    this.projDiscArray = projDiscArray(this.idealPt1, this.idealPt2);
    this.projPolarPt = new ProjectiveDiscPoint(this.confDiscArray[0], this.confDiscArray[1]);
    this.reflectionAcross = this.reflectionAcross();
  }

  public HLine(ProjectiveDiscPoint pt1, HPoint pt2){
    this(pt1, pt2.projDiscPt);
  }
  public HLine(HPoint pt1, ProjectiveDiscPoint pt2){
    this(pt1.projDiscPt, pt2);
  }
  public HLine(HPoint pt1, HPoint pt2){
    this(pt1.projDiscPt, pt2.projDiscPt);
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
    outArray[0] = -a; // center x
    outArray[1] = -b; // center y
    outArray[2] = Math.sqrt(a*a+b*b-1); // circle radius
    // outArray[3] = 0; // arc angle, not using this yet, just drawing the whole circle
    return outArray;
  }

  public double[] halfPlaneArray(HPoint idealPt1, HPoint idealPt2){
    double[] outArray = new double[2];
    UpperHalfPlanePoint pt1 = idealPt1.halfPlanePt;
    UpperHalfPlanePoint pt2 = idealPt2.halfPlanePt;
    // find the two ideal points (on the x-axis) whose geodesic passes through pt1 and pt2
    // first, I will find the center of the circle whose arc is that geodesic
    double diffX = pt2.realPart-pt1.realPart;
    double diffY = pt2.imagPart-pt1.imagPart;
    double center = (diffY/(-diffX))*(diffY/2 - pt2.imagPart) + pt1.realPart + diffX/2;
    // now I will find the radius of that circle
    double distance = Math.sqrt(Math.pow((pt1.realPart - center), 2) + Math.pow(pt1.imagPart, 2));
    outArray[0] = center;
    outArray[1] = distance;
    return outArray;
  }
  public HLine perpendicularThrough(HPoint point){
    // this is easiest in the projective model
    // Take a line from the polar point to this line
    // But first we need to figure out which way to point it
    // Need to see if the polar point is to the right or left of the vector from idealPt1 to idealPt2
    // First, subtract idealPt1 to get get the vectors starting from zero
    double pt1ToPt2X = this.idealPt2.projDiscPt.realPart - this.idealPt1.projDiscPt.realPart;
    double pt1ToPt2Y = this.idealPt2.projDiscPt.imagPart - this.idealPt1.projDiscPt.imagPart;
    double pt1ToPolarX = this.projPolarPt.realPart - this.idealPt1.projDiscPt.realPart;
    double pt1ToPolarY = this.projPolarPt.imagPart - this.idealPt1.projDiscPt.imagPart;
    // now check if the dot product of pt1ToPolar rotated by 90 degrees with pt1ToPt2 is > or < 0
    double rotDotProduct = pt1ToPt2X*(-pt1ToPolarY)+pt1ToPt2Y*(pt1ToPolarX);
    // our rule will be to always turn left
    if (rotDotProduct > 0){
      return new HLine(point, this.projPolarPt);
    } else {
      return new HLine(this.projPolarPt, point);
    }
  }
  public HPoint markDistance(HPoint onLinePt, double distance){
    // x coordinates of ideal points
    double x1 = this.idealPt1.halfPlanePt.realPart;
    double x2 = this.idealPt2.halfPlanePt.realPart;
    // write an isometry mapping the ideal points of the line to 0 and infinity in the half plane model
    double d = 1.0;
    double b = -(x1*x2)/(x1+x2);
    double a = -b/x1;
    double c = -d/x2;
    LinearFractionalIsometry isom = new LinearFractionalIsometry(a, b, c, d);
    LinearFractionalIsometry isomInverse = isom.inverse();
    // apply the isometry
    ComplexNumber centeredPoint = isom.apply((ComplexNumber)onLinePt.halfPlanePt);
    double y = centeredPoint.imagPart;
    // move y*ln(distance) either up or down depending on the boolean parameter counterclockwise
    double newY;
    newY = y*Math.exp(distance);
    // apply the inverse
    ComplexNumber newPoint = isomInverse.apply(new ComplexNumber(0.0, newY));
    return new HPoint(new UpperHalfPlanePoint(newPoint));
  }
  public HLineSegment segment(HPoint endpoint1, HPoint endpoint2){
    return new HLineSegment(endpoint1, endpoint2);
  }
  public HIsometry reflectionAcross(){
    // Use inversion through a circle in the conformal disc
    ComplexNumber center = new ComplexNumber(this.confDiscArray[0], this.confDiscArray[1]);
    double radius = this.confDiscArray[2];
    LinearFractionalIsometry isom = new LinearFractionalIsometry(
      center, 
      new ComplexNumber(radius*radius-center.squareNorm(), 0.0), 
      new ComplexNumber(1.0, 0.0), 
      center.times(-1.0).conj(), 
      true); 
    return new HIsometry(isom);
  }
}
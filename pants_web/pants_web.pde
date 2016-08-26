
int convert(double x){
  // convert to drawing scale (pixels in window rather than -1 to 1)
  return round((float)x * (width/2));
}

ComplexNumber unconvert(float x, float y){
  // convert coordinates to back to math scale (-1 to 1 rather than pixels in window)
  return new ComplexNumber(((double)x)/(width/2), ((double)y)/(width/2));
}

void drawLnUHP(HLine ln){
  // TODO: implement line if you get Infinity
  double[] arr = ln.halfPlaneArray; 
  arc(convert(arr[0]), 0, convert(arr[1])*2, convert(arr[1])*2, 0, PI);
}

void drawSgUHP(HLineSegment sg){
  double[] arr = sg.halfPlaneArray; 
  // arc(convert(arr[0]), 0, convert(arr[1])*2, convert(arr[1])*2, arr[2], arr[3]);
}

void drawPtUHP(HPoint pt){
  ellipse( convert(pt.halfPlanePt.realPart), convert(pt.halfPlanePt.imagPart), 2, 2);
}

void drawLnCD(HLine ln){
  double[] arr = ln.confDiscArray;
  ellipse(convert(arr[0]), convert(arr[1]), convert(arr[2])*2, convert(arr[2])*2);
}

void drawSgCD(HLineSegment sg){
  double[] arr = sg.confDiscArray;
  arc(convert(arr[0]), convert(arr[1]), convert(arr[2])*2, convert(arr[2])*2, (float)arr[3], (float)arr[4]);
}

void drawPtCD(HPoint pt){
  ellipse( convert(pt.confDiscPt.realPart), convert(pt.confDiscPt.imagPart), 2, 2);
}

void drawLnPD(HLine ln){
  double[] arr = ln.projDiscArray; 
  line(convert(arr[0]), convert(arr[1]), convert(arr[2]), convert(arr[3]));
}

void drawSgPD(HLineSegment sg){
  double[] arr = sg.projDiscArray; 
  line(convert(arr[4]), convert(arr[5]), convert(arr[6]), convert(arr[7]));
}

void drawPtPD(HPoint pt){
  ellipse( convert(pt.projDiscPt.realPart), convert(pt.projDiscPt.imagPart), 2, 2);
}

void drawPtPD(ComplexNumber pt){
  ellipse( convert(pt.realPart), convert(pt.imagPart), 2, 2);
}

void setup(){
  size(500,500);
  noFill();
  // frameRate(1);
  // translate(width/2, width/2);
  // rotate(PI);
}

HPoint zeroPt = new HPoint(new ProjectiveDiscPoint(0.2, 0.5));
HPoint pt;
void draw(){
  background(255);
  stroke(0);
  // set up x,y axes oriented properly with 0 in the center
  translate(width/2, width/2);
  scale(1, -1);
  // draw the unit disc (for the conformal or projective model)
  ellipse(0, 0, width, width);
  // get mouse location in math coordinates
  float mouseX_shifted = mouseX-width/2;
  float mouseY_shifted = width/2 - mouseY;
  ComplexNumber mouse_location = unconvert(mouseX_shifted, mouseY_shifted);
  
  if (mouse_location.squareNorm() > 1.0) {
    pt = new HPoint(new ProjectiveDiscPoint(0.1, 0.5));
  } else {
    ProjectiveDiscPoint point = new ProjectiveDiscPoint(mouse_location);
    pt = new HPoint(point);
  }
  HLine ln = new HLine(zeroPt, pt);

  HRightHexagon hexagon = new HRightHexagon(1.0, 2.0, 2.0, ln, pt);

  stroke(0, 255, 0);
  for(int i = 0; i < 6; i++){
    drawPtPD(hexagon.vertices[i]);
    drawSgPD(hexagon.sides[i]);
  }

  stroke(0, 0, 255);
  for(int i = 0; i < 6; i++){
    drawPtCD(hexagon.vertices[i]);
    drawSgCD(hexagon.sides[i]);
  }
  // arc(50, 50, 50, 50, -(2.0/3)*PI, (2.0/3)*PI);

}class ComplexNumber{
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
  public ComplexNumber times(double other){
    return new ComplexNumber(this.realPart*other, this.imagPart*other);
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
}

class ConformalDiscPoint extends ComplexNumber{
  public ConformalDiscPoint(double realPart, double imagPart){
    super(realPart, imagPart);
    double sqNorm = Math.pow(realPart, 2)+Math.pow(imagPart, 2);
    if (sqNorm > 1.0+10*Math.ulp(1.0)){
      throw new IllegalArgumentException("Norm of conformal disc point cannot be more than 1.0, you wanted "+realPart+", "+imagPart+" with sqnorm "+sqNorm);
    }
  }
  public ConformalDiscPoint(ComplexNumber cn){
    this(cn.realPart, cn.imagPart);
  }
}

class HIsometry{
    LinearFractionalIsometry confDiscIsom;
    public HIsometry(LinearFractionalIsometry confDiscIsom){
        this.confDiscIsom = confDiscIsom;
    }
    // public HIsometry(HPoint in1, HPoint out1, HPoint in2, HPoint out2, HPoint in3, HPoint out3){
    //     this.confDiscIsom = new LinearFractionalIsometry(
    //         (ComplexNumber) in1.confDiscPt, 
    //         (ComplexNumber) out1.confDiscPt, 
    //         (ComplexNumber) in2.confDiscPt, 
    //         (ComplexNumber) out2.confDiscPt, 
    //         (ComplexNumber) in3.confDiscPt, 
    //         (ComplexNumber) out3.confDiscPt);
    // }
    public HPoint apply(HPoint pt){
        return new HPoint(confDiscIsom.apply((ComplexNumber)pt.confDiscPt));
    }
    public HLine apply(HLine ln){
        return new HLine(this.apply(ln.idealPt1), this.apply(ln.idealPt2));
    }
    public HIsometry compose(HIsometry other){
        return new HIsometry(confDiscIsom.compose(other.confDiscIsom));
    }
}

class HLine{
  public HPoint idealPt1;
  public HPoint idealPt2;
  public ProjectiveDiscPoint projPolarPt;
  public double[] halfPlaneArray = new double[2]; // half plane drawing info, this computation is done right in the constructor
  public double[] confDiscArray = new double[5];  // conformal disc drawing info
  public double[] projDiscArray = new double[4];  // projective disc drawing info


    
  //   // the ideal points are the ones where the circle hits the x-axis (on its diameter)
  //   this.idealPt1 = new HPoint(center+distance, 0.0);
  //   this.idealPt2 = new HPoint(center-distance, 0.0);
  //   // calculate drawing info
  //   this.confDiscArray = confDiscArray(this.idealPt1, this.idealPt2);
  //   this.projDiscArray = projDiscArray(this.idealPt1, this.idealPt2);
  //   // polar point in projective model is at the Euclidean center of the conformal model's geodesic
  //   this.projPolarPt = new ProjectiveDiscPoint(this.confDiscArray[0], this.confDiscArray[1]);
  // }

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
  public HIsometry reflectAcross(){
    // THIS DOESNT WORK RIGHT NOW
    // this process is described in https://en.wikipedia.org/wiki/M%C3%B6bius_transformation#Specifying_a_transformation_by_three_points
    ComplexNumber z1 = (ComplexNumber)this.idealPt1.confDiscPt;
    ComplexNumber z2 = (ComplexNumber)this.idealPt2.confDiscPt;
    ComplexNumber z3 = new ComplexNumber(this.confDiscArray[0], this.confDiscArray[1]);
    // first, find the isometry that sends z1 -> 0, z2 -> 1, z3 -> infinity
    LinearFractionalIsometry toZeroOneInf = new LinearFractionalIsometry(
      z2.plus(z3.times(-1.0)),
      z1.times(-1.0).times(z2.plus(z3.times(-1.0))),
      z2.plus(z1.times(-1.0)),
      z3.times(-1.0).times(z2.plus(z1.times(-1.0))));
    // now, find the inverse of the isometry that sends 0 -> z1, 1 -> z2, infinity ->infinity
    // oh no, that's gonna be hard to plug in!
    LinearFractionalIsometry oppositeIsom = new LinearFractionalIsometry(
      z2.plus(z3.times(-1.0)),
      z1.times(-1.0).times(z2.plus(z3.times(-1.0))),
      z2.plus(z1.times(-1.0)),
      z3.times(-1.0).times(z2.plus(z1.times(-1.0)))).inverse();    
    return new HIsometry(new LinearFractionalIsometry(0.0,0.0,0.0,0.0));//just so the compiler doesn't complain
  }
}

class HLineSegment extends HLine{
  public HPoint endpoint1;
  public HPoint endpoint2;  
  public double[] projDiscArray = new double[8];  // projective disc drawing info with endpoints;
  public double[] confDiscArray = new double[5];  // conformal disc drawing info with start and end angle
  public double[] halfPlaneArray = new double[4];  // half plane drawing info with start and end angle

  public HLineSegment(ProjectiveDiscPoint pt1, ProjectiveDiscPoint pt2){
    super(pt1, pt2);
    this.endpoint1 = new HPoint(pt1);
    this.endpoint2 = new HPoint(pt2);
    this.projDiscArray = this.projDiscArray();
    this.confDiscArray = this.confDiscArray();
    this.halfPlaneArray = this.halfPlaneArray();
  }
  public HLineSegment(ProjectiveDiscPoint pt1, HPoint pt2){
    this(pt1, pt2.projDiscPt);
  }
  public HLineSegment(HPoint pt1, ProjectiveDiscPoint pt2){
    this(pt1.projDiscPt, pt2);
  }
  public HLineSegment(HPoint pt1, HPoint pt2){
    this(pt1.projDiscPt, pt2.projDiscPt);
  }
  public double[] projDiscArray(){
    // append start point and end point coordinates to the drawing array
    double[] outArray = new double[8];
    double[] lineInfo = super.projDiscArray;
    outArray[0] = lineInfo[0];
    outArray[1] = lineInfo[1];
    outArray[2] = lineInfo[2];
    outArray[3] = lineInfo[3];
    outArray[4] = this.endpoint1.projDiscPt.realPart;
    outArray[5] = this.endpoint1.projDiscPt.imagPart;
    outArray[6] = this.endpoint2.projDiscPt.realPart;
    outArray[7] = this.endpoint2.projDiscPt.imagPart;
    return outArray;
  }
  public double[] confDiscArray(){
    double[] outArray = new double[5];
    double[] lineInfo = super.confDiscArray;
    outArray[0] = lineInfo[0];
    outArray[1] = lineInfo[1];
    outArray[2] = lineInfo[2];
    // append starting and ending of absolute angle
    // compute angle between rightward vector and endpoint1 vector (emanating from circle center)
    double toEndpoint1X = endpoint1.confDiscPt.realPart - lineInfo[0];
    double toEndpoint1Y = endpoint1.confDiscPt.imagPart - lineInfo[1];
    double toEndpoint2X = endpoint2.confDiscPt.realPart - lineInfo[0];
    double toEndpoint2Y = endpoint2.confDiscPt.imagPart - lineInfo[1];
    double angle1 = Math.atan2(toEndpoint1Y, toEndpoint1X) - Math.atan2(0.0, 1.0);
    double angle2 = Math.atan2(toEndpoint2Y, toEndpoint2X) - Math.atan2(0.0, 1.0);
    if (angle1>angle2){
      double temp = angle1;
      angle1 = angle2;
      angle2 = temp;
    }
    if (angle2-angle1 >= Math.PI){
      angle1 += 2*Math.PI;
      double temp = angle1;
      angle1 = angle2;
      angle2 = temp;
    }
    outArray[3] = angle1;
    outArray[4] = angle2;
    return outArray;
  }
  public double[] halfPlaneArray(){
    // append starting and ending of absolute angle
    return new double[1];
  }
}

class HPoint{
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
    double denominator = 1 + Math.sqrt(1-projDiscPt.squareNorm());
    if (projDiscPt.squareNorm() > 1.0){
      // if x is a projDiscPt is accidentally a little outside the disc, just set square norm to 1
      denominator = 1.0;
    }
    double x = projDiscPt.realPart / denominator;
    double y = projDiscPt.imagPart / denominator;
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
} class HRightHexagon{
  
  // side lengths: A,B,C are glued, A_op, B_op, C_op are boundary components
  public double sideA;
  public double sideB;
  public double sideC;
  public double sideA_op;
  public double sideB_op;
  public double sideC_op;
  
  // line of sideA
  public HLine centerLn;
  // center of sideA
  public HPoint centerPt;

  public HPoint[] vertices;
  public HLineSegment[] sides;
  
  public HRightHexagon(double sideA, double sideB, double sideC, HLine centerLn, HPoint centerPt){
    // cosh(a side) = ( cosh(one of its neighbours) cosh(other neighbor) + cosh(opposite) ) / 
    //             ( sinh(one neighbor) sinh(other neighbor )
    this.sideA = sideA;
    this.sideB = sideB;
    this.sideC = sideC;
    this.sideA_op = (Math.cosh(sideB)*Math.cosh(sideC) + Math.cosh(sideA)) / (Math.sinh(sideB)*Math.sinh(sideC));
    this.sideB_op = (Math.cosh(sideA)*Math.cosh(sideC) + Math.cosh(sideB)) / (Math.sinh(sideA)*Math.sinh(sideC));
    this.sideC_op = (Math.cosh(sideA)*Math.cosh(sideB) + Math.cosh(sideC)) / (Math.sinh(sideA)*Math.sinh(sideB));
    // find the other vertices and store the sides as a sequence of HLines
    HPoint[] vertices = new HPoint[6];
    HLineSegment[] sides = new HLineSegment[6];
    HLine perpLn;
    
    vertices[5] = centerLn.markDistance(centerPt, -sideA/2.0);

    vertices[0] = centerLn.markDistance(centerPt, sideA/2.0);
    sides[0] = new HLineSegment(vertices[5], vertices[0]);
    
    perpLn = sides[0].perpendicularThrough(vertices[0]); // line of the next side
    vertices[1] = perpLn.markDistance(vertices[0], sideC_op);  // next vertex
    sides[1] = new HLineSegment(vertices[0], vertices[1]); // next side

    perpLn = sides[1].perpendicularThrough(vertices[1]);
    vertices[2] = perpLn.markDistance(vertices[1], sideB);
    sides[2] = new HLineSegment(vertices[1], vertices[2]);
    
    perpLn = sides[2].perpendicularThrough(vertices[2]);
    vertices[3] = perpLn.markDistance(vertices[2], sideA_op);
    sides[3] = new HLineSegment(vertices[2], vertices[3]);
    
    perpLn = sides[3].perpendicularThrough(vertices[3]);
    vertices[4] = perpLn.markDistance(vertices[3], sideC);
    sides[4] = new HLineSegment(vertices[3], vertices[4]);

    sides[5] = new HLineSegment(vertices[4], vertices[5]);

    this.vertices = vertices;
    this.sides = sides;
    }
  }class LinearFractionalIsometry{
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
  public LinearFractionalIsometry(double a, double b, double c, double d){
    this.a = new ComplexNumber(a, 0.0);
    this.b = new ComplexNumber(b, 0.0);
    this.c = new ComplexNumber(c, 0.0);
    this.d = new ComplexNumber(d, 0.0);
  }
  public LinearFractionalIsometry compose(LinearFractionalIsometry other){
    // multiply matrices
    ComplexNumber a = this.a.times(other.a).plus(this.b.times(other.b));
    ComplexNumber b = this.a.times(other.b).plus(this.b.times(other.d));
    ComplexNumber c = this.c.times(other.a).plus(this.d.times(other.c));
    ComplexNumber d = this.c.times(other.b).plus(this.d.times(other.d));
    return new LinearFractionalIsometry(a, b, c, d);
  }
  public ComplexNumber apply(ComplexNumber z){
    // z -> (az+b)/(cz+d)
    ComplexNumber numerator = z.times(a).plus(b);
    ComplexNumber denominator = z.times(c).plus(d);
    return numerator.times(denominator.multInverse());
  }
  public LinearFractionalIsometry inverse(){
    //from https://en.wikipedia.org/wiki/M%C3%B6bius_transformation
    return new LinearFractionalIsometry(this.d, this.b.times(-1.0), this.c.times(-1.0), this.a);
  }
}class ProjectiveDiscPoint extends ComplexNumber{
  public ProjectiveDiscPoint(double realPart, double imagPart){
    super(realPart, imagPart);
    double sqNorm = Math.pow(realPart, 2)+Math.pow(imagPart, 2);
    // if (sqNorm > 1.0+10*Math.ulp(1.0)){
    //   throw new IllegalArgumentException("Norm of projective disc point cannot be more than 1.0, you wanted "+realPart+", "+imagPart+" with sqnorm "+sqNorm);
    // }
  }
  public ProjectiveDiscPoint(ComplexNumber cn){
    this(cn.realPart, cn.imagPart);
  } 
}class UpperHalfPlanePoint extends ComplexNumber{
  public UpperHalfPlanePoint(double realPart,double imagPart){
    this.realPart = realPart;
    this.imagPart = imagPart;
    // if (imagPart < 0.0 - 10*Math.ulp(0.0)){
    //   throw new IllegalArgumentException("Imaginary part of upper half-plane point cannot be negative, you wanted "+realPart+", "+imagPart);
    // }
  }
  public UpperHalfPlanePoint(ComplexNumber cn){
    this(cn.realPart, cn.imagPart);
  }
}

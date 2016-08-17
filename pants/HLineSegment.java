public class HLineSegment extends HLine{
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
  public HPoint intersectPt(HLineSegment other){
    HPoint point = super.intersectPt((HLine) other);
    double ptX = point.projDiscPt.realPart;
    double x1 = this.endpoint1.projDiscPt.realPart;
    double x2 = this.endpoint2.projDiscPt.realPart;
    double x3 = other.endpoint1.projDiscPt.realPart;
    double x4 = other.endpoint2.projDiscPt.realPart;
    boolean between = true;
    if(ptX < x1){
      if (ptX<x2){
        between = false;
      } 
    } else {
      if (ptX>x2){
        between = false;
      }
    }
    if(ptX < x3){
      if (ptX<x4){
        between = false;
      } 
    } else {
      if (ptX>x4){
        between = false;
      }
    }
    if (between){
      return point;
    } else{
      return null;
    }
  }
  public HPoint intersectPt(HLine other){
    HPoint point = super.intersectPt(other);
    double ptX = point.projDiscPt.realPart;
    double x1 = this.endpoint1.projDiscPt.realPart;
    double x2 = this.endpoint2.projDiscPt.realPart;
    boolean between = true;
    if(ptX < x1){
      if (ptX<x2){
        between = false;
      } 
    } else {
      if (ptX>x2){
        between = false;
      }
    }
    if (between){
      return point;
    } else{
      return null;
    }
  }
}
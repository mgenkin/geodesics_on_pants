public class HRightHexagon{
  
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
    this.sideA_op = acosh((Math.cosh(sideB)*Math.cosh(sideC) + Math.cosh(sideA)) / (Math.sinh(sideB)*Math.sinh(sideC)));
    this.sideB_op = acosh((Math.cosh(sideA)*Math.cosh(sideC) + Math.cosh(sideB)) / (Math.sinh(sideA)*Math.sinh(sideC)));
    this.sideC_op = acosh((Math.cosh(sideA)*Math.cosh(sideB) + Math.cosh(sideC)) / (Math.sinh(sideA)*Math.sinh(sideB)));
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
    public HRightHexagon(double sideA, double sideB, double sideC, HLineSegment[] sides, HPoint[] vertices){
    this.sideA = sideA;
    this.sideB = sideB;
    this.sideC = sideC;
    this.sideA_op = acosh((Math.cosh(sideB)*Math.cosh(sideC) + Math.cosh(sideA)) / (Math.sinh(sideB)*Math.sinh(sideC)));
    this.sideB_op = acosh((Math.cosh(sideA)*Math.cosh(sideC) + Math.cosh(sideB)) / (Math.sinh(sideA)*Math.sinh(sideC)));
    this.sideC_op = acosh((Math.cosh(sideA)*Math.cosh(sideB) + Math.cosh(sideC)) / (Math.sinh(sideA)*Math.sinh(sideB)));
    // find the other vertices and store the sides as a sequence of HLines
    this.vertices = vertices;
    this.sides = sides;
    }
    private static double acosh(double x) { 
      return Math.log(x + Math.sqrt(x*x - 1.0)); 
    } 
  }
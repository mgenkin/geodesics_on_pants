HPoint p1 = new HPoint(0.01, 0.01);
HPoint p2;
HLine ln1;

int convert(double x){
  // convert to drawing scale (pixels in window rather than -1 to 1)
  return round((float)x * (width/2));
}

ComplexNumber unconvert(float x, float y){
  // convert coordinates to back to math scale (-1 to 1 rather than pixels in window)
  return new ComplexNumber(((double)x)/(width/2), ((double)y)/(width/2));
}

void drawLineUHP(HLine ln){
  // TODO: implement line if you get Infinity
  double[] arr = ln.halfPlaneArray; 
  arc(convert(arr[0]), 0, convert(arr[1])*2, convert(arr[1])*2, 0, PI);
  ellipse( convert(arr[0]), 0, 2, 2);
}

void drawPtUHP(HPoint pt){
  ellipse( convert(pt.halfPlanePt.realPart), convert(pt.halfPlanePt.imagPart), 2, 2);
}

void drawLineCD(HLine ln){
  double[] arr = ln.confDiscArray;
  ellipse(convert(arr[0]), convert(arr[1]), convert(arr[2])*2, convert(arr[2])*2);
}

void drawPtCD(HPoint pt){
  ellipse( convert(pt.confDiscPt.realPart), convert(pt.confDiscPt.imagPart), 2, 2);
}

void drawLinePD(HLine ln){
  double[] arr = ln.projDiscArray; 
  line(convert(arr[0]), convert(arr[1]), convert(arr[2]), convert(arr[3]));
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
  // translate(width/2, width/2);
  // rotate(PI);
}

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
    p2 = new HPoint(1.0, 1.0);
  } else {
    ConformalDiscPoint pt = new ConformalDiscPoint(mouse_location);
    p2 = new HPoint(pt);
  }
  HLine ln = new HLine(p2, p1);
  HLine ln_perp = ln.perpendicularThrough(p2);

  // draw projective disc geodesics in green
  stroke(0, 255, 0);
  drawPtPD(p1);
  drawPtPD(p2);
  drawLinePD(ln);
  drawLinePD(ln_perp);
  drawPtPD(ln.idealPt1);
  drawPtPD(ln.idealPt2);
  drawPtPD(ln.projPolarPt);

  // draw conformal disc geodesics in blue
  stroke(0, 0, 255);
  drawPtCD(p1);
  drawPtCD(p2);
  drawLineCD(ln);
  drawLineCD(ln_perp);
  drawPtCD(ln.idealPt1);
  drawPtCD(ln.idealPt2);

  stroke(255,0,0);
}
HPoint p1 = new HPoint(0.01, 0.01);
HPoint p2;
HLine ln1;

int convert(double x){
  // convert to drawing scale
  return round((float)x * (width/2));
}

ComplexNumber unconvert(float x, float y){
  // convert coordinates to back to math scale
  return new ComplexNumber(((double)x)/(width/2), ((double)y)/(width/2));
}

void drawLineUHP(HLine ln){
  // TODO: implement line if you get Infinity
  double[] hpcr = ln.halfPlaneCenterRadius; 
  arc(convert(hpcr[0]), 0, convert(hpcr[1])*2, convert(hpcr[1])*2, 0, PI);
  ellipse( convert(hpcr[0]), 0, 2, 2);
}

void drawPtUHP(HPoint pt){
  ellipse( convert(pt.halfPlanePt.realPart), convert(pt.halfPlanePt.imagPart), 2, 2);
}

void drawLineCD(HLine ln){
  double[] cdcr = ln.confDiscCenterRadius;
  println(cdcr);   
  ellipse(convert(cdcr[0]), convert(cdcr[1]), convert(cdcr[2])*2, convert(cdcr[2])*2);
}

void drawPtCD(HPoint pt){
  ellipse( convert(pt.confDiscPt.realPart), convert(pt.confDiscPt.imagPart), 2, 2);
}

void setup(){
  size(500,500);
  noFill();
  translate(width/2, width/2);
  rotate(PI);
}

void draw(){
  background(255);
  translate(width/2, width/2);
  scale(1, -1);
  // draw the conformal disc
  ellipse(0, 0, width, width);
  float mouseX_shifted = mouseX-width/2;
  float mouseY_shifted = width/2 - mouseY;
  ComplexNumber mouse_location = unconvert(mouseX_shifted, mouseY_shifted);
  if (mouse_location.squareNorm() > 1.0) {
    p2 = new HPoint(1.0, 1.0);
  } else {
    ConformalDiscPoint cdpt = new ConformalDiscPoint(mouse_location);
    p2 = new HPoint(cdpt);
  }
  drawPtCD(p1);
  drawPtCD(p2);
  HLine ln = new HLine(p2, p1);
  drawLineCD(ln);
  drawPtCD(ln.idealPt1);
  drawPtCD(ln.idealPt2);
}
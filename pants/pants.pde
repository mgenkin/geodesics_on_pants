HPoint p1 = new HPoint(0.5, 0.5);
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
  double[] hpcr = ln.halfPlaneCenterRadius;
  println(hpcr);
  println(convert(hpcr[0]));
  println(convert(hpcr[1]));
  arc(convert(hpcr[0]), 0, convert(hpcr[1])*2, convert(hpcr[1])*2, 0, PI);
  ellipse( convert(hpcr[0]), 0, 2, 2);
}

void drawPt(HPoint pt){
  ellipse( convert(pt.halfPlanePt.realPart), convert(pt.halfPlanePt.imagPart), 2, 2);
}

void setup(){
  size(500,500);
  noFill();
  translate(width/2, width/2);
  rotate(PI);
  // draw the conformal disc
  //ellipse(0, 0, width, width);
  // draw the axes
  line(-width/2, 0, width/2, 0);
  line(0, height/2, 0, height/2);
}

void draw(){
  background(255);
  translate(width/2, width/2);
  scale(1, -1);
  float mouseX_shifted = mouseX-width/2;
  float mouseY_shifted = width/2 - mouseY;
  println(unconvert(mouseX_shifted, mouseY_shifted).squareNorm());
  println(unconvert(mouseX_shifted, mouseY_shifted).toString());
  if (mouseY_shifted < 0.0) {
    p2 = new HPoint(1.0, 1.0);
  } else {
    p2 = new HPoint(unconvert(mouseX_shifted, mouseY_shifted));
  }
  drawPt(p1);
  drawPt(p2);
  drawLineUHP(new HLine(p2, p1));
}
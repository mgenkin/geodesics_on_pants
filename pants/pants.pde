
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

HPoint zeroPt = new HPoint(new ProjectiveDiscPoint(0.0, 0.9));
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

  HRightHexagon hexagon = new HRightHexagon(1.0, 1.0, 1.0, ln, pt);
  HRightHexagon reflectedHexagon = hexagon.sides[0].reflectionAcross.apply(hexagon);

  stroke(0, 255, 0);
  for(int i = 0; i < 6; i++){
    drawPtPD(hexagon.vertices[i]);
    drawSgPD(hexagon.sides[i]);
    drawPtPD(reflectedHexagon.vertices[i]);
    drawSgPD(reflectedHexagon.sides[i]);
  }

  stroke(0, 0, 255);
  for(int i = 0; i < 6; i++){
    drawPtCD(hexagon.vertices[i]);
    drawSgCD(hexagon.sides[i]);
    drawPtCD(reflectedHexagon.vertices[i]);
    drawSgCD(reflectedHexagon.sides[i]);
  }
  println(hexagon.sideA);
  println(hexagon.sideB);
  println(hexagon.sideC);
  println(hexagon.sideA_op);
  println(hexagon.sideB_op);
  println(hexagon.sideC_op);

}
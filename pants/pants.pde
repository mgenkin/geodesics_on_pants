
ComplexNumber mouse_location;
HPoint anchorPt = new HPoint(new ConformalDiscPoint(0.01, 0.9));
HPoint centerPt = new HPoint(new ConformalDiscPoint(0.01, 0.0));
HLine ln = new HLine(anchorPt, centerPt);
HRightHexagon hexagon;// = new HRightHexagon(a, b, c, ln, centerPt);  //Jimmy
HPants pants;// = new HPants(hexagon);
String word_string = "";
CurveWord word;// = new CurveWord("ab", pants);
Slider aSlider;
Slider bSlider;
Slider cSlider;
double minValue = 0.1;
double maxValue = 5.0;

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

void mousePressed(){
  float mouseX_shifted = mouseX-width/2;
  float mouseY_shifted = width/2 - mouseY;
  mouse_location = unconvert(mouseX_shifted, mouseY_shifted);
  if (mouse_location.squareNorm() < 1.05){
    anchorPt = new HPoint(new ConformalDiscPoint(mouse_location));    
  }
}

void mouseDragged(){
  float mouseX_shifted = mouseX-width/2;
  float mouseY_shifted = width/2 - mouseY;
  mouse_location = unconvert(mouseX_shifted, mouseY_shifted);
  if (mouse_location.squareNorm() < 1.05){
    centerPt = new HPoint(new ConformalDiscPoint(mouse_location));    
  }
  if (aSlider.inSlider(mouseX, mouseY)){
    aSlider.updateSlider(mouseX);
  } else if (bSlider.inSlider(mouseX, mouseY)){
    bSlider.updateSlider(mouseX);
  } else if (cSlider.inSlider(mouseX, mouseY)){
    cSlider.updateSlider(mouseX);
  }
}

void setup(){
  size(700, 700);
  aSlider = new Slider(minValue, maxValue, 0, 0, 80, 20);
  bSlider = new Slider(minValue, maxValue, 0, 20, 80, 20);
  cSlider = new Slider(minValue, maxValue, 0, 40, 80, 20);
  noFill();
}

void draw(){
  background(255);
  stroke(0);
  // set up x,y axes oriented properly with 0 in the center
  pushMatrix();
  translate(width/2, width/2);
  scale(1, -1);
  // draw the unit disc (for the conformal or projective model)
  ellipse(0, 0, width, width);

  ln = new HLine(anchorPt, centerPt);
  hexagon = new HRightHexagon(aSlider.value(), bSlider.value(), cSlider.value(), ln, centerPt);  //Jimmy  
  pants = new HPants(hexagon);
  if(word_string.length()>0){
    word = new CurveWord(word_string, pants);
    fill(0);
    scale(1, -1);
    text(word_string, width/2-200, -height/2+50);
    scale(1, -1);
    noFill();
  }

  // stroke(0, 255, 0);
  // for(int i = 0; i < 10; i++){
  //   drawPtPD(pants.verticesToDraw[i]);
  //   drawSgPD(pants.sidesToDraw[i]);
  // }

  stroke(0, 0, 255);
  for(int i = 0; i < 10; i++){
    drawPtCD(pants.verticesToDraw[i]);
  }
  drawSgCD(pants.sidesToDraw[0]);
  drawSgCD(pants.sidesToDraw[5]);
  drawSgCD(pants.sidesToDraw[2]);
  drawSgCD(pants.sidesToDraw[7]);
  drawSgCD(pants.sidesToDraw[4]);
  drawSgCD(pants.sidesToDraw[9]);

  stroke(0, 255, 0); // a
  drawSgCD(pants.sidesToDraw[1]);
  drawSgCD(pants.sidesToDraw[6]);
  stroke(255, 0, 255); // b
  drawSgCD(pants.sidesToDraw[3]);
  drawSgCD(pants.sidesToDraw[8]);
  stroke(0, 255, 255); // c
  drawSgCD(pants.leftHexagon.sides[0]);
  
  if(word_string.length()>0){
    stroke(255,0,0);
    for(int i = 0; i < word.word.length(); i++){
      drawSgCD(word.axisSegments[i]);
    }
  }

  popMatrix(); // draw sliders
  stroke(0, 255, 0); // a
  aSlider.drawSlider();
  stroke(255, 0, 255); // b
  bSlider.drawSlider();
  stroke(0, 255, 255); // c
  cSlider.drawSlider();  
}

void keyTyped()
{
  if(key=='a'){
    if(word_string.length()!= 0 && word_string.charAt(word_string.length()-1) == 'A'){
      word_string = word_string.substring(0, word_string.length()-1);
    } else{
      word_string += 'a';
    }
  }
  if(key=='b'){
    if(word_string.length()!= 0 && word_string.charAt(word_string.length()-1) == 'B'){
      word_string = word_string.substring(0, word_string.length()-1);
    } else{
      word_string += 'b';
    }
  }
  if(key=='A'){
    if(word_string.length()!= 0 && word_string.charAt(word_string.length()-1) == 'a'){
      word_string = word_string.substring(0, word_string.length()-1);
    } else{
      word_string += 'A';
    }
  }
  if(key=='B'){
    if(word_string.length()!= 0 && word_string.charAt(word_string.length()-1) == 'b'){
      word_string = word_string.substring(0, word_string.length()-1);
    } else{
      word_string += 'B';
    }
  }
  if(key==BACKSPACE ){
    if (word_string.length() > 0){
      word_string = word_string.substring(0, word_string.length()-1);
    }
  }

}

class Slider{
  double minValue = 0.0;
  double maxValue = 0.0;
  double proportion = 0.2;
  int x = 0;
  int y = 0;
  int w = 0;
  int h = 0;
  public Slider(double minValue, double maxValue, int x, int y, int w, int h){
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  public void drawSlider(){
    line(x, y+h/2, x+w, y+h/2);
    line((int) (x+(proportion*w)), y, (int) (x+(proportion*w)), y+h);
    fill(0);
    text(""+String.format("%.2f", this.value()), x+w, y+h);
    noFill();
  }
  public void updateSlider(int xPos){
    this.proportion = (double)(xPos-this.x)/this.w;
  }
  public boolean inSlider(int xPos, int yPos){
    if ((x<xPos && xPos< x+w) && (y<yPos && yPos<y+h)){
      return true;
    } else{
      return false;
    }
  }
  public double value(){
    return this.minValue + (this.maxValue-this.minValue)*this.proportion;
  }
}
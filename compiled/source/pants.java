import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class pants extends PApplet {

HPoint p1 = new HPoint(0.5f, 0.5f);
HPoint p2;
HLine ln1;

public int convert(double x){
  // convert to drawing scale
  return round((float)x * (width/2));
}

public ComplexNumber unconvert(float x, float y){
  // convert coordinates to back to math scale
  return new ComplexNumber(((double)x)/(width/2), ((double)y)/(width/2));
}

public void drawLineUHP(HLine ln){
  double[] hpcr = ln.halfPlaneCenterRadius;
  println(hpcr);
  println(convert(hpcr[0]));
  println(convert(hpcr[1]));  
  arc(convert(hpcr[0]), 0, convert(hpcr[1])*2, convert(hpcr[1])*2, 0, PI);
  ellipse( convert(hpcr[0]), 0, 2, 2);
}

public void drawPt(HPoint pt){
  ellipse( convert(pt.halfPlanePt.realPart), convert(pt.halfPlanePt.imagPart), 2, 2);
}

public void setup(){
  
  noFill();
  translate(width/2, width/2);
  rotate(PI);
  // draw the conformal disc
  //ellipse(0, 0, width, width);
  // draw the axes
  line(-width/2, 0, width/2, 0);
  line(0, height/2, 0, height/2);
}

public void draw(){
  background(255);
  translate(width/2, width/2);
  scale(1, -1);
  float mouseX_shifted = mouseX-width/2;
  float mouseY_shifted = width/2 - mouseY;
  println(unconvert(mouseX_shifted, mouseY_shifted).squareNorm());
  println(unconvert(mouseX_shifted, mouseY_shifted).toString());
  if (mouseY_shifted < 0.0f) {
    p2 = new HPoint(1.0f, 1.0f);
  } else {
    p2 = new HPoint(unconvert(mouseX_shifted, mouseY_shifted));
  }
  double center = (p2.halfPlanePt.squareNorm() - p1.halfPlanePt.squareNorm()) / 2*(p2.halfPlanePt.realPart - p1.halfPlanePt.realPart);
  println((p2.halfPlanePt.squareNorm() - p1.halfPlanePt.squareNorm()) / 2*(p2.halfPlanePt.realPart - p1.halfPlanePt.realPart)+"center");
  println(Math.sqrt(Math.pow((p1.halfPlanePt.realPart - center), 2) + Math.pow(p1.halfPlanePt.imagPart, 2))+"rad1");
  println(Math.sqrt(Math.pow((p2.halfPlanePt.realPart - center), 2) + Math.pow(p2.halfPlanePt.imagPart, 2))+"rad2");
  drawPt(p1);
  drawPt(p2);
  drawLineUHP(new HLine(p2, p1));
}
  public void settings() {  size(500,500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "pants" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

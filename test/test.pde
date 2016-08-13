Ball b = new Ball();
void setup(){

}
void draw(){
    background(0);
    ellipse(mouseX, mouseY, b.radius, b.radius);
}

class Ball{
    public int radius;
    public Ball(){
        this.radius = 100;
    }
}
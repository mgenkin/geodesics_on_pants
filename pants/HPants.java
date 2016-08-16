public class HPants{
    public HRightHexagon leftHexagon;
    public HIsometry a_translate;
    public HIsometry b_translate;
    public HLineSegment[] sidesToDraw;
    public HPoint[] verticesToDraw;
    public HPants(HRightHexagon leftHexagon){
        this.leftHexagon = leftHexagon;
        this.a_translate = this.leftHexagon.sides[2].reflectionAcross.compose(this.leftHexagon.sides[0].reflectionAcross);
        this.b_translate = this.leftHexagon.sides[0].reflectionAcross.compose(this.leftHexagon.sides[4].reflectionAcross);
        HRightHexagon rightHexagon = leftHexagon.sides[0].reflectionAcross.apply(leftHexagon);
        this.sidesToDraw = new HLineSegment[] {
            leftHexagon.sides[1],
            leftHexagon.sides[2],
            leftHexagon.sides[3],
            leftHexagon.sides[4],
            leftHexagon.sides[5],
            rightHexagon.sides[1],
            rightHexagon.sides[2],
            rightHexagon.sides[3],
            rightHexagon.sides[4],
            rightHexagon.sides[5]
            };
        this.verticesToDraw = new HPoint[] {
            leftHexagon.vertices[0],
            leftHexagon.vertices[1],
            leftHexagon.vertices[2],
            leftHexagon.vertices[3],
            leftHexagon.vertices[4],
            leftHexagon.vertices[5],
            rightHexagon.vertices[0],
            rightHexagon.vertices[1],
            rightHexagon.vertices[2],
            rightHexagon.vertices[3],
            rightHexagon.vertices[4],
            rightHexagon.vertices[5],
        };
    }
}
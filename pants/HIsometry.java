public class HIsometry{
    LinearFractionalIsometry confDiscIsom;
    public HIsometry(LinearFractionalIsometry confDiscIsom){
        this.confDiscIsom = confDiscIsom;
    }
    // public HIsometry(HPoint in1, HPoint out1, HPoint in2, HPoint out2, HPoint in3, HPoint out3){
    //     this.confDiscIsom = new LinearFractionalIsometry(
    //         (ComplexNumber) in1.confDiscPt, 
    //         (ComplexNumber) out1.confDiscPt, 
    //         (ComplexNumber) in2.confDiscPt, 
    //         (ComplexNumber) out2.confDiscPt, 
    //         (ComplexNumber) in3.confDiscPt, 
    //         (ComplexNumber) out3.confDiscPt);
    // }
    public HPoint apply(HPoint pt){
        return new HPoint(confDiscIsom.apply((ComplexNumber)pt.confDiscPt));
    }
    public HLine apply(HLine ln){
        return new HLine(this.apply(ln.idealPt1), this.apply(ln.idealPt2));
    }    
    public HLineSegment apply(HLineSegment sg){
        return new HLineSegment(this.apply(sg.endpoint1), this.apply(sg.endpoint2));
    }
    public HRightHexagon apply(HRightHexagon hx){
        HLineSegment[] newSides = new HLineSegment[6];
        HPoint[] newVertices = new HPoint[6];
        for(int i = 0; i < 6; i++){
            newSides[i] = this.apply(hx.sides[i]);
            newVertices[i] = this.apply(hx.vertices[i]);
        }
        return new HRightHexagon(hx.sideA, hx.sideB, hx.sideC, newSides, newVertices);
    }
    public HIsometry compose(HIsometry other){
        return new HIsometry(confDiscIsom.compose(other.confDiscIsom));
    }
}
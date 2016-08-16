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
        return new HPoint(new ConformalDiscPoint(confDiscIsom.apply((ComplexNumber)pt.confDiscPt)));
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
    public HPants apply(HPants pants){
        return new HPants(this.apply(pants.leftHexagon));
    }
    public HIsometry compose(HIsometry other){
        return new HIsometry(confDiscIsom.compose(other.confDiscIsom));
    }
    public HIsometry inverse(){
        return new HIsometry(confDiscIsom.inverse());
    }
    public HLine axis(){
        if(this.confDiscIsom.conjugate){
            return null;
        } else {
            // z = ( a-d +-  sqrt[ (d-a)^2 + 4bc ] ) / 2c
            ComplexNumber a = this.confDiscIsom.a;
            ComplexNumber b = this.confDiscIsom.b;
            ComplexNumber c = this.confDiscIsom.c;
            ComplexNumber d = this.confDiscIsom.d;
            ComplexNumber discriminant = d.plus(a.times(-1.0)).times(d.plus(a.times(-1.0))).plus(b.times(c.times(4.0)));
            ComplexNumber pt1 = (a.plus(d.times(-1.0).plus(discriminant.sqrt())).times(c.times(2.0).multInverse()));
            ComplexNumber pt2 = (a.plus(d.times(-1.0).plus(discriminant.sqrt().times(-1.0))).times(c.times(2.0).multInverse()));
            // translate point to see if it's in the disc
            // if it's in the disc, that point is the source.  Otherwise, switch the order
            // TODO: this is totally cheating and won't always work
            ComplexNumber testPt = this.confDiscIsom.apply(((ComplexNumber) pt1).times(0.99));
            if (testPt.norm() < pt1.norm()){
                return new HLine(new HPoint(new ConformalDiscPoint(pt1)), new HPoint(new ConformalDiscPoint(pt2)));
            } else {
                return new HLine(new HPoint(new ConformalDiscPoint(pt2)), new HPoint(new ConformalDiscPoint(pt1)));
            }
        }
    }
}
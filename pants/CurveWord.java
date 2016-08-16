public class CurveWord{
    public String word;
    public HIsometry[] shiftedIsometries;
    public HPants pants;
    public HLine[] axes;
    public CurveWord(String word, HPants pants){
        this.word = word;
        this.pants = pants;
        for(int i = 0; i<word.length(); i++){
            if (!("abAB".contains(word.substring(i, i+1)))){
                throw new IllegalArgumentException("Word must only contain letters: a, b, A, B");
            }
        }
        this.shiftedIsometries = new HIsometry[this.word.length()];
        for(int i = 0; i < this.word.length(); i++){
            // fill up shiftedIsometries with the cyclically shifted words
            HIsometry isom = new HIsometry(new LinearFractionalIsometry(1.0, 0.0, 0.0, 1.0));
            for(int j = i; j < i+word.length(); j++){
                int letterIdx = j % word.length();
                if(word.charAt(letterIdx) == 'a'){
                    isom = isom.compose(this.pants.a_translate);
                } else if (word.charAt(letterIdx) == 'b'){
                    isom = isom.compose(this.pants.b_translate);
                } else if (word.charAt(letterIdx) == 'A'){
                    isom = isom.compose(this.pants.a_translate.inverse());
                } else if (word.charAt(letterIdx) == 'B'){
                    isom = isom.compose(this.pants.b_translate.inverse());
                }
            }
            this.shiftedIsometries[i] = isom;
        }
        // find the axes
        this.axes = new HLine[this.word.length()];
        for(int i = 0; i < this.word.length(); i++){
            this.axes[i] = this.shiftedIsometries[i].axis();
        }
    }
}
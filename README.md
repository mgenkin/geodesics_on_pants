# Geodesics On Pants

This is a sketch in Processing 3.1 to visualize the geodesic representatives of curve homotopy classes in the 3-holed sphere.

To try it out, download and install Processing and open the sketch.  What you will see is the fundamental octagon of the 3-holed sphere visualized in the Poincare disk.  The red segments are the boundary components, and the blue and green segments are identified with the other segment of their color to make the pair of pants.

Typing the letters a, b, A, and B will define your curve in the way described in the image below:

!['a' is a loop around one boundary component and 'b' around another, while the third is equivalent to 'ab'.  Capital letters are inverses of lowercase letters.](/pantscut.png?raw=true)

Moving the sliders in the top left of the screen with your mouse changes the lengths of the "seams" between the punctures.  These three numbers are enough to describe the hyperbolic metric.

I'm working on making the processing.js web version work, but for now this will have to be it.  Sorry!

Please email me at genkin.matt@gmail.com if you have any questions/concerns, want to know how it works, or want to know why it isn't working.  Also please let me know if you use it for something cool.
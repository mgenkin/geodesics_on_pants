Here is my proposal for the next couple weeks.


### Style requests ###

 1. Use mostly small classes with one purpose (e.g. a ComplexNumber class designed only to manage complex algebraic operations). This is mainly for two reasons. One, so that the presence of a method in a class is never surprising to the reader. Two, so that it is easy to find a method with a given purpose. Java does allow multiple classes in a source file, if that helps.
 2. Use specific and accurate method and class names. Be willing to change these names as the purposes evolve.
 3. Let's try to keep the Processing-specific code to just one file, like the `.pde` file. That way the core operations are in pure Java that could be more easily ported elsewhere.

### Math classes ###

 1. ComplexNumber
 2. Vector3D
 3. Vector2D
 4. UpperHalfPlanePoint
 5. ConformalDiscPoint
 6. ProjectiveDiscPoint
 7. HPoint (i.e. a point in the abstract hyperbolic plane)
 8. HLine (and HLineSegment?)
 9. HRightHexagon
 10. Matrix2D
 11. Matrix3D
 12. LinearFractionalTransformation
 13. UpperHalfPlaneIsometry
 14. ConformalDiscIsometry
 15. ProjectiveDiscIsometry
 16. HIsometry

### Their data ###

  * Complex numbers are pairs `(a,b)` of real numbers.
  * An upper half plane point is a complex number with positive imaginary part.
  * A conformal disc point (in the Poincare disc) is a complex number with norm less than 1.
  * A projective disc point is a vector `(a,b,c)`, well-defined up to non-zero scaling, such that

        a^2 + b^2 < c^2

    (Equivalently, since the scale is immaterial, a vector of the form `(a,b)=(a,b,1)` such that `a^2 + b^2 < 1`.)
  * A hyperbolic plane point should be a managed list of points from all the models: The upper half plane and the Poincare/Klein discs.
  * A hyperbolic plane line should be a managed list of pairs of "ideal boundary" points from all the models. In some models there should be more data associated with it. For example:
     1. In the projective model, a line is also associated with the "polar point" as discussed in our meetings (the point of intersection of certain tangent lines).
     2. In the conformal disc, a line is represented by a circular arc, so it makes sense to keep track of its center.
     3. In the upper half plane, a line is also represented by a circular arc, so it makes sense to keep track of its center as well (which is on the real line).
  * A hyperbolic right hexagon should be a list of 6 cyclically-ordered points and correponding lines, in which consecutive pairs of lines are orthogonal. If 3 alternating side lengths are given, the remaining side lengths of a right hexagon are given by the formula:

        cosh(a side) = ( cosh(one of its neighbours) cosh(other neighbor) + cosh(opposite) ) / 
                         ( sinh(one neighbor) sinh(other neighbor )

    See [http://library.msri.org/books/gt3m/PDF/2.pdf](http://library.msri.org/books/gt3m/PDF/2.pdf) page 23.
  * A linear fractional transformation, or Moebius transformation, is a homeomorphism of the Riemann sphere (the complex numbers together with infinity) represented by a matrix of complex numbers:

        | a b |
        | c d |

    The effect on a complex number `z` (allowing `z = infinity`) is:

       z -> (az+b)/(cz+d)

  * If the entries of the matrix of a linear fractional transformation are real numbers, the transformation is an isometry of the upper half plane.

  * A conformal disc isometry can be obtained from an upper half plane isometry by "conjugation" as follows. There is a special fractional transformation

        z -> (-z+i)/(-iz+1)  

    and its inverse

        z -> (z-i)/(iz-1)

    They map the disc to the upper half plane, and back, respectively.
    [See these notes, page 1](http://www.maths.manchester.ac.uk/~cwalkden/hyperbolic-geometry/lecture06.pdf).
    To get a fractional transformation of the conformal disc:

      1. Go from the disc to the upper half plane
      2. Do a real fractional transformation
      3. Go back to the disc
     
    In other words, the matrices of the desired transformations are of the form of the matrix product:

        |1 -i| |a b| |-1 i|
        |i -1| |c d| |-i 1|

  * A projective disc isometry is represented by an invertible 3 by 3 matrix of real numbers `M` such that

        B inverse_transpose(M) B = M

    or equivalently

        transpose(M) B M = B

    where `B` is the matrix

        |1  0  0|
        |0  1  0|
        |0  0 -1|

    The effect of `M` on a point `(a,b,c)` is the matrix product `M` times column vector `(a,b,c)`.

    The point is that these matrices represent the projective plane transformations that preserve the circle represented by points

        (a,b,c)  such that  a^2 + b^2 = c^2

    Note that `M` and any non-zero multiple of `M` represent the same transformation.

  * We can regard an abstract hyperbolic plane isometry as a consistent managed list of isometries in each model.


### Their operations ###

We will need:

  * addition, subtraction, multiplication, and division of complex numbers
  * conversions between points/lines of different models
  * distance and angle functions
  * constructors from different types of defining data, e.g. to create an HLine perpendicular to a given HLine and containing a given point, or to create an HRightHexagon from 3 given side lengths based at a given point on a given line.
  * methods that compose isometries and apply them to the models


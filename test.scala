package test
import scala.math
import RayTracer.Vector3
import RayTracer.Ray

object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")
    println(sqrtplus1(123))
    val myVal : String = "Foo"
    println(myVal)
    val p1 = Person("Fred Flinstone")
    val p2 = Person.apply("Fred Flinstone")

    println("Value of pi: " + math.Pi)
    println("Square root of 4: " + math.sqrt(4))
    println("Square root of 5: " + math.sqrt(5))

    // allocating memory of 1D Array of string.  
        var days = Array("Sunday", "Monday", "Tuesday",  
                    "Wednesday", "Thursday", "Friday", 
                    "Saturday" )
      
    println("Array elements are : ") 
    for ( m1 <-days ) {
        println(m1 )
    }

    val sample = new Sample(32, 34)
    println(Sample.staticMeth(12, 2))
    println(sample.instMeth(3))

    /*var capital = Map("d" -> "a", "asd" -> "af")
    capital += ("Japan" -> "Tokyo")
    for (c <- capital.keys)
      capital(c) = capital(c).capitalize

    assert(capital("asd") == "Af")*/

    val ray = new Ray(Vector3(1, 2, 3), Vector3(1, 2, 3))
    ray.printDirection
    ray.printOrigin
    val vec = new Vector3(1, 1, 1)
    println("mag = " + vec.getMagnitude)
    vec.printVector
    vec.normalize
    println("mag = " + vec.getMagnitude)
    vec.printVector

    val vec1 = new Vector3(1, 1, 1)
    val vec2 = new Vector3(2, 2, 2)
    val vec3 = vec1 + vec2
    vec3.printVector
    val vec4 = vec3 * 2.0f
    vec4.printVector
  }

  def sqrtplus1(x: Int) = {
    math.sqrt(x) + 1.0
  }

  case class Person (var name : String) {

  }

  class Sample(x : Int = 1, val p : Int = 2) {
    def instMeth(y : Int = 4) = x + y
  }

  object Sample {
    def staticMeth(x : Int = 2, y : Int = 3) = x * y
  }

}

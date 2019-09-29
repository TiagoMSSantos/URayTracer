package RayTracer

class Intersection (
                     var intersectionPoint : Vector3 = Vector3(),
                     var distance : Float = Float.MaxValue,
                     var ray : Ray,
                     var sphere : Sphere = Sphere()) {
  val intersected : Boolean = if (distance < Float.MaxValue) true else false
  val color : Vector3 = sphere.color

  def printIntersection() {
    println("intersection:")
    if (this.intersected) {
      println("ray org: " + this.ray.origin.x + ", " + this.ray.origin.y + ", " + this.ray.origin.z)
      println("ray dir: " + this.ray.direction.x + ", " + this.ray.direction.y + ", " + this.ray.direction.z)
      println("point: " + this.intersectionPoint.x + ", " + this.intersectionPoint.y + ", " + this.intersectionPoint.z)
      println("distance: " + this.distance)
      println("color: " + this.color.x + ", " + this.color.y + ", " + this.color.z)
    } else {
      println("not intersected")
    }
    println("")
  }

  def getColor : Int = {
    val color : Vector3 = this.color
    val red : Int = Math.min(Math.max((color.x * 255).toInt, 0), 255)
    val green : Int = Math.min(Math.max((color.y * 255).toInt, 0), 255)
    val blue : Int =  Math.min(Math.max((color.z * 255).toInt, 0), 255)
    val alpha : Int = 255
    val res : Int = red << 24 | green << 16 | blue << 8 | alpha
    res
  }

}

object Intersection {
  def apply (
              intersectionPoint : Vector3 = Vector3(),
              distance : Float = Float.MaxValue,
              ray : Ray) : Intersection = {
    new Intersection(intersectionPoint, distance, ray)
  }
}

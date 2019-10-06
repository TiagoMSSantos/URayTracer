package RayTracer

class Intersection (
                     var intersectionPoint : Vector3 = Vector3(),
                     var intersectionNormal : Vector3 = Vector3(),
                     var distance : Float = Float.MaxValue,
                     var material : Material = Material(Vector3(), Vector3())) {
  val intersected : Boolean = distance > 0 && distance < Float.MaxValue

  def printIntersection() {
    println("intersection:")
    if (this.intersected) {
      println("point: " + this.intersectionPoint.x + ", " + this.intersectionPoint.y + ", " + this.intersectionPoint.z)
      println("distance: " + this.distance)
    } else {
      println("not intersected")
    }
    println("")
  }



}

object Intersection {
  def apply (
              intersectionPoint : Vector3 = Vector3(),
              intersectionNormal : Vector3 = Vector3(),
              distance : Float = Float.MaxValue,
              material : Material = Material(Vector3(), Vector3())) : Intersection = {
    new Intersection(intersectionPoint, intersectionNormal, distance, material)
  }
}

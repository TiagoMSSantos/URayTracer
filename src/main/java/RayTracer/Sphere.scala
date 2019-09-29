package RayTracer

class Sphere(var radius : Float = 0, var center : Vector3, var emission : Vector3, var color : Vector3) {

  def intersect(ray: Ray, intersection : Intersection): Intersection = {
    val vecToCenter: Vector3 = this.center - ray.origin // Solve t^2*d.d + 2*t*(o-p).d + (o-p).(o-p)-R^2 = 0
    val epsilon: Float = 1e-4f
    var t: Float = 0
    val vectorProjectionCenterOnDir: Float = vecToCenter * ray.direction
    var det: Float = vectorProjectionCenterOnDir * vectorProjectionCenterOnDir - vecToCenter * vecToCenter + this.radius * this.radius
    var distance: Float = 0

    if (det < 0) {
      distance = 0
    } else {
      det = math.sqrt(det).toFloat
      t = vectorProjectionCenterOnDir - det
      distance = 0
      if (t > epsilon) {
        distance = t
      } else {
        t = vectorProjectionCenterOnDir + det
        if (t > epsilon) {
          distance = t
        } else {
          distance = 0
        }
      }
      val intsersectionPoint : Vector3 = ray.origin + ray.direction * distance
      val newIntersection : Intersection = new Intersection (intsersectionPoint, distance, ray, this)
      val res = if (intersection.distance <= newIntersection.distance) intersection else newIntersection
      return res
    }
    intersection
  }

}

object Sphere {
  def apply(radius: Float = 0, center: Vector3 = Vector3(), emission: Vector3 = Vector3(), color: Vector3 = Vector3()): Sphere = {
    new Sphere(radius, center, emission, color)
  }
}

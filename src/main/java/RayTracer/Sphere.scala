package RayTracer

class Sphere(var radius : Float = 0, var center : Vector3, var material : Material) {

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
          return intersection
        }
      }

      if (distance > epsilon && distance < intersection.distance ) {
        val intsersectionPoint : Vector3 = ray.origin + ray.direction * distance
        val intsersectionNormal : Vector3 = (intsersectionPoint - this.center)
        val newIntersection : Intersection = Intersection (intsersectionPoint, intsersectionNormal.normalize(), distance, this.material, intersected = true)
        return newIntersection
      }
    }
    intersection
  }

}

object Sphere {
  def apply(radius: Float = 0, center: Vector3 = Vector3(), material : Material): Sphere = {
    new Sphere(radius, center, material)
  }
}

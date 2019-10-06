package RayTracer

class Plane (var point : Vector3, var normal : Vector3, var material : Material) {
  def intersect(ray: Ray, intersection: Intersection): Intersection = {
    val epsilon: Float = 1e-4f
    val denominator: Float = ray.direction * this.normal
    if (Math.abs(denominator) < epsilon) {
      return intersection
    }
    val distance : Float = ((this.point - ray.origin) * this.normal) / denominator
    if (distance > epsilon && distance < intersection.distance) {
      val intersectionPoint : Vector3 = ray.origin + ray.direction * distance
      val newIntersection = Intersection(intersectionPoint, this.normal.normalize(), distance, this.material, intersected = true)
      return newIntersection
    }
    intersection
  }
}

object Plane {
  def apply (point : Vector3, normal : Vector3, material : Material): Plane = {
    new Plane(point, normal.normalize(), material)
  }
}

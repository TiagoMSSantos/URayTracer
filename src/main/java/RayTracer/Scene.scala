package RayTracer

class Scene(var spheres: Array[Sphere], var planes: Array[Plane], var lights: Array[Light]) {

  def trace(ray: Ray): Intersection = {
    var intersection: Intersection = Intersection(Vector3(), Vector3(), ray.distance, Material(Vector3(), Vector3()))
    for (sphere <- spheres) {
      intersection = sphere.intersect(ray, intersection)
    }
    for (plane <- planes) {
      intersection = plane.intersect(ray, intersection)
    }
    intersection
  }

  def shadowTrace(ray: Ray): Boolean = {
    var intersection: Intersection = Intersection(Vector3(), Vector3(), ray.distance, Material(Vector3(), Vector3()))
    for (sphere <- spheres) {
      intersection = sphere.intersect(ray, intersection)
      if (intersection.intersected) {
        return false
      }
    }
    for (plane <- planes) {
      intersection = plane.intersect(ray, intersection)
      if (intersection.intersected) {
        return false
      }
    }
    true
  }

}

object Scene {
  def apply(spheres: Array[Sphere], planes: Array[Plane], lights: Array[Light]): Scene = {
    new Scene(spheres, planes, lights)
  }
}

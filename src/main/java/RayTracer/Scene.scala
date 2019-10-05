package RayTracer

class Scene(var spheres: Array[Sphere], var lights: Array[Light]) {

  def trace(ray: Ray): Intersection = {
    var intersection: Intersection = Intersection(Vector3(), Vector3(), Float.MaxValue, Material(Vector3(), Vector3()))
    for (sphere <- spheres) {
      intersection = sphere.intersect(ray, intersection)
    }
    intersection
  }

  def shadowTrace(ray: Ray): Boolean = {
    var intersection: Intersection = Intersection(Vector3(), Vector3(), Float.MaxValue, Material(Vector3(), Vector3()))
    for (sphere <- spheres) {
      intersection = sphere.intersect(ray, intersection)
    }
    !intersection.intersected
  }

}

object Scene {
  def apply(spheres: Array[Sphere], lights : Array[Light]): Scene = {
    new Scene(spheres, lights)
  }
}

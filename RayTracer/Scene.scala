package RayTracer

class Scene (spheres : Array[Sphere]) {
  def apply (spheres : Array[Sphere]): Scene = {
    new Scene(spheres)
  }

  def intersect (ray : Ray): Intersection = {
    var intersection : Intersection = Intersection (Vector3(0,0,0), Float.MaxValue, ray)
    for (i: Int <- spheres.indices) {
      intersection = spheres.apply(i).intersect(ray, intersection)
    }
    intersection
  }
}

object Scene {
  def apply (spheres : Array[Sphere]) : Scene = {
    new Scene(spheres)
  }
}

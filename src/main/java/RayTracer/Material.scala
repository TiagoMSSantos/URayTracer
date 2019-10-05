package RayTracer

class Material (var kd : Vector3, var ks : Vector3) {

}

object Material {
  def apply (kd : Vector3, ks : Vector3): Material = {
    new Material(kd, ks)
  }
}

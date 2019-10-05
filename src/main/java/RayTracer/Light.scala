package RayTracer

class Light (var point : Vector3, var emission : Vector3) {

}

object Light {
  def apply(point : Vector3, emission : Vector3): Light = {
    new Light(point, emission)
  }
}

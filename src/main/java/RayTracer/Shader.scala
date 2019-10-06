package RayTracer

class Shader() {

  def trace (ray: Ray, scene: Scene): Vector3 = {
    val intersection = scene.trace(ray)
    if (intersection.intersected) {
      return shade(intersection, ray, scene)
    }
    Vector3()
  }

  def shade (intersection: Intersection, ray: Ray, scene: Scene): Vector3 = {
    var color : Vector3 = Vector3()
    if (intersection.intersected && ray.depth <= 5) {
      val intersectionNormal = intersection.intersectionNormal.normalize()
      val intersectionPoint = intersection.intersectionPoint

      val enteringObject = ray.direction.normalize() * intersectionNormal < 0
      val shadingNormal : Vector3 = if (enteringObject) intersectionNormal else intersectionNormal.inverted()

      if (intersection.material.kd.hasValue()) {
        for(light <- scene.lights) {
          val dirToLight : Vector3 = light.point - intersectionPoint
          val projectionDir = dirToLight * shadingNormal
          if (projectionDir > 0) {
            val distanceToLight = dirToLight.getMagnitude
            val newRay : Ray  = Ray(dirToLight.normalize(), intersectionPoint, distanceToLight, ray.depth + 1)
            val intersectedLight = scene.shadowTrace(newRay)
            if (intersectedLight) {
              color = intersection.material.kd.mult(light.emission) * projectionDir
            }
          }
        }
      }

      if (intersection.material.ks.hasValue()) {
        val newDir : Vector3 = calculateReflectionDir(ray.direction.normalize(), shadingNormal.normalize())
        val newRay : Ray  = Ray(newDir.normalize(), intersectionPoint, Float.MaxValue, ray.depth + 1)
        val newIntersection : Intersection = scene.trace(newRay)
        if(newIntersection.intersected) {
          color = newIntersection.material.kd.mult(intersection.material.ks)
        }
      }

    }
    color
  }

  def calculateReflectionDir (dirInc : Vector3, normal : Vector3) : Vector3 = {
    // Rr = Ri - 2 N (Ri . N)
    val projectionDir : Float = dirInc * normal
    val reflectionDir : Vector3 = dirInc - ((normal * projectionDir) * 2.0f)
    reflectionDir.normalize()
  }

  def calculateRefractionDir (dirInc : Vector3, normal : Vector3) : Vector3 = {
    val refractionDir : Vector3 = dirInc - normal * (2 * (normal * dirInc))
    refractionDir
  }

}

object Shader {

  def apply(): Shader = {
    new Shader()
  }

}

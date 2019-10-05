package RayTracer

class Shader() {

  def shade (intersection: Intersection, ray: Ray, scene: Scene): Vector3 = {
    var color : Vector3 = Vector3()
    if (intersection.intersected && ray.depth <= 5) {
      val enteringObject = intersection.intersectionNormal * ray.direction > 0
      val shadingNormal : Vector3 = if (enteringObject) intersection.intersectionNormal else intersection.intersectionNormal.inverted()
      val intersectionPoint = intersection.intersectionPoint

      if (intersection.material.kd.hasValue()) {
        for(light <- scene.lights) {
          val dirToLight : Vector3 = light.point - intersectionPoint
          val projectionDir = dirToLight * shadingNormal
          if (projectionDir > 0) {
            val distanceToLight = dirToLight.getMagnitude
            val newRay : Ray  = Ray(dirToLight, intersectionPoint, distanceToLight, ray.depth + 1)
            val intersectedLight = scene.shadowTrace(newRay)
            if (intersectedLight) {
              color = intersection.material.kd.mult(light.emission) * projectionDir
            }
          }
        }
      }

      if (intersection.material.ks.hasValue()) {
        val newDir : Vector3 = calculateReflectionDir(ray.direction, shadingNormal)
        val newRay : Ray  = Ray(newDir, intersectionPoint, Float.MaxValue, ray.depth + 1)
        val newIntersection : Intersection = scene.trace(newRay)
        if(newIntersection.intersected) {
          color = newIntersection.material.kd
        }
      }

    }
    color
  }

  def calculateReflectionDir (dirInc : Vector3, normal : Vector3) : Vector3 = {
    val projectionDir : Float = 2.0f * (normal * dirInc.inverted())

    val reflectionDir : Vector3 = (normal * projectionDir) - dirInc.inverted()

    // Rr = Ri - 2 N (Ri . N)

    /*
        specDir = new myVect(shadingN);

        specDir.mult (2.f * shadingN.dot(r.dir.symmetric()));
        specDir.sub (r.dir.symmetric());

        specDir.normalize();
     */

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

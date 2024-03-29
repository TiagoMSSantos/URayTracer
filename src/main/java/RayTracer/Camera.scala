package RayTracer

class Camera (position : Vector3, lookAt : Vector3, var width : Int, var height : Int, hFov : Float, vFov : Float) {
  val up : Vector3 = Vector3 (0, 1)
  val direction : Vector3 = (this.lookAt - this.position).normalize()
  val right : Vector3 = this.up.crossProduct(this.direction).normalize()
  val hfov : Float = (hFov * Math.PI.toFloat)  / 180f
  val vfov : Float = (vFov * Math.PI.toFloat)  / 180f

  def castRay(x: Int, y: Int) : Ray = {
    //view space coordinates
    val centerOfPixelX = x + 0.5f
    val centerOfPixelY = y + 0.5f
    val us = -0.5f + (centerOfPixelX / this.width)
    val vs = -0.5f + (centerOfPixelY / this.height)
    val ws = 1.0f

    val u_alpha = this.hfov * (us - 0.0f)
    val v_alpha = -this.vfov * (vs - 0.0f)

    val dirX = Math.atan(u_alpha).toFloat
    val dirY = Math.atan(v_alpha).toFloat
    val dirZ = ws

    val direction = Vector3(dirX, dirY, dirZ)
    val origin = this.position

    val ray : Ray = Ray(direction.normalize(), origin, Float.MaxValue, 1)
    ray
  }

}

object Camera {
  // convenience constructors
  def apply(position : Vector3, lookAt : Vector3, width : Int, height : Int, hFov : Float, vFov : Float) : Camera = {
    val camera = new Camera(position, lookAt, width, height, hFov, vFov)
    camera
  }

}

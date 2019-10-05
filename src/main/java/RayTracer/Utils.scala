package RayTracer

class Utils {

}

object Utils {
  def convertColorToInt (color : Vector3) : Int = {
    val red : Int = Math.min(Math.max((color.x * 255).toInt, 0), 255)
    val green : Int = Math.min(Math.max((color.y * 255).toInt, 0), 255)
    val blue : Int =  Math.min(Math.max((color.z * 255).toInt, 0), 255)
    val alpha : Int = 255
    val res : Int = red << 24 | green << 16 | blue << 8 | alpha
    res
  }
}

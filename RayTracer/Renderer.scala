package RayTracer

class Renderer (camera : Camera, scene : Scene) {
  private val resX = camera.width
  private val resY = camera.height
  var image : Array[Int] = new Array[Int](this.resX * this.resY)
  var imageF : Array[Float] = new Array[Float](this.resX * this.resY * 3)

  def render () {

    for (y <- 0 until this.resY) {
      for (x <- 0 until this.resX) {
        val ray = camera.castRay(x, y)
        val intersection = scene.intersect(ray)
        println("x: " + x + ", y: " + y)
        intersection.printIntersection()
        val pixelIndex : Int = y * this.resY + x
        val pixelColor : Int = intersection.getColor
        image.update(pixelIndex, pixelColor)
        imageF.update(0, intersection.color.x)
        imageF.update(1, intersection.color.y)
        imageF.update(2, intersection.color.z)
      }
    }

  }

}

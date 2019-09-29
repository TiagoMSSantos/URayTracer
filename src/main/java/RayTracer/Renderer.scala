package RayTracer

import java.nio.ByteBuffer

class Renderer (camera : Camera, scene : Scene) {
  val resX: Int = camera.width
  val resY: Int = camera.height
  private val imageF : Array[Float] = new Array[Float](this.resX * this.resY * 4)
  val image : ByteBuffer = ByteBuffer.allocateDirect(resX * resY* 4)

  def render () {
    for (y <- 0 until this.resY) {
      for (x <- 0 until this.resX) {
        val ray = camera.castRay(x, y)
        val intersection = scene.intersect(ray)
        println("x: " + x + ", y: " + y)
        intersection.printIntersection()
        val pixelIndex : Int = ((this.resY - 1 - y) * this.resX + x) * 4
        val pixelColor : Int = intersection.getColor

        val bb: Array[Byte] = convertPixelColorToByte(pixelColor)

        image.put(pixelIndex + 0, bb.apply(0))
        image.put(pixelIndex + 1, bb.apply(1))
        image.put(pixelIndex + 2, bb.apply(2))
        image.put(pixelIndex + 3, bb.apply(3))

        imageF.update(pixelIndex + 0, intersection.color.x)
        imageF.update(pixelIndex + 1, intersection.color.y)
        imageF.update(pixelIndex + 2, intersection.color.z)
        imageF.update(pixelIndex + 3, 0.0f)
      }
    }
  }

  def printImage () {
    for (y <- 0 until this.resY) {
      for (x <- 0 until this.resX) {
        val pixelIndex : Int = ((this.resY - 1 - y) * this.resX + x) * 4
        val pixelColor : Int = image.get(pixelIndex)

        val red: Byte = image.get(pixelIndex + 0)
        val green: Byte = image.get(pixelIndex + 1)
        val blue: Byte = image.get(pixelIndex + 2)
        val alpha: Byte = image.get(pixelIndex + 3)

        val redF: Float = imageF.apply(pixelIndex + 0)
        val greenF: Float = imageF.apply(pixelIndex + 1)
        val blueF: Float = imageF.apply(pixelIndex + 2)
        val alphaF: Float = imageF.apply(pixelIndex + 3)

        println("x: " + x + ", y: " + y + ", pixel: " + ((this.resY - 1 - y) * this.resX + x) + ", color: " + (pixelColor & 255) +
          ", red: " + (red & 255) + ", green: " + (green & 255) + ", blue: " + (blue & 255) + ", alpha: " + (alpha & 255) +
          ", redF: " + redF + ", greenF: " + greenF + ", blueF: " + blueF + ", alphaF: " + alphaF)
      }
    }
    println("\n")
  }

  def convertPixelColorToByte (pixelColor : Int): Array[Byte] = {
    val bb = java.nio.ByteBuffer.allocate(4)
    bb.putInt(pixelColor)
    bb.flip  // now can read instead of writing
    bb.array()
  }

}

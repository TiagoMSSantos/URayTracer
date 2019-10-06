package RayTracer

import java.nio.ByteBuffer

class Renderer (camera : Camera, scene : Scene, shader : Shader) {
  val resX: Int = camera.width
  val resY: Int = camera.height
  val image : ByteBuffer = ByteBuffer.allocateDirect(resX * resY* 4)

  def render () {
    for (y <- 0 until this.resY) {
      for (x <- 0 until this.resX) {
        val ray = camera.castRay(x, y)
        val color = shader.trace(ray, scene)

        val pixelIndex : Int = ((this.resY - 1 - y) * this.resX + x) * 4
        val pixelColor : Int = Utils.convertColorToInt(color)

        val bb: Array[Byte] = convertPixelColorToByte(pixelColor)

        image.put(pixelIndex + 0, bb.apply(0))
        image.put(pixelIndex + 1, bb.apply(1))
        image.put(pixelIndex + 2, bb.apply(2))
        image.put(pixelIndex + 3, bb.apply(3))
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

        println("x: " + x + ", y: " + y + ", pixel: " + ((this.resY - 1 - y) * this.resX + x) + ", color: " + (pixelColor & 255) +
          ", red: " + (red & 255) + ", green: " + (green & 255) + ", blue: " + (blue & 255) + ", alpha: " + (alpha & 255))
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

object Renderer {
  def apply(camera : Camera, scene : Scene, shader : Shader): Renderer = {
    new Renderer(camera, scene, shader)
  }
}

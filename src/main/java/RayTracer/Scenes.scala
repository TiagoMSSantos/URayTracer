package RayTracer

class Scenes {

}

object Scenes {
  def cornellBox () : Scene = {
    val lightGrayMat = Material(Vector3(0.9f, 0.9f, 0.9f), Vector3())
    val redMat = Material(Vector3(0.9f), Vector3())
    val greenMat = Material(Vector3(0, 0.9f), Vector3(0, 0.2f))
    val blueMat = Material(Vector3(0, 0, 0.9f), Vector3())
    val mirrorMat = Material(Vector3(), Vector3(0.8f, 0.8f, 0.8f))

    val mirrorSphere : Sphere = Sphere(0.4f, Vector3(0.45f, -0.65f, 0.4f), mirrorMat)
    val greenSphere : Sphere = Sphere(0.5f, Vector3(-0.45f, -0.1f), greenMat)

    val backWall : Plane = Plane(Vector3(0.0f, 0.0f, 1.0f), Vector3(0.0f, 0.0f, -1.0f), lightGrayMat)
    val floor : Plane = Plane(Vector3(0.0f, -1.0f), Vector3(0.0f, 1.0f), lightGrayMat)
    val ceiling : Plane = Plane(Vector3(0.0f, 1.0f), Vector3(0.0f, -1.0f), lightGrayMat)
    val leftWall : Plane = Plane(Vector3(-1.0f), Vector3(1.0f), redMat)
    val rightWall : Plane = Plane(Vector3(1.0f), Vector3(-1.0f), blueMat)

    val light : Light = Light(Vector3(0, 0.99f), Vector3(1.0f, 1.0f, 1.0f))

    val scene = Scene(Array(mirrorSphere, greenSphere),  Array(floor, leftWall, backWall, rightWall, ceiling), Array(light))
    scene
  }

  def spheres () : Scene = {
    val sphereRed : Sphere = Sphere(0.4f, Vector3(), Material(Vector3(1), Vector3()))
    val sphereGreen : Sphere = Sphere(0.5f, Vector3(0, -1f), Material(Vector3(0, 1), Vector3()))
    val sphereBlue : Sphere = Sphere(0.4f, Vector3(-1f), Material(Vector3(0, 0, 1), Vector3()))
    val sphereYellow : Sphere = Sphere(0.4f, Vector3(1), Material(Vector3(), Vector3(1, 1)))

    val plane : Plane = Plane(Vector3(0, -2), Vector3(0, 1), Material(Vector3(1, 1, 1), Vector3()))

    val light : Light = Light(Vector3(0, 10), Vector3(0.5f, 0.5f, 0.5f))

    val scene = Scene(Array(sphereRed, sphereGreen, sphereBlue, sphereYellow),  Array(plane), Array(light))
    scene
  }
}

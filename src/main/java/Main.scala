import java.nio.ByteBuffer

import RayTracer._
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.{GLFW_FALSE, GLFW_RESIZABLE, GLFW_TRUE, GLFW_VISIBLE, glfwCreateWindow, glfwDefaultWindowHints, glfwDestroyWindow, glfwGetPrimaryMonitor, glfwGetVideoMode, glfwGetWindowSize, glfwInit, glfwMakeContextCurrent, glfwSetErrorCallback, glfwSetKeyCallback, glfwSetWindowPos, glfwSetWindowShouldClose, glfwShowWindow, glfwSwapInterval, glfwTerminate, glfwWindowHint, glfwWindowShouldClose, _}
import org.lwjgl.glfw.{GLFW, GLFWErrorCallback}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryStack.{stackMallocFloat, stackPush}
import org.lwjgl.system.MemoryUtil.NULL

object Main {
  def main(args: Array[String]): Unit = {
    val camera = Camera(Vector3(0.0f, -0.5f, -4f), Vector3(0f, -0.5f), 100, 100, 45, 45)

    val sphereRed : Sphere = Sphere(0.4f, Vector3(), Material(Vector3(1), Vector3()))
    val sphereGreen : Sphere = Sphere(0.4f, Vector3(0, -1f), Material(Vector3(0, 1), Vector3()))
    val sphereBlue : Sphere = Sphere(0.4f, Vector3(-1f), Material(Vector3(0, 0, 1), Vector3()))
    val sphereYellow : Sphere = Sphere(0.4f, Vector3(1), Material(Vector3(), Vector3(1, 1)))
    val light : Light = Light(Vector3(0, 10), Vector3(0.5f, 0.5f, 0.5f))
    val scene = Scene(Array(sphereRed, sphereGreen, sphereBlue, sphereYellow), Array(light))
    val shader = Shader()

    val renderer = new Renderer(camera, scene, shader)
    renderer.render()
    val mainGL: MainGL = new MainGL()
    mainGL.renderGL(renderer.image, renderer.resX, renderer.resY)
    //renderer.printImage()
  }
}

class MainGL { // The window handle
  private var window = 0L
  private val windowWidth = 800
  private val windowHeight = 600

  def renderGL(image : ByteBuffer, resX: Int, resY: Int): Unit = {
    System.out.println("Hello LWJGL " + Version.getVersion + "!")

    initGL()
    val textureId = loadTexture(image, resX, resY)
    initVertices(textureId)
    loopGL()

    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
    System.out.println("Finished!")
  }

  def loadTexture(image : ByteBuffer, resX: Int, resY: Int): Int = {
    GL11.glEnable(GL11.GL_TEXTURE_2D)
    //create a texture
    val textureId = glGenTextures
    //bind the texture
    glBindTexture(GL_TEXTURE_2D, textureId)
    //tell opengl how to unpack bytes
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
    //set the texture parameters, can be GL_LINEAR or GL_NEAREST
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER)

    //upload texture
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, resX, resY, 0, GL_RGBA, GL_UNSIGNED_BYTE, image)
    // Generate Mip Map
    GL30.glGenerateMipmap(GL_TEXTURE_2D)
    textureId
  }

  private def initVertices(textureId : Int) {
    try {
      val stack = stackPush
      try {
        val bufferVertices = stackMallocFloat(3 * 2 * 2)
        bufferVertices.put(-1.0f).put(-1.0f)
        bufferVertices.put(+1.0f).put(-1.0f)
        bufferVertices.put(-1.0f).put(+1.0f)

        bufferVertices.put(+1.0f).put(-1.0f)
        bufferVertices.put(+1.0f).put(+1.0f)
        bufferVertices.put(-1.0f).put(+1.0f)
        bufferVertices.flip

        val vboVertices = glGenBuffers
        glBindBuffer(GL_ARRAY_BUFFER, vboVertices)
        glBufferData(GL_ARRAY_BUFFER, bufferVertices, GL_STATIC_DRAW)

      } finally if (stack != null) stack.close()
    }
    glEnableClientState(GL_VERTEX_ARRAY)
    glVertexPointer(2, GL_FLOAT, 0, 0L)

    try {
      val stack = stackPush
      try {
        val bufferColors = stackMallocFloat(3 * 4 * 2)
        bufferColors.put(1.0f).put(1.0f).put(1.0f).put(1.0f)
        bufferColors.put(1.0f).put(1.0f).put(1.0f).put(1.0f)
        bufferColors.put(1.0f).put(1.0f).put(1.0f).put(1.0f)
        bufferColors.put(1.0f).put(1.0f).put(1.0f).put(1.0f)
        bufferColors.put(1.0f).put(1.0f).put(1.0f).put(1.0f)
        bufferColors.put(1.0f).put(1.0f).put(1.0f).put(1.0f)

        bufferColors.flip

        val vboColors = glGenBuffers
        glBindBuffer(GL_ARRAY_BUFFER, vboColors)
        glBufferData(GL_ARRAY_BUFFER, bufferColors, GL_STATIC_DRAW)

      } finally if (stack != null) stack.close()
    }
    glEnableClientState(GL_COLOR_ARRAY)
    glColorPointer(4, GL_FLOAT, 0, 0L)

    try {
      val stack = stackPush
      try {
        val bufferNormals = stackMallocFloat(3 * 4 * 2)
        bufferNormals.put(0.0f).put(0.0f).put(+1.0f).put(0.0f)
        bufferNormals.put(0.0f).put(0.0f).put(+1.0f).put(0.0f)
        bufferNormals.put(0.0f).put(0.0f).put(+1.0f).put(0.0f)

        bufferNormals.put(0.0f).put(0.0f).put(-1.0f).put(0.0f)
        bufferNormals.put(0.0f).put(0.0f).put(-1.0f).put(0.0f)
        bufferNormals.put(0.0f).put(0.0f).put(-1.0f).put(0.0f)
        bufferNormals.flip

        val vboNormals = glGenBuffers
        glBindBuffer(GL_ARRAY_BUFFER, vboNormals)
        glBufferData(GL_ARRAY_BUFFER, bufferNormals, GL_STATIC_DRAW)

      } finally if (stack != null) stack.close()
    }
    glEnableClientState(GL_NORMAL_ARRAY)
    glNormalPointer(GL_FLOAT, 0, 0L)

    try {
      val stack = stackPush
      try {
        val bufferTexCoords = stackMallocFloat(3 * 2 * 2)
        bufferTexCoords.put(0.0f).put(0.0f)
        bufferTexCoords.put(1.0f).put(0.0f)
        bufferTexCoords.put(0.0f).put(1.0f)

        bufferTexCoords.put(1.0f).put(0.0f)
        bufferTexCoords.put(1.0f).put(1.0f)
        bufferTexCoords.put(0.0f).put(1.0f)
        bufferTexCoords.flip

        val vboTexCoords = glGenBuffers
        glBindBuffer(GL_ARRAY_BUFFER, vboTexCoords)
        glBufferData(GL_ARRAY_BUFFER, bufferTexCoords, GL_STATIC_DRAW)

      } finally if (stack != null) stack.close()
    }
    glEnableClientState(GL_TEXTURE_COORD_ARRAY)
    glTexCoordPointer(2, GL_FLOAT, 0, 0L)

    GL13.glActiveTexture(GL13.GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, textureId)
  }

  private def initGL(): Unit = { // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set
    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit) throw new IllegalStateException("Unable to initialize GLFW")
    // Configure GLFW
    glfwDefaultWindowHints() // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    // Create the window
    window = glfwCreateWindow(windowWidth, windowHeight, "URayTracer", NULL, NULL)
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window")
    }

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window: Long, key: Int, _: Int, action: Int, _: Int) => {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window, true)
      } // We will detect this in the rendering loop
    })

    // Get the thread stack and push a new frame
    try {
      val stack = stackPush
      try {
        val pWidth = stack.mallocInt(1) // int*
        val pHeight = stack.mallocInt(1)
        // Get the window size passed to glfwCreateWindow
        glfwGetWindowSize(window, pWidth, pHeight)
        // Get the resolution of the primary monitor
        val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
        // Center the window
        glfwSetWindowPos(window, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
      } finally if (stack != null) {
        stack.close()
      }
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window)
    // Enable v-sync
    glfwSwapInterval(1)
    // Make the window visible
    glfwShowWindow(window)

    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities
  }

  private def loopGL(): Unit = { // This line is critical for LWJGL's interoperation with GLFW's
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( {
      !glfwWindowShouldClose(window)
    }) {
      GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_ACCUM_BUFFER_BIT | GL_STENCIL_BUFFER_BIT) // clear the framebuffer
      glClearColor(0.0f, 0.0f, 0.0f, 0.0f) // Set the clear color
      // Poll for window events. The key callback above will only be
      // invoked during this call.
      GLFW.glfwPollEvents()

      glViewport(0, 0, windowWidth, windowHeight)
      glDrawArrays(GL_TRIANGLES, 0, 6)

      GLFW.glfwSwapBuffers(window) // swap the color buffers
    }
  }

}

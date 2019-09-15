import scala.math
import RayTracer.{Camera, Intersection, Ray, Renderer, Scene, Sphere, Vector3}
import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.{GLFW_FALSE, GLFW_RESIZABLE, GLFW_TRUE, GLFW_VISIBLE, glfwCreateWindow, glfwDefaultWindowHints, glfwDestroyWindow, glfwGetPrimaryMonitor, glfwGetVideoMode, glfwGetWindowSize, glfwInit, glfwMakeContextCurrent, glfwPollEvents, glfwSetErrorCallback, glfwSetKeyCallback, glfwSetWindowPos, glfwSetWindowShouldClose, glfwShowWindow, glfwSwapBuffers, glfwSwapInterval, glfwTerminate, glfwWindowHint, glfwWindowShouldClose}
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.{GL_COLOR_BUFFER_BIT, glClear, glClearColor}
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

import org.lwjgl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

object HelloWorld {
  def main(args: Array[String]): Unit = {
    val camera = Camera(Vector3(0f, -0.5f, -2f), Vector3(0f, -0.5f, 0f), 1, 1, 45, 45)
    val scene = Scene(Array(Sphere(0.5f, Vector3(0, 0, 0), Vector3(0, 0, 0), Vector3(1, 0, 0))))
    val renderer = new Renderer(camera, scene)
    renderer.render()
    new Main().renderGL(renderer.imageF)
  }
}

class Main { // The window handle
  private var window = 0L
  private var progHandle = -1

  def renderGL(image : Array[Float]): Unit = {
    System.out.println("Hello LWJGL " + Version.getVersion + "!")
    initGL()
    this.progHandle = GL20.glCreateProgram
    loopGL(image)
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
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
    window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL)
    if (window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window")
    }
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      def foo(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
          glfwSetWindowShouldClose(window, true)
        } // We will detect this in the rendering loop
      }

      foo(window, key, scancode, action, mods)
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
  }

  private def loopGL(image : Array[Float]): Unit = { // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities
    // Set the clear color
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while ( {
      !glfwWindowShouldClose(window)
    }) {
      GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer

      GLFW.glfwSwapBuffers(window) // swap the color buffers


      GL20.glUseProgram(progHandle)
      //glClearColor(image.apply(0), image.apply(1), image.apply(2), 1.0f)
      GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
      GL20.glDisableVertexAttribArray(0)

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      GLFW.glfwPollEvents()
    }
  }

}

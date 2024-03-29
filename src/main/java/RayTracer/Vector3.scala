package RayTracer

class Vector3(var x : Float = 0, var y : Float = 0, var z: Float = 0) {

  def getMagnitude: Float = {
    math.sqrt((this.x * this.x + this.y * this.y + this.z * this.z).toDouble).toFloat
  }

  def normalize(): Vector3 = {
    val magnitude = getMagnitude
    if (magnitude > 0) {
      this.x = this.x / magnitude
      this.y = this.y / magnitude
      this.z = this.z / magnitude
    }
    this
  }

  def printVector() {
    println("vector: " + this.x + ", " + this.y + ", " + this.z)
  }

  def +(other : Vector3): Vector3 = {
    Vector3(this.x + other.x, this.y + other.y, this.z + other.z)
  }

  def -(other : Vector3): Vector3 = {
    Vector3(this.x - other.x, this.y - other.y, this.z - other.z)
  }

  def inverted(): Vector3 = {
    Vector3(-this.x, -this.y, -this.z)
  }

  def *(other : Vector3): Float = {
    this.x * other.x + this.y * other.y + this.z * other.z
  }

  def mult(other : Vector3): Vector3 = {
    Vector3(this.x * other.x, this.y * other.y, this.z * other.z)
  }

  def *(value : Float): Vector3 = {
    Vector3(this.x * value, this.y * value, this.z * value)
  }

  def crossProduct(other : Vector3): Vector3 = {
    Vector3(
      this.y * other.z - this.z * other.y, 
      this.z * other.x - this.x * other.z,
      this.x * other.y - this.y * other.x
    )
  }

  def hasValue() : Boolean = {
    val hasX = this.x > 0
    val hasY = this.y > 0
    val hasZ = this.z > 0
    hasX || hasY || hasZ
  }

}

object Vector3 {
  // convenience constructors
  def apply(x: Float = 0, y: Float = 0, z: Float = 0): Vector3 = {
    val vector = new Vector3(x, y, z)
    vector
  }

}

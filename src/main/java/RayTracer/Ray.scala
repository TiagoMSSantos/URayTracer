package RayTracer

class Ray(var direction : Vector3, var origin : Vector3, var distance : Float, var depth : Integer) {
    def printDirection() {
        println("direction: " + direction.x + ", " + direction.y + ", " + direction.z)
    }

    def printOrigin() {
        println("origin: " + origin.x + ", " + origin.y + ", " + origin.z)
    }
}

object Ray {
    def apply (direction : Vector3, origin : Vector3, dist : Float, depth : Integer) : Ray = {
        new Ray (direction.normalize(), origin, dist, depth)
    }
}

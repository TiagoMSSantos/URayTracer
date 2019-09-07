package RayTracer

class Ray(var direction : Vector3, var origin : Vector3) {
    def apply(direction : Vector3, origin : Vector3) : Ray = {
        var ray = new Ray(direction, origin)
        ray
    }

    def printDirection() {
        println("direction: " + direction.x + ", " + direction.y + ", " + direction.z)
    }

    def printOrigin() {
        println("origin: " + origin.x + ", " + origin.y + ", " + origin.z)
    }
}
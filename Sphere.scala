package RayTracer
import scala.math

class Sphere(var radius : Float = 0, var center : Vector3, var emission : Vector3, var color : Vector3) {

    def intersect(ray : Ray) {
        var L = this.center - ray.origin
        var tc = L * ray.direction
        if (tc < 0) false

        var distance = math.sqrt((tc * tc) - (L * L))
        if (distance > this.radius) false

        var t1c = math.sqrt((this.radius * this.radius) - (distance * distance))

        var t1 = tc - t1c
        var t2 = tc + t1c
        //true
    }
    
}

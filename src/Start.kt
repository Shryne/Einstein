import csp.graph.Graph
import csp.graph.Vertex
import java.util.*


//####################################################
// vertex names
//####################################################
val brite = "Brite"
val schwede = "Schwede"
val däne = "Däne"
val norweger = "Norweger"
val deutscher = "Deutscher"

val rot = "rot"
val grün = "grün"
val gelb = "gelb"
val weiß = "weiß"
val blau = "blau"

val hund = "Hund"
val vogel = "Vogel"
val katze = "Katze"
val pferd = "Pferd"
val fisch = "Fisch"

val tee = "Tee"
val kaffee = "Kaffee"
val milch = "Milch"
val bier = "Bier"
val wasser = "Wasser"

val pallmall = "Pall Mall"
val dunhill = "Dunhill"
val marlboro = "Marlboro"
val winfield = "Windfield"
val rothmanns = "Rothmanns"

val nations = arrayListOf(brite, schwede, däne, norweger, deutscher)
val colors = arrayListOf(rot, grün, gelb, weiß, blau)
val pets = arrayListOf(hund, vogel, katze, pferd, fisch)
val drinks = arrayListOf(tee, kaffee, milch, bier, wasser)
val cigarettes = arrayListOf(pallmall, dunhill, marlboro, winfield, rothmanns)

fun main(args: Array<String>) {
    val domain = arrayListOf(0, 1, 2, 3, 4)

    val graph: Graph = Graph()

    //####################################################
    // add vertices
    //####################################################
    with(graph, {
        addVertices(nations, domain)
        addVertices(colors, domain)
        addVertices(pets, domain)
        addVertices(drinks, domain)
        addVertices(cigarettes, domain)
    })

    //####################################################
    // constraints
    //####################################################
    // unary
    val middleHouse = { x: Int -> x == 2 }
    val firstHouse = { x: Int -> x == 0}

    // binary
    val equal = { x: Int , y: Int -> x == y }
    val unequal = { x: Int, y: Int -> x != y }

    val neighbour = { x: Int, y: Int -> Math.abs(x - y) == 1 }
    val leftTo = { x: Int, y: Int -> x < y }
    val rightTo = { x: Int, y: Int -> x > y }

    //####################################################
    // add edges
    //####################################################
    with(graph, {
        // unary
        addEdge(milch, middleHouse)             // 7
        addEdge(norweger, firstHouse)           // 9

        // binary
        addEdge(brite, rot, equal)              // 1
        addEdge(schwede, hund, equal)           // 2
        addEdge(däne, tee, equal)               // 3
        addEdge(grün, kaffee, equal)            // 5
        addEdge(pallmall, vogel, equal)         // 6
        addEdge(gelb, dunhill, equal)           // 8
        addEdge(winfield, bier, equal)          // 12
        addEdge(deutscher, rothmanns, equal)    // 14

        addEdge(marlboro, katze, neighbour)     // 10
        addEdge(pferd, dunhill, neighbour)      // 11
        addEdge(norweger, blau, neighbour)      // 13
        addEdge(marlboro, wasser, neighbour)    // 15

        addEdge(grün, weiß, leftTo)             // 4
        addEdge(weiß, grün, rightTo)            // 4


        addEdge(rot, brite, equal)              // 1
        addEdge(hund, schwede, equal)           // 2
        addEdge(tee, däne, equal)               // 3
        addEdge(kaffee, grün, equal)            // 5
        addEdge(vogel, pallmall, equal)         // 6
        addEdge(dunhill, gelb, equal)           // 8
        addEdge(bier, winfield, equal)          // 12
        addEdge(rothmanns, deutscher, equal)    // 14

        addEdge(katze, marlboro, neighbour)     // 10
        addEdge(dunhill, pferd, neighbour)      // 11
        addEdge(blau, norweger, neighbour)      // 13
        addEdge(wasser, marlboro, neighbour)    // 15
    }) // 26 constraints

    // all different constraints - because we don't want the
    // german to have a dog and a horse at the same time
    // (all nations for example have to be in different rows)
    permuteAdd(graph, nations, unequal)
    permuteAdd(graph, colors, unequal)
    permuteAdd(graph, drinks, unequal)
    permuteAdd(graph, cigarettes, unequal)
    permuteAdd(graph, pets, unequal)

    println(graph.solveCSP())

    printResult(graph.vertices)
}

/**
 * A method to sort all the variables based on their number (row) and store them
 * inside the corresponding row.
 */
fun printResult(vertices: ArrayList<Vertex>) {
    for (i in 0..4) {
        val row = ArrayList<Vertex>()

        vertices.forEach {
            if (it.domain.contains(i))
                row.add(it)
        }
        println("Row $i: $row")
    }
}

/**
 * Some helper for the equality constraints. Takes a list of vertex names and the constraint
 * and adds them all as edges into the graph.
 *
 * A list of n vertex names would lead to (n ^ 2 - n) constraints.
 */
fun permuteAdd(graph: Graph, toPermute: List<String>, constraint: (Int, Int) -> Boolean) {
    for (x in toPermute)
        for (y in toPermute)
            if (x != y) graph.addEdge(x, y, constraint)
}
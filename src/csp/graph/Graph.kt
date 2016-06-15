package csp.graph

import java.util.*

/**
 * A graph to hold the vertices and edges for a
 * constraint solving problem. Note that this class
 * is meant to be mutable. After the call of solveCSP()
 * the state of this graph will be changed, whether
 * their is a solution or not.
 *
 * Later will cause a broken graph and the return of
 * false from solveCSP().
 */
class Graph {
    var vertices = ArrayList<Vertex>()
    var edges = ArrayList<Edge>()

    //#####################################################
    // algorithm methods
    //#####################################################
    /**
     * Steps to solve the csp:
     * 1) Node consistency
     * 2) Edge consistency
     * 3) Instantiation and testing
     */
    fun solveCSP(): Boolean {
        nc()
        ac3()
        return instantiateRec(0, vertices[0].domain)
    }

    /**
     * Establishes node consistency for all vertices of
     * the given graph. This function corresponds to the nc
     * procedure in the pdf (page 49).
     */
    private fun nc() {
        // for each V in nodes(G)
        vertices.forEach { it.nc() }
    }

    /**
     * Establishes consistency for the graph. This function corresponds
     * to the ac3 procedure in the pdf (page 59)
     */
    private fun ac3() {
        val edges = ArrayList(edges)

        while (!edges.isEmpty()) {
            val edge = edges.removeAt(0)

            if (edge.revise()) {
                edges.addAll(edgesWithTo(edge.from, { it.from != edge.from } ))
            }
        }

    }

    /**
     * Checks whether the current state of the vertices is consistent
     * starting from the current vertex index. In this algorithm we just
     * want to check the vertices after the current vertex, because the
     * others were already checked.
     * Returns true for an inconsistent graph.
     *
     * This function corresponds to the given algorithm in the pdf (page
     * 91).
     */
    private fun ac3la(vertexIndex: Int): Boolean {
        val currentVertex = vertices[vertexIndex]

        /// Q <- {(Vi,Vcv) in arcs(G),i>cv};
        //println("All my edges are: $edges")
        val edges = edgesWithTo(currentVertex, { vertices.indexOf(it.from) > vertexIndex })

        /// consistent <- true;
        var consistent = true

        /// while not Q empty & consistent
        while (!edges.isEmpty() && consistent) {
            /// select and delete any arc (Vk,Vm) from Q;
            val edge = edges.removeAt(0)

            /// if REVISE(Vk,Vm) then
            if (edge.revise()) {
                /// Q <- Q union {(Vi,Vk)
                edges.addAll(edgesWithTo(edge.from,
                        /// such that (Vi,Vk) in arcs(G),i#k,i#m,i>cv}
                        { it.from != edge.to &&
                                vertices.indexOf(it.from) > vertexIndex }))

                /// consistent <- not Dk empty
                consistent = !edge.from.domain.isEmpty()
            }
        }

        return consistent
    }

    /**
     * This function instantiates each vertex one by one and checks
     * whether the instantiation leads to a consistent state. Otherwise
     * the state will be restored and the next value will be taken.
     *
     * It is part of the algorithm, but not explicitly mentioned in
     * the sheets (at least not with pseudo code).
     */
    private fun instantiateRec(cv: Int, currentDomain: MutableList<Int>): Boolean {
        val backupGraph = backup()
        val currentVertex = vertices[cv]

        if (currentDomain.isEmpty()) return false;
        currentVertex.domain = arrayListOf(currentDomain.removeAt(0))

        if (ac3la(cv)) {
            if (cv + 1 >= vertices.size) return true
            if (instantiateRec(cv + 1, vertices[cv + 1].domain)) return true
        }

        restore(backupGraph)
        return instantiateRec(cv, currentDomain)
    }

    //#####################################################
    // other
    //#####################################################
    /**
     * This method needs to be called before addEdge.
     */
    fun addVertex(name: String, domain: List<Int>) {
        vertices.add(Vertex(name, ArrayList(domain)))
    }

    /**
     * The bulk version of addVertex.
     */
    fun addVertices(nameList: List<String>, domain:List<Int>) {
        for (n in nameList)
            addVertex(n, domain)
    }

    /**
     * In general one have to be careful with constraints in terms of division.
     * Something like x / 2 == y could lead to unexpected behaviour.
     *
     * Furthermore this method relies in the previous insertion of the
     * given vertices with addVertex/addVertices.
     */
    fun addEdge(vertexName1: String, vertexName2: String, constraint: (Int, Int) -> Boolean) {
        val newEdge = Edge(vertices.get(vertexName1), vertices.get(vertexName2), constraint)
        edges.add(newEdge)
    }

    /**
     * AddEdges for unary constraints.
     */
    fun addEdge(vertexName: String, constraint: (Int) -> Boolean) {
        vertices.get(vertexName).addConstraint(constraint)
    }

    override fun toString() = "${javaClass.simpleName}$vertices"
    //#####################################################
    // helper
    //#####################################################
    /**
     * Returns all edges that have the given vertex as the target,
     * except the without vertex.
     *
     * This function is used to model lines like "{(Vi,Vcv) in arcs(G),i>cv}" (page 91).
     *
     * We use the ArrayList constructor, because .filter returns a List that
     * is immutable and we need a mutable list.
     */

    private fun edgesWithTo(to: Vertex, additionalCondition: (Edge) -> Boolean) =
            ArrayList(edges.filter { it.to == to && additionalCondition(it) })

    /**
     * Returns the vertex with the given name.
     */
    private fun ArrayList<Vertex>.get(vertexName: String): Vertex =
        first { it.name == vertexName }

    /**
     * Returns a deepcopy of this graph.
     */
    private fun backup(): Graph {
        val graph = Graph()

        vertices.forEach {
            v -> graph.addVertex(v.name, ArrayList(v.domain))
        }

        edges.forEach {
            e -> graph.addEdge(e.from.name, e.to.name, e.constraint)
        }

        return graph;
    }

    /**
     * Restores this graph based on the given graph.
     */
    private fun restore(graph: Graph) {
        vertices.clear()
        edges.clear()

        graph.vertices.forEach { vertices.add(it) }
        graph.edges.forEach { edges.add(it) }
    }
}


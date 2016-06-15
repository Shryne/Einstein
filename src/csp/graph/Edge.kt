package csp.graph

/**
 * An edge is a combination of two vertices and a binary constrain
 * between.
 */
class Edge(internal val from: Vertex,
           internal val to: Vertex,
           internal val constraint: (Int, Int) -> Boolean) {

    //#####################################################
    // algorithm methods
    //#####################################################
    /**
     * This method removes all the domain elements from the first
     * vertex that aren't consistent in terms of the constraint
     * of this edge with the second vertex.
     * It's the implementation of the algorithm given in the
     * pdf on page 50 to establish edge consistency.
     *
     * Returns true, if any elements were removed.
     */
    internal fun revise() =
        /// for each X in Di do (Di == from.domain element)
        /// if there is no such Y in Dj such (...)
        /// delete X from Di, return true
        from.domain.removeIf {
            /// that (X,Y) is consistent
            !to.domain.consistent(it, constraint)
        }

    //#####################################################
    // other
    //#####################################################
    override fun toString() =
            "${javaClass.simpleName}[$from, $to]"
}
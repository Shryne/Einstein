package csp.graph

import java.util.*

/**
 * Representing our Variables. The name vertex is taken because we
 * want to represent our net with a graph and a vertex seems to be
 * a more satisfying name.
 *
 * This class Takes an ArrayList of Int's to be constructed.
 */
class Vertex(val name: String, var domain: ArrayList<Int>) {
    /*
    unConstraint: Integer -> Boolean
    biConstraint: Integer, Integer -> Boolean

    Unfortunately kotlin doesn't support type aliases yet.
     */

    //#####################################################
    // variables
    //#####################################################
    // only getters
    /**
     * A list of all unary constraints pointing to this vertex. A unary
     * constraint is a lambda taking an integer (the domain element - from this
     * vertex) and returning a boolean
     * (true in case of a valid value and false otherwise).
     */
    private val unConstraints = ArrayList<(Int) -> Boolean>()

    //#####################################################
    // algorithm methods
    //#####################################################
    /**
     * Establishes the node consistency, by removing every element of the domain
     * that isn't valid in terms of the given unary constraints.
     */
    internal fun nc() {
        /// for each X in the domain D of V
        /// (...)
        /// delete X from D;
        domain.removeIf {
            unConstraints.inconsistent(it)
        }
    }
    //#####################################################
    // other
    //#####################################################
    internal fun addConstraint(unaryConstraint: (Int) -> Boolean) {
        unConstraints.add(unaryConstraint)
    }

    override fun toString() =
        "${javaClass.simpleName}($name)$domain"

    //#####################################################
    // helper
    //#####################################################
    /**
     * This an extension for all collections with the generic type
     * Integer -> Boolean (unary constraint). It is used to get
     * a more readable version of the nc method.
     *
     * Collection<Integer -> Boolean> gets the new method "inconsistent"
     * and it returns a boolean, if there is any constraint inside this
     * collection that isn't valid with the taken domainElem.
     *
     * The higher performance resulting from checking for an invalid value instead
     * of whether all values are valid is the reason for writing a inconsistent
     * method instead of a consistent method
     */
    private fun Collection<(Int) -> Boolean>
            /// if any unary constraint on V is inconsistent with X
            .inconsistent(domainElem: Int) = any { !it(domainElem) }
}

/**
 * This extension is meant to check the consistency of one element
 * with all the elements in the collection in terms of the given
 * constraint.
 */
internal fun Collection<Int>
        .consistent(otherElem: Int, constraint: (Int, Int) -> Boolean) =
        // are there any elements in this collection that are inconsistent
        // with the given element by the given constraint?
        any { constraint(otherElem, it) }
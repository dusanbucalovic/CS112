package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 *
 * @author Dusan Bucalovic, db1119, db1119@scarletmail.rutgers.edu
 */
public class Polynomial {

    /**
     * Reads a polynomial from an input stream (file or keyboard). The storage format
     * of the polynomial is:
     * <pre>
     *     <coeff> <degree>
     *     <coeff> <degree>
     *     ...
     *     <coeff> <degree>
     * </pre>
     * with the guarantee that degrees will be in descending order. For example:
     * <pre>
     *      4 5
     *     -2 3
     *      2 1
     *      3 0
     * </pre>
     * which represents the polynomial:
     * <pre>
     *      4*x^5 - 2*x^3 + 2*x + 3
     * </pre>
     *
     * @param sc Scanner from which a polynomial is to be read
     * @return The polynomial linked list (front node) constructed from coefficients and
     * degrees read from scanner
     * @throws IOException If there is any input error in reading the polynomial
     */
    public static Node read(Scanner sc)
            throws IOException {
        Node poly = null;
        while (sc.hasNextLine()) {
            Scanner scLine = new Scanner(sc.nextLine());
            poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
            scLine.close();
        }
        return poly;
    }

    private static Node append(Node head, float coeff, int degree) {

        if (head == null) {
            return new Node(coeff, degree, null);
        }

        Node tmp = getLast(head);
        tmp.next = new Node(coeff, degree, null);
        return head;
    }

    private static Node getLast(Node head) {
        Node tmp = head;
        while (tmp != null && tmp.next != null) {
            tmp = tmp.next;
        }
        return tmp;
    }

    private static Node clone(Node node) {
        if (node == null)
            return null;

        Node clone = new Node(node.term.coeff, node.term.degree, null);
        Node tmpHead = clone;

        Node tmp = node;
        while (tmp != null && tmp.next != null) {
            tmp = tmp.next;
            tmpHead.next = new Node(tmp.term.coeff, tmp.term.degree, null);
            tmpHead = tmpHead.next;
        }
        return clone;
    }

    /**
     * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
     * The returned polynomial MUST have all new nodes. In other words, none of the nodes
     * of the input polynomials can be in the result.
     *
     * @param poly1 First input polynomial (front of polynomial linked list)
     * @param poly2 Second input polynomial (front of polynomial linked list
     * @return A new polynomial which is the sum of the input polynomials - the returned node
     * is the front of the result polynomial
     */
    public static Node add(Node poly1, Node poly2) {
        /** COMPLETE THIS METHOD **/
        // FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
        // CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION

        Node tmp1 = poly1;
        Node tmp2 = poly2;
        Node sum = null;

        // Terms are stored in ASCENDING order of degrees from front to rear in a non-circular linked list.
        while (tmp1 != null && tmp2 != null) {
            if (tmp1.term.degree == tmp2.term.degree) {
                float coeff = tmp1.term.coeff + tmp2.term.coeff;
                if (coeff != 0)
                    sum = append(sum, coeff, tmp1.term.degree);
                tmp1 = tmp1.next;
                tmp2 = tmp2.next;
            } else if (tmp1.term.degree > tmp2.term.degree) {
                if (tmp2.term.coeff != 0)
                    sum = append(sum, tmp2.term.coeff, tmp2.term.degree);
                tmp2 = tmp2.next;
            } else {
                if (tmp1.term.coeff != 0)
                    sum = append(sum, tmp1.term.coeff, tmp1.term.degree);
                tmp1 = tmp1.next;
            }
        }

        // One of the list might be longer, so there could be some remaining items
        Node tmp = getLast(sum);

        // if poly1 is longer
        if (tmp == null && tmp1 != null && tmp2 == null) {
            return clone(tmp1);
        } else if (tmp != null && tmp1 != null && tmp2 == null) {
            tmp.next = clone(tmp1);
        }

        // if poly2 is longer
        if (tmp == null && tmp1 == null && tmp2 != null) {
            return clone(tmp2);
        } else if (tmp != null && tmp1 == null && tmp2 != null) {
            tmp.next = clone(tmp2);
        }

        return sum;
    }


    /**
     * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
     * The returned polynomial MUST have all new nodes. In other words, none of the nodes
     * of the input polynomials can be in the result.
     *
     * @param poly1 First input polynomial (front of polynomial linked list)
     * @param poly2 Second input polynomial (front of polynomial linked list)
     * @return A new polynomial which is the product of the input polynomials - the returned node
     * is the front of the result polynomial
     */
    public static Node multiply(Node poly1, Node poly2) {
        /** COMPLETE THIS METHOD **/
        // FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
        // CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION

        Node tmp1 = poly1;
        Node result = null;
        while (tmp1 != null) {
            Node tmp2 = poly2;
            Node multi = null;
            while (tmp2 != null) {
                multi = append(multi, tmp1.term.coeff * tmp2.term.coeff, tmp1.term.degree + tmp2.term.degree);
                tmp2 = tmp2.next;
            }
            tmp1 = tmp1.next;
            result = add(result, multi);
        }
        return result;
    }

    /**
     * Evaluates a polynomial at a given value.
     *
     * @param poly Polynomial (front of linked list) to be evaluated
     * @param x    Value at which evaluation is to be done
     * @return Value of polynomial p at x
     */
    public static float evaluate(Node poly, float x) {
        /** COMPLETE THIS METHOD **/
        // FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
        // CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
        Node tmp = poly;
        float result = 0;
        while (tmp != null) {
            result += tmp.term.coeff * Math.pow(x, tmp.term.degree);
            tmp = tmp.next;
        }
        return result;
    }

    /**
     * Returns string representation of a polynomial
     *
     * @param poly Polynomial (front of linked list)
     * @return String representation, in descending order of degrees
     */
    public static String toString(Node poly) {
        if (poly == null) {
            return "0";
        }

        String retval = poly.term.toString();
        for (Node current = poly.next; current != null;
             current = current.next) {
            retval = current.term.toString() + " + " + retval;
        }
        return retval;
    }
}

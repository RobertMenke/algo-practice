package problem_of_the_week.trees;

class Node {
    int data;
    Node left;
    Node right;

    public Node(int data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }
}

// https://www.hackerrank.com/challenges/is-binary-search-tree/problem
public class IsThisABST {
    boolean checkBST(Node root) {
        boolean leftCheck = true;
        boolean rightCheck = true;

        if (root.left != null) {
            leftCheck = checkBST(root.left);
        }

        if (root.right != null) {
            rightCheck = checkBST(root.right);
        }

        if (!leftCheck || !rightCheck) {
            return false;
        }

        if (root.left != null && maxSubtree(root.left) >= root.data) {
            return false;
        }

        if (root.right != null && minSubtree(root.right) <= root.data) {
            return false;
        }

        return true;
    }

    int maxSubtree(Node root) {
        int currentMax = root.data;

        if (root.left != null) {
            currentMax = Math.max(currentMax, maxSubtree(root.left));
        }

        if (root.right != null) {
            currentMax = Math.max(currentMax, maxSubtree(root.right));
        }

        return Math.max(currentMax, root.data);
    }

    int minSubtree(Node root) {
        int currentMin = root.data;

        if (root.left != null) {
            currentMin = Math.min(currentMin, minSubtree(root.left));
        }

        if (root.right != null) {
            currentMin = Math.min(currentMin, minSubtree(root.right));
        }

        return Math.min(currentMin, root.data);
    }

    public static void main(String[] args) {
        Node sample = new Node(
            3,
            new Node(
                5,
                new Node(1, null, null),
                new Node(4, null, null)
            ),
            new Node(
                2,
                new Node(6, null, null),
                null
            )
        );

        IsThisABST instance = new IsThisABST();
        System.out.println(instance.checkBST(sample));
    }
}

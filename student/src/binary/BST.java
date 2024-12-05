public class BST {
    private Node root;

    private Node insert(Node node, Student student) {
        if (node == null) {
            return new Node(student);
        }
        if (student.id < node.student.id) {
            node.left = insert(node.left, student);
        } else if (student.id > node.student.id) {
            node.right = insert(node.right, student);
        }
        return node;
    }

    private Node search(Node node, int id) {
        if (node == null || node.student.id == id) {
            return node;
        }

        if (id < node.student.id) {
            return search(node.left, id);
        }

        return search(node.right, id);
    }

    private void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            node.student.printStudent();
            inorder(node.right);
        }
    }

    public BST() {
        root = null;
    }

    public void insert(Student student) {
        root = insert(root, student);
    }

    public Student search(int id) {
        Node result = search(root, id);
        if (result != null) {
            return result.student;
        }
        return null;
    }

    public void inorder() {
        inorder(root);
    }
}

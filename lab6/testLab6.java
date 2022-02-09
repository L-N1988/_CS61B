package lab6;

public class testLab6 {
    public static void main(String[] args) {
        UnionFind qf = new UnionFind(10);
        qf.union(0, 1);
        qf.union(2, 1);
        qf.union(2, 3);
        qf.union(4, 5);
        qf.union(4, 6);
        qf.union(4, 7);
        System.out.println(qf.connected(0, 2));
        System.out.println(qf.connected(0, 3));
        System.out.println(qf.connected(0, 5));
        System.out.println(qf.connected(0, 7));
    }
}


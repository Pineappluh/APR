public class PermutationMatrix extends Matrix {

    private int permutationsDone = 0;

    public PermutationMatrix(int n) {
        super(n, n, 0);
        for(int i = 0; i < n; i++) {
            setElement(i, i, 1);
        }
    }

    @Override
    public void swapRows(int i1, int i2) {
        super.swapRows(i1, i2);
        permutationsDone++;
    }

    public int getPermutationsDone() {
        return permutationsDone;
    }
}

package math;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Matrix {

    private static double epsilon = 10e-12;

    private Map<Integer, Double> elements = new HashMap<>();

    private int n;

    private int m;

    private Matrix() {
    }

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
    }

    public Matrix(int n, int m, double element) {
        this(n, m);
        for(int i = 0; i < n * m; i++) {
            this.elements.put(i, element);
        }
    }

    public Matrix(int n, int m, double ... elements) {
        this(n, m);
        for(int i = 0; i < elements.length; i++) {
            this.elements.put(i, elements[i]);
        }
    }

    public Matrix(String path) {
        int rows = 0;
        try(Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] rowElements = row.split("\\s+");
                this.n = rowElements.length;
                for(int i = 0; i < n; i++) {
                    setElement(rows, i, Double.valueOf(rowElements[i]));
                }
                rows++;
            }
        } catch (Exception ex) {
            System.err.println("Error occurred when trying to initialize matrix from given file: " + ex.getClass());
        }
        this.m = rows;
    }


    public int getRows() {
        return m;
    }

    public int getColumns() {
        return n;
    }

    public Matrix assign(Matrix mat) {
        this.elements.clear();
        this.elements.putAll(mat.elements);
        this.n = mat.n;
        this.m = mat.m;
        return this;
    }

    public double getElement(int i, int j) {
        return elements.get(j + i * n);
    }

    public void setElement(int i, int j, double el) {
        elements.put(j + i * n, el);
    }

    public Matrix map(BiFunction<Integer, Double, Double> f) {
        Matrix result = this.copy();
        result.elements.forEach((k, v) -> result.elements.put(k, f.apply(k, v)));
        return result;
    }

    public Matrix add(Matrix mat) {
        if (this.n != mat.n || this.m != mat.m) {
            throw new RuntimeException("Can't add two matrices with different dimensions");
        }

        Matrix result = new Matrix(n, m);
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                result.setElement(i, j, this.getElement(i, j) + mat.getElement(i, j));
            }
        }
        return result;
    }

    public Matrix added(Matrix mat) {
        return this.assign(this.add(mat));
    }

    public Matrix subtract(Matrix mat) {
        if (this.n != mat.n || this.m != mat.m) {
            throw new RuntimeException("Can't subtract two matrices with different dimensions");
        }

        Matrix result = new Matrix(n, m);
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                result.setElement(i, j, this.getElement(i, j) - mat.getElement(i, j));
            }
        }
        return result;
    }

    public Matrix subtracted(Matrix mat) {
        return this.assign(this.subtract(mat));
    }

    public Matrix transpose() {
        Matrix result = new Matrix(m, n);
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                result.setElement(i, j, this.getElement(j, i));
            }
        }
        return result;
    }

    public Matrix multiply(Matrix mat) {
        if (this.n != mat.m) {
            throw new RuntimeException("Can't multiply matrices with incompatible dimensions.");
        }

        Matrix result = new Matrix(mat.n, this.m);
        for(int i = 0; i < result.m; i++) {
            for(int j = 0; j < result.n; j++) {
                double sum = 0;
                for(int k = 0; k < mat.m; k++) {
                    sum += this.getElement(i, k) * mat.getElement(k, j);
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }

    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(n, m);
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                result.setElement(i, j, scalar * this.getElement(i, j));
            }
        }
        return result;
    }

    public Matrix forwardSubstitution(Matrix b) {
        if (this.m != this.n || b.m != this.n) {
            throw new RuntimeException("Incorrect dimensions.");
        }

        Matrix y = b.copy();
        for(int i = 0; i < n - 1; i++) {
            for(int j = i + 1; j < n; j++) {
                y.subtractElement(j, 0, this.getElement(j, i) * y.getElement(i, 0));
            }
        }
        return y;
    }

    public Matrix backwardSubstitution(Matrix y) {
        if (this.m != this.n || y.m != this.n) {
            throw new RuntimeException("Incorrect dimensions.");
        }

        Matrix x = y.copy();
        for(int i = n - 1; i >= 0; i--) {
            double el = getElement(i, i);
            if (Math.abs(el) < epsilon) {
                throw new RuntimeException("Error occurred during backward substitution, tried to divide with zero!");
            }
            x.divideElement(i, 0, el);
            for(int j = 0; j <= i - 1; j++) {
                x.subtractElement(j, 0, this.getElement(j, i) * x.getElement(i, 0));
            }
        }
        return x;
    }

    public void LU() {
        for(int i = 0; i < n - 1; i++) {
            LUDivideAndSubtract(i);
        }
    }

    public PermutationMatrix LUP() {
        PermutationMatrix P = new PermutationMatrix(n);
        for(int i = 0; i < n - 1; i++) {
            int pivot = i;
            for(int j = i + 1; j < n; j++) {
                if(Math.abs(getElement(j, i)) > Math.abs(getElement(i, i))) {
                    pivot = j;
                }
            }
            P.swapRows(i, pivot);
            this.swapRows(i, pivot);
            LUDivideAndSubtract(i);
        }
        return P;
    }

    private void LUDivideAndSubtract(int i) {
        for(int j = i + 1; j < n; j++) {
            if (Math.abs(getElement(i, i)) < epsilon) {
                throw new RuntimeException("Error occurred during LU decomposition, pivot was 0!");
            }
            divideElement(j, i, getElement(i, i));
            for(int k = i + 1; k < n; k++) {
                subtractElement(j, k, getElement(j, i) * getElement(i, k));
            }
        }
    }

    public void addElement(int i, int j, double value) {
        setElement(i, j, getElement(i, j) + value);
    }

    public void divideElement(int i, int j, double divider) {
        setElement(i, j, getElement(i, j) /  divider);
    }

    public void subtractElement(int i, int j, double subtrahend) {
        addElement(i, j, -subtrahend);
    }

    public void swapRows(int i1, int i2) {
        if(i1 == i2) {
            return;
        }

        for(int j = 0; j < n; j++) {
            swapElements(i1, j, i2, j);
        }
    }

    private void swapElements(int i1, int j1, int i2, int j2) {
        double tmp = getElement(i1, j1);
        setElement(i1, j1, getElement(i2, j2));
        setElement(i2, j2, tmp);
    }

    public Matrix inverse() {
        Matrix A = this.copy();
        Matrix P = A.LUP();
        Matrix E = createIdentityMatrix(n);
        Matrix X = new Matrix(n, m);
        for(int i = 0; i < n; i++) {
            Matrix y = A.forwardSubstitution(P.multiply(E.getColumn(i)));
            Matrix x;
            try {
                x = A.backwardSubstitution(y);
            } catch(Exception ex) {
                throw new RuntimeException("Matrix is singular.");
            }
            X.setColumn(i, x);
        }
        return X;
    }

    public double determinant() {
        Matrix A = this.copy();
        PermutationMatrix P = A.LUP();
        double determinant = 1;
        for(int i = 0; i < n; i++) {
            determinant *= A.getElement(i, i);
        }
        return Math.pow(-1, P.getPermutationsDone()) * determinant;
    }

    public Matrix getColumn(int j) {
        Matrix col = new Matrix(1, m);
        for(int i = 0; i < m; i++) {
            col.setElement(i, 0, getElement(i, j));
        }
        return col;
    }

    public void setColumn(int j, Matrix column) {
        for(int i = 0; i < m; i++) {
            setElement(i, j, column.getElement(i, 0));
        }
    }

    public double getEuclideanNorm() {
        if (n != 1) {
            throw new RuntimeException("Can only calculate the euclidean norm if the matrix is a column vector!");
        }

        double norm = 0;
        for (int i = 0; i < getRows(); i++) {
            norm += Math.pow(getElement(i, 0), 2);
        }
        return Math.sqrt(norm);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matrix)) {
            return false;
        }
        Matrix mat = (Matrix) o;
        return equals(mat, epsilon);
    }

    public boolean equals(Matrix mat, double epsilon) {
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < n; j++) {
                if(Math.abs(this.getElement(i, j) - mat.getElement(i, j)) > epsilon) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    public void writeToFile(String name) {
        try(PrintWriter writer = new PrintWriter(name, "UTF-8")) {
            for(int i = 0; i < m; i++) {
                writer.println(rowAsString(i));
            }
        } catch (Exception ex) {
            System.err.println("Error occurred when trying to write matrix to file: " + ex.getClass());
        }
    }

    public void printToConsole() {
       System.out.println(this.toString());
    }

    private String rowAsString(int i) {
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < n; j++) {
            sb.append(String.format("%.10f", getElement(i, j)));
            if (j != n - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder matrixAsString = new StringBuilder();
        for(int i = 0; i < m; i++) {
            matrixAsString.append(rowAsString(i));
            if (i != m - 1) {
                matrixAsString.append("\n");
            }
        }
        return matrixAsString.toString();
    }

    public Matrix copy() {
        Matrix tmp = new Matrix();
        return tmp.assign(this);
    }

    public static Matrix createIdentityMatrix(int n) {
        Matrix I = new Matrix(n, n, 0);
        for (int i = 0; i < n; i++) {
            I.setElement(i, i, 1);
        }
        return I;
    }

    public static void solveLUP(Matrix A, Matrix b) {
        try {
            Matrix P = A.LUP();
            Matrix y = A.forwardSubstitution(P.multiply(b));
            Matrix x = A.backwardSubstitution(y);
            System.out.println("LUP result:");
            x.printToConsole();
        } catch(Exception ex) {
            System.out.println("Failed to solve with LUP, division with 0 occurred. \n");
        }

    }

    public static void solveLU(Matrix A, Matrix b) {
        try {
            A.LU();
            Matrix y = A.forwardSubstitution(b);
            Matrix x = A.backwardSubstitution(y);
            System.out.println("LU result:");
            x.printToConsole();
        } catch(Exception ex) {
            System.out.println("Failed to solve with LU, division with 0 occurred. \n");
        }

    }

    public static void main(String[] args) {
        System.out.println("2. zad");
        Matrix A = new Matrix(3, 3, 3, 9, 6, 4, 12, 12, 1, -1, 1);
        Matrix b = new Matrix(1, 3, 12, 12, 1);
        solveLU(A.copy(), b);
        solveLUP(A.copy(), b);

        System.out.println("3. zad");
        A = new Matrix(3, 3, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix LU = A.copy();
        LU.LU();
        System.out.println("LU decomp:");
        LU.printToConsole();
        Matrix LUP = A.copy();
        LUP.LUP();
        System.out.println("LUP decomp:");
        LUP.printToConsole();
        solveLU(A.copy(), b);
        solveLUP(A.copy(), b);

        System.out.println("4. zad");
        A = new Matrix(3, 3, 0.000001, 3000000, 2000000, 1000000, 2000000, 3000000, 2000000, 1000000, 2000000);
        b = new Matrix(1, 3, 12000000.000001, 14000000, 10000000);
        solveLU(A.copy(), b);
        solveLUP(A.copy(), b);

        System.out.println("5. zad");
        A = new Matrix(3, 3, 0, 1, 2, 2, 0, 3, 3, 5, 1);
        b = new Matrix(1, 3, 6, 9, 3);
        solveLU(A.copy(), b);
        solveLUP(A.copy(), b);

        System.out.println("6. zad");
        A = new Matrix(3, 3, 4000000000.0, 1000000000.0, 3000000000.0, 4, 2, 7, 0.0000000003, 0.0000000005, 0.0000000002);
        b = new Matrix(1, 3, 9000000000.0, 15, 0.0000000015);
        epsilon = 10e-6;
        solveLUP(A.copy(), b);
        System.out.println("Epsilon was 10e-6, solution with smaller epsilon is:");
        epsilon = 10e-12;
        solveLUP(A.copy(), b);

        System.out.println("7. zad");
        A = new Matrix(3, 3, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        try {
            A.inverse().printToConsole();
        } catch(Exception ex) {
            System.out.println("Failed to calculate inverse, matrix is singular. \n");
        }

        System.out.println("8. zad");
        A = new Matrix(3, 3, 4, -5, -2, 5, -6, -2, -8, 9, 3);
        A.inverse().printToConsole();

        System.out.println("9. zad");
        A = new Matrix(3, 3, 4, -5, -2, 5, -6, -2, -8, 9, 3);
        System.out.println(A.determinant() + "\n");

        System.out.println("10. zad");
        A = new Matrix(3, 3, 3, 9, 6, 4, 12, 12, 1, -1, 1);
        System.out.println(A.determinant());
    }
}

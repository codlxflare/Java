class Complex {
    private double real;
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imaginary + other.imaginary);
    }

    public Complex multiply(Complex other) {
        double realPart = this.real * other.real - this.imaginary * other.imaginary;
        double imaginaryPart = this.real * other.imaginary + this.imaginary * other.real;
        return new Complex(realPart, imaginaryPart);
    }

    public Complex conjugate() {
        return new Complex(this.real, -this.imaginary);
    }

    public String toString() {
        if (imaginary >= 0) {
            return real + " + " + imaginary + "i";
        } else {
            return real + " - " + (-imaginary) + "i";
        }
    }
}

class ComplexMatrix {
    private Complex[][] matrix;
    private int rows;
    private int cols;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new Complex[rows][cols];
    }

    public void setElement(int row, int col, Complex value) {
        matrix[row][col] = value;
    }

    public Complex getElement(int row, int col) {
        return matrix[row][col];
    }

    public ComplexMatrix add(ComplexMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Matrices must have the same dimensions for addition.");
        }

        ComplexMatrix result = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setElement(i, j, this.getElement(i, j).add(other.getElement(i, j)));
            }
        }
        return result;
    }

    public ComplexMatrix multiply(ComplexMatrix other) {

        ComplexMatrix result = new ComplexMatrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex sum = new Complex(0, 0);
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.getElement(i, k).multiply(other.getElement(k, j)));
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }

    public ComplexMatrix transpose() {
        ComplexMatrix result = new ComplexMatrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setElement(j, i, this.getElement(i, j));
            }
        }
        return result;
    }

    public Complex determinant() {
        if (rows != cols) {
            throw new UnsupportedOperationException("Not determinant.");
        }
        return determinantHelper(matrix);
    }

    private Complex determinantHelper(Complex[][] matrix) {
        int n = matrix.length;
        if (n == 1) {
            return matrix[0][0];
        } else if (n == 2) {
            return matrix[0][0].multiply(matrix[1][1]).add(matrix[0][1].multiply(matrix[1][0]).multiply(new Complex(-1, 0)));
        } else {
            Complex det = new Complex(0, 0);
            for (int j = 0; j < n; j++) {
                Complex[][] subMatrix = new Complex[n - 1][n - 1];
                for (int k = 1; k < n; k++) {
                    for (int l = 0, m = 0; l < n; l++) {
                        if (l != j) {
                            subMatrix[k - 1][m++] = matrix[k][l];
                        }
                    }
                }
                Complex sign = (j % 2 == 0) ? new Complex(1, 0) : new Complex(-1, 0);
                det = det.add(matrix[0][j].multiply(sign).multiply(determinantHelper(subMatrix)));
            }
            return det;
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(matrix[i][j]).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

public class Main {
    public static void main(String[] args) {

        ComplexMatrix realMatrix1 = new ComplexMatrix(2, 2);
        realMatrix1.setElement(0, 0, new Complex(1, 3));
        realMatrix1.setElement(0, 1, new Complex(2, 1));
        realMatrix1.setElement(1, 0, new Complex(3, 3));
        realMatrix1.setElement(1, 1, new Complex(4, 1));

        ComplexMatrix realMatrix2 = new ComplexMatrix(2, 2);
        realMatrix2.setElement(0, 0, new Complex(5, 0));
        realMatrix2.setElement(0, 1, new Complex(6, 0));
        realMatrix2.setElement(1, 0, new Complex(7, 0));
        realMatrix2.setElement(1, 1, new Complex(8, 0));

        System.out.println("Matrix 1:");
        System.out.println(realMatrix1);
        System.out.println("Matrix 2:");
        System.out.println(realMatrix2);

        System.out.println("+:");
        ComplexMatrix realSum = realMatrix1.add(realMatrix2);
        System.out.println(realSum);

        System.out.println("*:");
        ComplexMatrix realProduct = realMatrix1.multiply(realMatrix2);
        System.out.println(realProduct);

        System.out.println("Transpose Matrix 1:");
        ComplexMatrix realTranspose = realMatrix1.transpose();
        System.out.println(realTranspose);

        System.out.println("Determinant of Matrix 1:");
        System.out.println(realMatrix1.determinant());

        System.out.println("Determinant of Matrix 2:");
        System.out.println(realMatrix2.determinant());

    }
}

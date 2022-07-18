package processor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing two-dimensional matrices of real values
 */
public class Matrix {
    private final double[] values;

    private final int rows;
    private final int columns;

    private Double determinant = null;
    private final boolean isSquare;
    private final boolean isVector;

    public Matrix(List<Double> values, int rows, int columns) {
        this(values.stream().mapToDouble(n -> n).toArray(), rows, columns);
    }

    public Matrix(double[] values, int rows, int columns) {
        if (values.length != rows * columns) {
            //throw new IllegalArgumentException("Matrix dimensions do not correspond to the values given");
            System.out.println("ERROR");
            System.out.println(values.length + " != " + rows + " * " + columns);
        }

        this.values = values;
        this.rows = rows;
        this.columns = columns;

        isSquare = this.rows == this.columns;
        isVector = this.rows == 1 || this.columns == 1;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("\n");

        for (int i = 0; i < rows; i++) {
            StringBuilder innerValues = new StringBuilder();
            for (int j = 0; j < columns; j++) {
                innerValues.append(values[j + i * columns]).append(" ");
            }
            innerValues.replace(innerValues.length() - 1, innerValues.length(), "");
            output.append(innerValues).append("\n");
        }

        output.replace(output.length() - 1, output.length(), "\n");
        return output.toString();
    }

    public double get(int row, int column) {
        return values[row * columns + column];
    }

    public Matrix add(Matrix matrix) {
        if (!Arrays.equals(this.getDimensions(), matrix.getDimensions())) {
            //throw new IllegalArgumentException("Dimensions of matrices do not match");
            System.out.println("ERROR");
            return null;
        }

        double[] addedValues = Arrays.copyOf(this.values, values.length);
        for (int i = 0; i < addedValues.length; i++) {
            addedValues[i] = addedValues[i] + matrix.values[i];
        }

        return new Matrix(addedValues, rows, columns);
    }

    public int[] getDimensions() {
        return new int[] {rows, columns};
    }

    public Matrix multiply(double scalar) {
        List<Double> multiplied = Arrays.stream(this.values).boxed().collect(Collectors.toList());
        multiplied.replaceAll(integer -> integer * scalar);
        return new Matrix(multiplied, rows, columns);
    }

    public Matrix multiply(Matrix matrix) {
        if (columns != matrix.rows) {
            return null;
        }

        double[] newValues = new double[this.rows * matrix.columns];

        for (int i = 0; i < newValues.length; i++) {
            int rowNumber = i / matrix.columns;
            int columnNumber = i % matrix.columns;

            double value = 0;
            for (int j = 0; j < columns; j++) {
                double partialRowValue = values[rowNumber * columns + j];
                double partialColumnValue = matrix.values[j * matrix.columns + columnNumber];
                value += partialRowValue * partialColumnValue;
            }

            newValues[i] = value;
        }

        return new Matrix(newValues, this.rows, matrix.columns);
    }

    public Matrix transpose() {
        double[] newValues = Arrays.copyOf(values, values.length);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newValues[j * rows + i] = values[i * columns + j];
            }
        }

        return new Matrix(newValues, columns, rows);
    }

    public Matrix sideTranspose() {
        return verticalLineTranspose().transpose().verticalLineTranspose();
    }

    public Matrix verticalLineTranspose() {
        double[] newValues = new double[values.length];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newValues[i * columns + j] = values[i * columns + columns - j - 1];
            }
        }

        return new Matrix(newValues, rows, columns);
    }

    public Matrix horizontalLineTranspose() {
        double[] newValues = new double[values.length];

        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                newValues[i * columns + j] = values[(rows - 1 - i) * columns + j];
            }
        }

        return new Matrix(newValues, rows, columns);
    }

    private double calculateDeterminant() {
        if (this.rows == 1 && this.columns == 1) {
            return this.get(0, 0);
        } else if (this.rows == 2 && this.columns == 2) {
            return this.get(0, 0) * this.get(1, 1) - this.get(0, 1) * this.get(1, 0);
        }


        double determinant = 0;
        for (int i = 0; i < this.columns; i++) {
            determinant += this.values[i] * getCofactor(0, i);
        }

        return determinant;
    }

    private double getCofactor(int row, int column) {
        return Math.pow(-1, row + column) * this.getMinor(row, column).calculateDeterminant();
    }

    private Matrix getMinor(int row, int column) {
        double[] minorValues = new double[(this.rows - 1) * (this.columns - 1)];

        int p = 0;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (i != row && j != column) {
                    minorValues[p] = this.get(i, j);
                    p++;
                }
            }
        }

        return new Matrix(minorValues, this.rows - 1, this.columns - 1);
    }

    public double getDeterminant() {
        if (rows != columns) {
            throw new RuntimeException("Non square matrix does not have a determinant");
        }
        if (determinant == null) {
            determinant = this.calculateDeterminant();
        }

        return determinant;
    }

    public Matrix getInverse() {
        double determinant = getDeterminant();

        if (determinant == 0) {
            return null;
        }

        return getCofactorMatrix().transpose().multiply(1 / determinant);
    }

    private Matrix getCofactorMatrix() {
        double[] cofactors = new double[values.length];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cofactors[i * columns + j] = getCofactor(i, j);
            }
        }

        return new Matrix(cofactors, rows, columns);
    }
}
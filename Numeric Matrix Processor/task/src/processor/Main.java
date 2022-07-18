package processor;


import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
    public static void main(String[] args) {
        boolean isDone = false;
        do {
            System.out.println("1. Add matrices\n" +
                            "2. Multiply matrix by a constant\n" +
                            "3. Multiply matrices\n" +
                            "4. Transpose matrix\n" +
                            "5. Calculate a determinant\n" +
                            "6. Inverse matrix\n" +
                            "0. Exit\n" +
                            "Your choice:");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    matrixAdditionDialog();
                    break;
                case 2:
                    matrixScalarMultiplicationDialog();
                    break;
                case 3:
                    matrixMultiplicationDialog();
                    break;
                case 4:
                    matrixTranspositionDialog();
                    break;
                case 5:
                    matrixDeterminantDialog();
                    break;
                case 6:
                    inverseMatrixDialog();
                    break;
                case 0:
                    isDone = true;
                    break;
            }

        } while (!isDone);
    }

    private static void inverseMatrixDialog() {
        System.out.println("Enter the size of the matrix:");
        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the matrix:");
        Matrix m = getMatrix(dimensions);
        Matrix inverse = m.getInverse();
        if (inverse == null) {
            System.out.println("This matrix doesn't have an inverse.");
        } else {
            System.out.println(inverse);
        }
    }

    private static void matrixDeterminantDialog() {
        System.out.println("Enter the size of the matrix:");
        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the matrix:");
        Matrix m = getMatrix(dimensions);
        System.out.println(m.getDeterminant());
    }

    private static void matrixTranspositionDialog() {
        int choice;
        do {
            System.out.println("1. Main diagonal\n" +
                    "2. Side diagonal\n" +
                    "3. Vertical line\n" +
                    "4. Horizontal line\n" +
                    "Your choice:");

            choice = Integer.parseInt(scanner.nextLine());

        } while (choice > 4);
        System.out.println("Enter the size of the matrix:");
        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the matrix:");
        Matrix m = getMatrix(dimensions);

        switch (choice) {
            case 1:
                System.out.println(m.transpose());
                break;
            case 2:
                System.out.println(m.sideTranspose());
                break;
            case 3:
                System.out.println(m.verticalLineTranspose());
                break;
            case 4:
                System.out.println(m.horizontalLineTranspose());
                break;
        }

    }

    private static void matrixAdditionDialog() {
        System.out.println("Enter the size of the first matrix:");
        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the first matrix:");
        Matrix m1 = getMatrix(dimensions);

        System.out.println("Enter the size of the second matrix:");
        dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the second matrix:");
        Matrix m2 = getMatrix(dimensions);

        System.out.println("The result is:");
        System.out.println(m1.add(m2));
    }

    private static void matrixScalarMultiplicationDialog() {
        System.out.println("Enter the size of the matrix:");
        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the matrix:");
        Matrix m = getMatrix(dimensions);

        System.out.println("Enter the constant:");
        double constant = Double.parseDouble(scanner.nextLine());

        System.out.println("The result is:");
        System.out.println(m.multiply(constant));
    }

    private static void matrixMultiplicationDialog() {
        System.out.println("Enter the size of the first matrix:");
        int[] dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the first matrix:");
        Matrix m1 = getMatrix(dimensions);

        System.out.println("Enter the size of the second matrix:");
        dimensions = Arrays.stream(scanner.nextLine().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        System.out.println("Enter the second matrix:");
        Matrix m2 = getMatrix(dimensions);

        System.out.println("The result is:");
        System.out.println(m1.multiply(m2));
    }

    private static Matrix getMatrix(int[] dimensions) {
        double[] values = new double[dimensions[0] * dimensions[1]];
        for (int i = 0; i < dimensions[0]; i++) {
            double[] row = Arrays.stream(scanner.nextLine().split("\\s+")).mapToDouble(Double::parseDouble).toArray();
            for (int j = 0; j < dimensions[1]; j++) {
                values[i * dimensions[1] + j] = row[j];
            }
        }

        return new Matrix(Arrays.stream(values).boxed().collect(Collectors.toList()), dimensions[0], dimensions[1]);
    }
}

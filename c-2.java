import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class ArithmeticExpressionSolver {

    public static void main(String[] args) throws IOException {
        // Change these filenames to match your input and output file names
        String inputFileName = "input.txt";
        String outputFileName = "output.txt";
        
        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
        FileWriter writer = new FileWriter(outputFileName);
        
        String line;
        while ((line = reader.readLine()) != null) {
            String result = solveExpression(line);
            writer.write(line + " " + result + "\n");
        }
        
        reader.close();
        writer.close();
    }
    
    public static String solveExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                number.append(c);
                
                while (i < expression.length() - 1 && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                    number.append(expression.charAt(i + 1));
                    i++;
                }
                
                numbers.push(Double.parseDouble(number.toString()));
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    double num2 = numbers.pop();
                    double num1 = numbers.pop();
                    char op = operators.pop();
                    numbers.push(applyOperator(num1, num2, op));
                }
                
                operators.pop();
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                    double num2 = numbers.pop();
                    double num1 = numbers.pop();
                    char op = operators.pop();
                    numbers.push(applyOperator(num1, num2, op));
                }
                
                operators.push(c);
            }
        }
        
        while (!operators.isEmpty()) {
            double num2 = numbers.pop();
            double num1 = numbers.pop();
            char op = operators.pop();
            numbers.push(applyOperator(num1, num2, op));
        }
        
        return Double.toString(numbers.pop());
    }
    
    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
    
    public static int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }
    
    public static double applyOperator(double num1, double num2, char op) {
        switch (op) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            case '^':
                return Math.pow(num1, num2);
            default:
                return 0;
        }
    }
}

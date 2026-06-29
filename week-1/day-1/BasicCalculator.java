import java.util.Scanner;

public class BasicCalculator{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first number : ");
        int a = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter second number : ");
        int b = sc.nextInt();
        sc.nextLine();
        System.out.print("Choose opeartor (+, -, *, /, %) : ");
        char op = sc.next().charAt(0);

        switch(op){
        case '+': 
            System.out.println("Result : "+(a+b));
            break;
        case '-':
            System.out.println("Result : "+(a-b));
            break;
        case '*':
            System.out.println("Result : "+(a*b));
            break;
        case '/':
            if(b == 0){
                System.out.println("Denominator cannot be zero.");
            }
            else{
                float res = (float)a/(float)b;
                System.out.println("Result : "+res);
            }
            break;
        case '%':
            System.out.println("Result : "+(a%b));
            break;
        default:
            System.out.println("Invalid input.");
        }

        sc.close();
    }
}
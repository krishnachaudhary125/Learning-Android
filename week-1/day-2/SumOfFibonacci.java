import java.util.Scanner;

public class SumOfFibonacci {

    static int sum(int n){
        int a = 0;
        int b = 0;
        int sum = 1;

        if(n<=0)
            return 0;

        int current = 1;
        for(int i = 2; i<=n; i++){
            a = b;
            b = current;
            current = a + b;
            sum += current;
        }
        return sum;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the length of fibonacci seies : ");
        int n = sc.nextInt();
        System.out.println("Sum of size of "+n+" fibonacci series : "+sum(n));
    }
}
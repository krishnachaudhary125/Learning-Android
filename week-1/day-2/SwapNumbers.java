public class SwapNumbers{
    public static void main(String[] args) {
        int numOne = 5;
        int numTwo = 10;

        int temp;

        temp = numOne;
        numOne = numTwo;
        numTwo = temp;

        System.out.println("numOne : "+numOne+"\nnumTwo : "+numTwo);
    }
}
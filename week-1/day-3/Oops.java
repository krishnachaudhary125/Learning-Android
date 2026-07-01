import java.util.Scanner;

public class Shape{
    private double area;
    public Shape(double area){
        this.area = area;
    }
    public double getArea(){
        return area;
    }
    public void setArea(double area){
        this.area = area;
    }
    public void calculateArea(){
        System.out.print("Area of the shape : "+getArea()+"\n");
    }
}
public class Rectangle extends Shape{
    private double length;
    private double breadth;

    public Rectangle(double length, double breadth){
        super(length * breadth);
        this.length = length;
        this.breadth = breadth;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getBreadth() {
        return breadth;
    }

    public void setBreadth(double breadth) {
        this.breadth = breadth;
    }
}
public class Circle extends Shape{
    private double radius;

    public Circle(double radius){
        super(3.14*radius*radius);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
public class Oops{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter length of rectangle : ");
        double len = sc.nextDouble();
        System.out.print("Enter breadth of rectangle : ");
        double bre = sc.nextDouble();
        Shape s1 = new Rectangle(len, bre);
        System.out.print("Enter radius of Circle : ");
        double r = sc.nextDouble();
        Shape s2 = new Circle(r);
        s1.calculateArea();
        s2.calculateArea();
        sc.close();
    }
}
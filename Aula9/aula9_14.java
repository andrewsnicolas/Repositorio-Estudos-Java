package Aulas.Aula9;
import java.util.InputMismatchException;
import java.util.Scanner;
class CustomError extends Exception{
    public CustomError(String string){
        super(string);
    }
}
public class aula9_14 {
    static Scanner reader = new Scanner(System.in);
    public static void main(String args[]) throws CustomError {
        Integer num1, num2;
        num1 = entrarDados();
        num2 = entrarDados();
        Integer res;
        boolean checker = false;
        try{
            res = num1/num2;
            float res2 = (float) num1/num2;
            if(num1 == 0 && num2 == 1) throw new CustomError("O primeiro numero não pode ser igual a 0 e o outro igual a 1");
            checker = (num1 != 0 && res == 0)? true : false;
            System.out.println("O resultado da divisão dos números que você digitou é: "+(checker? res2: res));
        } catch(ArithmeticException e){
            System.out.println("Divisão por 0 não é possível");
        } catch(CustomError e){
            System.out.println("Exceção especial ocorrida: "+ e);
        }
        reader.close();
    }
    private static Integer entrarDados(){
        Integer num = null;
        boolean valido = false;
        while(!valido){
            try{
                System.out.println("Digite um número inteiro: ");
                num = reader.nextInt();
                valido = true;
            } catch (InputMismatchException e){
                System.out.println("Entrada de dados inválida!");
                reader.nextLine();
            }
        }
        return num;
    }
}

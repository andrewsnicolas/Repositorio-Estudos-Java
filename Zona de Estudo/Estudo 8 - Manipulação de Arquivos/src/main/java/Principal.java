import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class Principal {
    public static void main(String args[]){
        Scanner reader = new Scanner(System.in);
        boolean errou = false;
        int escolha = 0;
        do{
        do{
            if(errou) System.out.println("\033[1;31mVALOR DIGITADO INVÁLIDO\nPOR FAVOR, DIGITE OUTRO NÚMERO\033[0m");
            System.out.println("\nO que você quer fazer?\n" +
                    "1)Criar um aluno\n" +
                    "2)Ver as notas de um aluno\n" +
                    "3)Criar um arquivo de alunos aprovados\n" +
                    "4)Exibir as médias dos alunos\n" +
                    "5)Gerar um arquivo das médias dos alunos\n" +
                    "6)Fazer backup\n" +
                    "7)Sair");
            escolha = reader.nextInt();
            if(escolha<1 || escolha>7) errou = true;
            else errou = false;
        } while(escolha < 1 || escolha>7);
        if(escolha==7) break;
        switch(escolha) {
            case 1:
                try {
                    ClasseFuncoes.criarAluno();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                try {
                    ClasseFuncoes.verificarNota();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 3:
                try{
                    ClasseFuncoes.criarArquivoAprovados();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 4:
                try{
                    ClasseFuncoes.mostrarMediaAlunos();
                } catch(Exception e){
                    System.out.println(e);
                }
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                break;
        }
        }while(true);
        reader.close();
    }
}

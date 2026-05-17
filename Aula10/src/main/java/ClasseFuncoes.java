import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.lang.Character;
public class ClasseFuncoes {
    static Scanner reader = new Scanner(System.in);
    static String caminhoAtual = System.getProperty("user.dir") + "\\Aulas\\Aula10\\notasAlunos.txt";
    static File arquivo = new File(caminhoAtual);
    public static void criarAluno() throws IOException {
        boolean errou = false;
        char entrada;
        String nome = "";
        while(nome == null){
            if(errou) System.out.println("\033[1;31mNOME INVÁLIDO! \n" +
                    "POR FAVOR, INSIRA UM NOME COM PELO MENOS UMA LETRA\033[0m");
            System.out.println("\nDigite o nome do aluno que deseja criar:\n ");
            nome = reader.nextLine();
            errou = true;
        }
        errou = false;
        entrada = 'f';
        while (Character.toLowerCase(entrada) != 's'){
            if(errou) System.out.println("\033[1;31mVALOR INVÁLIDO! \n" +
                    "POR FAVOR, INSIRA UM VALOR DE ENTRADA VÁLIDO\033[0m");
            System.out.printf("Você tem certeza que deseja adicionar esse (%s) nome?" +
                    "\nDigite 's' para sim\nDigite 'n' para não\n", nome);
            entrada = reader.next().charAt(0);
            errou = true;
            if(Character.toLowerCase(entrada) == 'n') criarAluno();
        }
        float[] notas = new float[4];
        for(int i = 0; i<4; i++){
            errou = false;
            notas[i] = -1;
            while(notas[i] < 0 && notas[i]>10){
                if(errou) System.out.println("\033[1;31mVALOR INVÁLIDO! \n" +
                        "POR FAVOR, INSIRA UM VALOR DE ENTRADA VÁLIDO\033[0m");
                System.out.printf("\nDigite a %d° nota do aluno %s:  ", (i+1), nome);
                notas[i] = reader.nextFloat();
                errou = true;
            }
        }
        try(FileWriter escritor = new FileWriter(caminhoAtual))
        {
            String conteudo = nome + ";" + notas[0] + ";" + notas[1] + ";" + notas[2] + ";" + notas[3];
            escritor.write(conteudo);
            System.out.println("Aluno adicionado com sucesso!");
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    public static void procurarAluno() throws FileNotFoundException {
        boolean opcaoInvalida = false;
        boolean opcaoInvalidaSN;
        char continuarProcurando;
        String linha = "", nome;
        int linhaAtual = 0;
        int localizacao = -1;
        Scanner readerArq = new Scanner(arquivo);
        while(localizacao < 0){
            continuarProcurando = 'a';
            opcaoInvalidaSN = false;
            if(opcaoInvalida) {
                while(Character.toLowerCase(continuarProcurando) != 'n' && Character.toLowerCase(continuarProcurando) != 's'){
                    if(opcaoInvalidaSN) System.out.println("\033[1;31mLETRA INSERIDA INVÁLIDA\n" +
                                    "DIGITE 'S' PARA SIM E 'N' PARA NÃO\033[0m");
                    System.out.println("O nome que você digitou não foi encontrado\n" +
                            "Deseja continuar procurando? [S/N]");
                    continuarProcurando = reader.next().charAt(0);
                    opcaoInvalidaSN = true;
                }
            }
            System.out.println("Digite o nome do aluno que está procurando: ");
            nome = reader.nextLine();
            while(readerArq.hasNext()){
                linha = readerArq.nextLine();
                if(!linha.contains(nome)) linhaAtual++;
                else {
                    System.out.println("Nome encontrado!");
                    localizacao = linhaAtual;
                    break;
                }
            }
            if(localizacao>=0) break;
            opcaoInvalida = true;
        }
        if(localizacao>=0){
            String[] aluno = linha.split(";");
            System.out.printf(
                    "O nome completo do aluno é: %s\n" +
                    "1° nota: %s\n" +
                    "2° nota: %s\n" +
                    "3° nota: %s\n" +
                    "4° nota: %s", aluno[0], aluno[1], aluno[2], aluno[3], aluno[4]);
        }
    }
}

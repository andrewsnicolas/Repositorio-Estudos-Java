package Aulas.Aula8.Listas.Exercicios;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
public class Ex56 {
    public static void main(String args[]){
        Scanner reader = new Scanner(System.in);
        int quantidadeLinhas = reader.nextInt();
        reader.nextLine();
        String[] entrada = new String[2];
        ArrayList<String> nomes = new ArrayList<String>();
        ArrayList<Integer> pontuacao = new ArrayList<Integer>();
        for(int i = 0; i<quantidadeLinhas; i++){
            entrada = reader.nextLine().split(" ");
            nomes.add(String.valueOf(entrada[0]));
            pontuacao.add(Integer.valueOf(entrada[1]));
        }

        for(int i = 0; i<quantidadeLinhas; i++) {
            for (int j = 0; j < quantidadeLinhas - 1; j++) {
                if (i == j) continue;
                if (pontuacao.get(i) > pontuacao.get(j)) {
                    entrada[0] = nomes.get(j);
                    nomes.set(j, nomes.get(i));
                    nomes.set(i, entrada[0]);
                    entrada[1] = String.valueOf(pontuacao.get(j));
                    pontuacao.set(j, pontuacao.get(i));
                    pontuacao.set(i, Integer.valueOf(entrada[1]));
                }
            }
        }
        for(int i = 0; i<quantidadeLinhas; i++){
            System.out.printf("\n %d %s %d",i+1, nomes.get(i), pontuacao.get(i));
            }
        }
}

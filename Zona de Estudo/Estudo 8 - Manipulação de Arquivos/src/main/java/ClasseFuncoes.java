import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.HexFormat;
import java.util.ArrayList;

import java.lang.Character;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
public class ClasseFuncoes {
    //Variáveis globais
    static Scanner reader = new Scanner(System.in);
    static String caminhoAtual = System.getProperty("user.dir") + "\\Zona de Estudo\\Estudo 8 - Manipulação de Arquivos";
    static File arquivo = new File(caminhoAtual+"\\notasAlunos.txt");


    //OPÇÃO 1 - Criar um aluno
    public static void criarAluno() throws IOException {
        boolean errou = false;
        char entrada;
        String nome = "";

        //Verificador de se o usuário não digitou nada
        while(nome == null){
            if(errou) System.out.println("\033[1;31mNOME INVÁLIDO! \n" +
                    "POR FAVOR, INSIRA UM NOME COM PELO MENOS UMA LETRA\033[0m");
            System.out.println("\nDigite o nome do aluno que deseja criar:\n ");
            nome = reader.nextLine();
            errou = true;
        }

        errou = false;
        entrada = 'f';

        //Verifica se o usuário quer mesmo esse nome
        while (Character.toLowerCase(entrada) != 's'){
            if(errou) System.out.println("\033[1;31mVALOR INVÁLIDO! \n" +
                    "POR FAVOR, INSIRA UM VALOR DE ENTRADA VÁLIDO\033[0m");
            System.out.printf("Você tem certeza que deseja adicionar esse (%s) nome?" +
                    "\nDigite 's' para sim\nDigite 'n' para não\n", nome);
            entrada = reader.next().charAt(0);
            errou = true; //É usada para dizer se o usuário digitou algo errado

            //Se ele não quiser, volta toda a função para ele digitar o nome
            if(Character.toLowerCase(entrada) == 'n') criarAluno();
        }

        float[] notas = new float[4];

        //Pede para o usuário digitar as notas
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

        //Escreve no arquivo o nome e as notas
        try(FileWriter escritor = new FileWriter(caminhoAtual))
        {
            String conteudo = nome + ";" + notas[0] + ";" + notas[1] + ";" + notas[2] + ";" + notas[3];
            escritor.write(conteudo);
            System.out.println("Aluno adicionado com sucesso!");
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    //Opção 3: Verificar notas de alunos
    public static void verificarNota() throws FileNotFoundException {
        //Opções de alunos que podem aparecer após o usuário digitar os nomes
        Map<Integer, String> alunosPossiveis = new HashMap<Integer, String>();

        /*
        Opção Inválida é para caso o usuário tenha digitado o nome errado
        Opção Inválida SN serve para verificar se o usuário digitou S ou N
         */
        boolean opcaoInvalida = false, opcaoInvalidaSN, opcaoInvalidaIndx;

        //Variável que recebe S ou N - verifica se o usuário quer continuar procurando o aluno
        char continuarProcurando;
        /*
        nome - recebe o nome digitado pelo usuário
        linha - recebe o valor de cada linha do arquivo
        */
        String linha = "", nome;

        //Aluno - armazena os dados de cada aluno
        String[] aluno = new String[5];

        /*
        contLinhas - contador para a linha que o usuário está
        quantAlunPossiv - alunos que aparecem no filtro após digitar o nome
        opcaoEscol - Qual é o nome que ele quer as notas
        */
        int contLinhas = 0, quantAlunPossiv, opcaoEscol = -1;

        reader.nextLine(); //limpa o reader
        while(opcaoEscol < 0){
            alunosPossiveis.clear(); //limpa o vetor
            continuarProcurando = 'a'; //garante que o loop seja feito
            opcaoInvalidaSN = false;
            if(opcaoInvalida) {
                while(true){
                    if(opcaoInvalidaSN) System.out.println("\033[1;31mLETRA INSERIDA INVÁLIDA\n" +
                                    "DIGITE 'S' PARA SIM E 'N' PARA NÃO\033[0m");

                    System.out.println("O nome que você digitou não foi encontrado\n" +
                            "Deseja continuar procurando? [S/N]");
                    continuarProcurando = reader.next().charAt(0);


                    if(Character.toLowerCase(continuarProcurando) == 'n' ||
                            Character.toLowerCase(continuarProcurando) == 's') break;
                    opcaoInvalidaSN = true; //Se o loop acontecer, ele mostrará que a opção digitada foi inválida
                }
            }
            if(Character.toLowerCase(continuarProcurando) == 's') {
                reader.nextLine(); //limpa o readline
                opcaoInvalida = false;
            }
            if(Character.toLowerCase(continuarProcurando) == 'n') {
                System.out.println("\nVocê decidiu encerrar a busca");
                break;
            }

            System.out.println("Digite o nome do aluno que está procurando: ");
            nome = reader.nextLine();

            Scanner readerArq = new Scanner(arquivo);
            contLinhas = 0;

            //Lê a linha e confirma se o nome é igual ou possui caracteres semelhantes ao que o usuário digitou
            while(readerArq.hasNext()){
                linha = readerArq.nextLine();
                if(linha.contains(nome)) {
                    aluno = linha.split(";");

                    /*
                    Crie uma lista de nomes que podem ser possiveis por possuir a mesma cadeia de texto
                    que o usuário digitou
                    */
                    alunosPossiveis.put(contLinhas, aluno[0]);
                }
                contLinhas++;
            }

            quantAlunPossiv = alunosPossiveis.size();

            //Se não houver nenhum usuario semelhante, o nome digitado é inválido
            if(quantAlunPossiv==0) opcaoInvalida = true;


            else if(quantAlunPossiv<=8){
                System.out.println("Quais desses alunos você quer saber as informações? (Escolha pelo índice)");
                for(Integer i : alunosPossiveis.keySet()){
                    System.out.printf("\n Índice - %d   Nome: %s", i, alunosPossiveis.get(i));
                }
                opcaoInvalidaIndx = false;
                do{
                    if(opcaoInvalidaIndx)
                        System.out.println("\nDIGITE UM VALOR DENTRO DO INTERVALO");

                    System.out.println("\nDigite o índice que você deseja: ");
                    opcaoEscol = reader.nextInt();

                    opcaoInvalidaIndx = true; //Caso o usuário digite valor inválido
                } while(alunosPossiveis.get(opcaoEscol) == null);

                //Caso o usuário definitivamente escolha qual aluno ele quer
                aluno = linha.split(";");
            }

            //A fim de que o usuário não pegue muitas informações ou o nome fique muito longe
            else
                System.out.println("NÃO FOI POSSÍVEL LOCALIZAR (HÁ MUITOS NOMES)\nREESCREVA E DÊ MAIS DETALHE DOS NOMES");

            //Termina a função de ler o arquivo (ou seja, volta para a primeira linha quando for reaberto)
            readerArq.close();

        }
        if(opcaoEscol >=0){

            System.out.printf(
                    "O nome completo do aluno é: %s\n" +
                    "1° nota: %s\n" +
                    "2° nota: %s\n" +
                    "3° nota: %s\n" +
                    "4° nota: %s", aluno[0], aluno[1], aluno[2], aluno[3], aluno[4]);
        }
    }
    public static float calcularMediaAluno(String aluno[]){
        int tamanhoVetor = aluno.length;
        float media = 0;
        for(int i = 1; i<tamanhoVetor; i++){
            media += Float.valueOf(aluno[i]);
        }
        media/=4;
        return media;
    }
    public static void escreverAlunosAprovados(String linha, FileWriter escritor) throws FileNotFoundException{
        linha = linha.replace(",",".");
        String[] aluno = linha.split(";");
        float media = calcularMediaAluno(aluno);
        try{

            if(media>6) escritor.write(aluno[0]+"\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void criarArquivoAprovados() throws FileNotFoundException{
        String linha = "";
        try{
            //Lê o arquivo original e prepara o arquivo de aprovados para ser escrito
            Scanner readerArq = new Scanner(arquivo);
            FileWriter escritorArqAprovados = new FileWriter(caminhoAtual+"alunoAprovados.txt");

            //
            while(readerArq.hasNextLine()){
                linha = readerArq.nextLine();
                escreverAlunosAprovados(linha, escritorArqAprovados); //Trata os dados do aluno naquela linha
            }

            //Fecha o arquivo de aprovados - por ter terminado a tarefa
            escritorArqAprovados.close();
            readerArq.close();
            //Se tudo ocorrer certo, essa mensagem será acionada
            System.out.println("Arquivo criado com sucesso!");

            //Caso dê errado
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void mediaAlunos(String funcao){
        try{

            //Declarações de variáveis
            Scanner reader = new Scanner(arquivo); //Reader para ler o arquivo que foi declarado na linha 13
            String[] aluno = new String[4]; //Recebe cada item do aluno
            float media = 0;
            String linha = ""; //Recebe cada linha do arquivo

            System.out.println("\n");
            while(reader.hasNextLine()){
                linha = reader.nextLine();

                linha = linha.replace(",","."); 
                //Há a troca de vírgula para ponto para o computador converter 

                aluno = linha.split(";");
                media = calcularMediaAluno(aluno);
                if(funcao=="escrever") escrever(caminhoAtual+"\\mediaAlunos.txt","Média: "+ media +" - Nome: "+aluno[0]+" \n");
                else System.out.printf("Média: %.2f - Nome: %s \n", media, aluno[0]);
            }
            reader.close();
            System.out.println("\n");
        } catch(Exception e){
            System.out.println(e);
        }
        
    }
    public static void escrever(String caminho, String conteudo){
        try{
            FileWriter escritor = new FileWriter(caminho);
            escritor.write(conteudo);
            escritor.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public static void verificarExistenciaArquivo(){
        System.out.println("Digite o nome do arquivo que deseja fazer o backup manual: ");
        String nomeArquivo = reader.next();
        String pathArquivOrig = caminhoAtual+"\\"+nomeArquivo;
        File arquivoSelecionado = new File(pathArquivOrig);
        if(!arquivoSelecionado.exists() || arquivoSelecionado.isDirectory())
            System.out.println("ERRO\nO ARQUIVO NÃO EXISTE");
        else
        {
            //System.out.println("O arquivo existe");
            String caminhoPastaBackups = caminhoAtual+"\\Backups";
            File pastaBackups = new File(caminhoPastaBackups);
            if(!pastaBackups.exists())
                pastaBackups.mkdir();
                String nomeArquivoBackup = definirNomeBackup(nomeArquivo);
            try{
                //Cria o arquivo backup
                String pathArquivCop = caminhoPastaBackups+"\\"+nomeArquivoBackup;
                File arquivoCopiado = new File(pathArquivCop);
                Files.copy(arquivoSelecionado.toPath(), arquivoCopiado.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                //Verifica a veracidade do conteúdo
                String codigoSHArquivoOriginal = validadorBackup(pathArquivOrig);
                if(codigoSHArquivoOriginal == "Falha") {
                    System.out.println("Um erro ocorreu ao converter o código do arquivo original para SHA-256");
                    return;
                }
                String codigoSHABackup = validadorBackup(pathArquivCop);
                if(codigoSHABackup == "Falha") {
                    System.out.println("Um erro ocorreu ao converter o código do arquivo copiado para SHA-256 ");
                    return;
                }
                if(codigoSHABackup.intern() == codigoSHArquivoOriginal.intern()) 
                    System.out.println("A integridade dos dados foi mantida durante o backup\nBackup executado com sucesso!");
                else
                {
                    System.out.println("A integridade dos dados foi comprometida\nArquivo backup apagado");
                }
                ArrayList<String> infoLog = new ArrayList();
                UUID idUUID = UUID.randomUUID();
                String idString = idUUID+"";
                infoLog.add(idString);
                Date dataHoraAtual = new Date();
                String data = new SimpleDateFormat("ddMMyyyy_HHmmss").format(dataHoraAtual);
                infoLog.add(data);
                infoLog.add(nomeArquivo);
                infoLog.add(pathArquivOrig);
                infoLog.add(pathArquivCop);
                
                infoLog.add(retornarTamanhoArquivo(arquivoSelecionado));
                infoLog.add(retornarTamanhoArquivo(arquivoCopiado));
                infoLog.add(codigoSHArquivoOriginal);
                infoLog.add(codigoSHABackup);
                compactarBackup(pathArquivCop, nomeArquivoBackup, caminhoPastaBackups);
                escreverLog(infoLog, caminhoPastaBackups);
               } catch(Exception e){
                System.out.println(e);
            }
        }
    }
    public static String validadorBackup(String pathArquiv){
        try {
            byte[] conteudoArquivoBytes = Files.readAllBytes(Paths.get(pathArquiv));
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(conteudoArquivoBytes);
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Falha";
    }
    public static String definirNomeBackup(String nomeArquivoOriginal)
    {
        int indice = nomeArquivoOriginal.indexOf(".");
        String nomeArquivoSemExt = nomeArquivoOriginal.substring(0,indice);
        Date dataHoraAtual = new Date();
        String data = new SimpleDateFormat("ddMMyyyy_HHmmss").format(dataHoraAtual);
        nomeArquivoSemExt += "_"+data;
        String extensao = nomeArquivoOriginal.substring(indice);
        String nomeArquivoCompleto = nomeArquivoSemExt+extensao;
        return nomeArquivoCompleto;
    }
    public static String retornarTamanhoArquivo(File arquivo){
        int cont = 0;
                float tamanhoArquivo  = arquivo.length();
                while(tamanhoArquivo/1000>=1){
                    tamanhoArquivo = tamanhoArquivo/1000;
                    cont++;
                }
                String unidade = "bytes";
                switch(cont){
                    case 1:
                        unidade = "Kb";
                        break;
                    case 2:
                        unidade = "Mb";
                        break;
                    case 3:
                        unidade = "Gb";
                        break;
                }
        return (tamanhoArquivo+" "+unidade);
    }
    public static void compactarBackup(String arquivo, String nomeBackup, String pathPasta){
        try{
            int indice = nomeBackup.indexOf(".");

            String nomeArquivoSemExt = nomeBackup.substring(0,indice);

            FileOutputStream fos = new FileOutputStream(pathPasta+"\\"+nomeArquivoSemExt+".zip");
            //Cria o arquivo zip vazio

            ZipOutputStream zip = new ZipOutputStream(fos);
            //Transforma ele em Zip

            ZipEntry zipEntry = new ZipEntry(arquivo);
            //Cria a instância que vai ler os dados de tal arquivo

            zip.putNextEntry(zipEntry);
            //Prepara a cópia das informações do arquivo

            FileInputStream fis = new FileInputStream(pathPasta+"\\"+nomeArquivoSemExt+".zip");
            //Abre o arquivo que irá ser copiado

            byte[] buffer = new byte[2048];
            //Escolha estratégica pela média do tamanho dos arquivos

            int byteLidos; 

            while((byteLidos = fis.read(buffer)) > 0){ 
            //Lê os bytes do arquivo e verifica se há bytes para serem lidos

            zip.write(buffer, 0, byteLidos); 
            //Escreve os bytes da posição 0 até o final do arquivo
            }
            zip.closeEntry();
            fis.close();
            zip.close();
            System.out.println("Arquivo Zipado com sucesso");
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public static void escreverLog(ArrayList<String> informacoes, String pathPastaBackup){
        String pathArquivoLog = pathPastaBackup+"\\log.txt";
        try{
            FileWriter logWriter = new FileWriter(pathArquivoLog, true);
            logWriter.write("\n================================================================\n");
            logWriter.write("\n");
            logWriter.write("\nID do Backup:                "+ informacoes.get(0));
            logWriter.write("\nData/Hora:                   "+ informacoes.get(1));
            logWriter.write("\nNome Usuário:                "+ System.getProperty("user.name"));
            logWriter.write("\nArquivo Original:            "+ informacoes.get(2));
            logWriter.write("\nLocal arquivo original:      "+ informacoes.get(3));
            logWriter.write("\nLocal Backup:                "+ informacoes.get(4));
            logWriter.write("\n");
            logWriter.write("\nTamanho do arquivo original: "+ informacoes.get(5));
            logWriter.write("\nTamanho do arquivo copiado:  "+ informacoes.get(6));
            logWriter.write("\nSHA-256 arquivo original:    "+ informacoes.get(7));
            logWriter.write("\nSHA-256 backup:              "+ informacoes.get(8));
            logWriter.write("\n");
            logWriter.write("\nComprimido:              SIM");
            logWriter.write("\nStatus:                  SUCESSO");
            logWriter.write("\n");
            logWriter.write("\n==============================================================\n");
            logWriter.close();
            System.out.println("Log atualizado com sucesso!");
        } catch(Exception e){
            System.out.println(e);
        }
        
        
    }
}

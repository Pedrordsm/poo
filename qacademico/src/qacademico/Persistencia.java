package qacademico;

import java.io.*;
import java.util.ArrayList;

public class Persistencia {
    public static void salvar(Sistema sistema) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dados.txt"))) {
            salvarProfessores(writer, sistema.getProfs());
            salvarAlunos(writer, sistema.getAlunos());
            salvarTurmas(writer, sistema.getTurmas());
            writer.write("FIM");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    private static void salvarProfessores(BufferedWriter writer, ArrayList<Professor> professores) throws IOException {
        for (Professor p : professores) {
            writer.write("PROF");
            writer.newLine();
            writer.write(p.getNome());
            writer.newLine();
            writer.write(p.getCpf());
            writer.newLine();
            writer.write(String.valueOf(p.getSalario()));
            writer.newLine();
        }
    }

    private static void salvarAlunos(BufferedWriter writer, ArrayList<Aluno> alunos) throws IOException {
        for (Aluno a : alunos) {
            writer.write("ALU");
            writer.newLine();
            writer.write(a.getNome());
            writer.newLine();
            writer.write(a.getCpf());
            writer.newLine();
            writer.write(a.getMat());
            writer.newLine();
        }
    }

    private static void salvarTurmas(BufferedWriter writer, ArrayList<Turma> turmas) throws IOException {
        for (Turma t : turmas) {
            writer.write("TUR");
            writer.newLine();
            salvarDadosBasicosTurma(writer, t);
            salvarAlunosTurma(writer, t.getAlunos());
            salvarAvaliacoesTurma(writer, t.getAvs());
        }
    }

    private static void salvarDadosBasicosTurma(BufferedWriter writer, Turma t) throws IOException {
        writer.write(t.getNome());
        writer.newLine();
        writer.write(String.valueOf(t.getAno()));
        writer.newLine();
        writer.write(String.valueOf(t.getSem()));
        writer.newLine();
        writer.write(t.getProf().getCpf());
        writer.newLine();
    }

    private static void salvarAlunosTurma(BufferedWriter writer, ArrayList<Aluno> alunos) throws IOException {
        writer.write(String.valueOf(alunos.size()));
        writer.newLine();
        for (Aluno a : alunos) {
            writer.write(a.getMat());
            writer.newLine();
        }
    }

    private static void salvarAvaliacoesTurma(BufferedWriter writer, ArrayList<Avaliacao> avaliacoes) throws IOException {
        writer.write(String.valueOf(avaliacoes.size()));
        writer.newLine();
        for (Avaliacao av : avaliacoes) {
            if (av instanceof Prova) {
                salvarProva(writer, (Prova) av);
            } else if (av instanceof Trabalho) {
                salvarTrabalho(writer, (Trabalho) av);
            }
        }
    }

    private static void salvarProva(BufferedWriter writer, Prova p) throws IOException {
        writer.write("PROV");
        writer.newLine();
        writer.write(p.getNome());
        writer.newLine();
        writer.write(String.valueOf(p.getDtAplic().getDia()));
        writer.newLine();
        writer.write(String.valueOf(p.getDtAplic().getMes()));
        writer.newLine();
        writer.write(String.valueOf(p.getDtAplic().getAno()));
        writer.newLine();
        writer.write(String.valueOf(p.getValor()));
        writer.newLine();
        writer.write(String.valueOf(p.getnQuestoes()));
        writer.newLine();

        for (AlunoProva ap : p.getNotas()) {
            for (Double nota : ap.getNotas()) {
                writer.write(String.valueOf(nota));
                writer.newLine();
            }
        }
    }

    private static void salvarTrabalho(BufferedWriter writer, Trabalho tr) throws IOException {
        writer.write("TRAB");
        writer.newLine();
        writer.write(tr.getNome());
        writer.newLine();
        writer.write(String.valueOf(tr.getDtAplic().getDia()));
        writer.newLine();
        writer.write(String.valueOf(tr.getDtAplic().getMes()));
        writer.newLine();
        writer.write(String.valueOf(tr.getDtAplic().getAno()));
        writer.newLine();
        writer.write(String.valueOf(tr.getValor()));
        writer.newLine();
        writer.write(String.valueOf(tr.getnIntegrantes()));
        writer.newLine();
        writer.write(String.valueOf(tr.getGrupos().size()));
        writer.newLine();

        for (GrupoTrabalho g : tr.getGrupos()) {
            writer.write(String.valueOf(g.getAlunos().size()));
            writer.newLine();
            for (Aluno a : g.getAlunos()) {
                writer.write(a.getMat());
                writer.newLine();
            }
            writer.write(String.valueOf(g.getNota()));
            writer.newLine();
        }
    }

    public static Sistema carregar() {
        Sistema sistema = new Sistema();
        try (BufferedReader reader = new BufferedReader(new FileReader("dados.txt"))) {
            String linha;
            while ((linha = reader.readLine()) != null && !linha.equals("FIM")) {
                switch (linha) {
                    case "PROF":
                        sistema.novoProf(carregarProfessor(reader));
                        break;
                    case "ALU":
                        sistema.novoAluno(carregarAluno(reader));
                        break;
                    case "TUR":
                        sistema.novaTurma(carregarTurma(reader, sistema));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo de dados não encontrado. Criando novo sistema.");
        }
        return sistema;
    }

    private static Professor carregarProfessor(BufferedReader reader) throws IOException {
        String nome = reader.readLine();
        String cpf = reader.readLine();
        double salario = Double.parseDouble(reader.readLine());
        return new Professor(nome, cpf, salario);
    }

    private static Aluno carregarAluno(BufferedReader reader) throws IOException {
        String nome = reader.readLine();
        String cpf = reader.readLine();
        String mat = reader.readLine();
        return new Aluno(nome, cpf, mat);
    }

    private static Turma carregarTurma(BufferedReader reader, Sistema sistema) throws IOException {
        String nome = reader.readLine();
        int ano = Integer.parseInt(reader.readLine());
        int sem = Integer.parseInt(reader.readLine());

        String cpfProf = reader.readLine();
        Professor prof = sistema.encontrarProf(cpfProf);
        if (prof == null) {
            throw new IOException("Professor não encontrado para a turma " + nome);
        }

        int numAlunos = Integer.parseInt(reader.readLine());
        ArrayList<Aluno> alunosTurma = new ArrayList<>();
        for (int i = 0; i < numAlunos; i++) {
            String matAluno = reader.readLine();
            Aluno aluno = sistema.encontrarAluno(matAluno);
            if (aluno != null) {
                alunosTurma.add(aluno);
            }
        }

        int numAvaliacoes = Integer.parseInt(reader.readLine());
        ArrayList<Avaliacao> avaliacoes = new ArrayList<>();
        for (int i = 0; i < numAvaliacoes; i++) {
            String tipoAval = reader.readLine();
            if (tipoAval.equals("PROV")) {
                avaliacoes.add(carregarProva(reader, sistema, alunosTurma));
            } else if (tipoAval.equals("TRAB")) {
                avaliacoes.add(carregarTrabalho(reader, sistema, alunosTurma));
            }
        }

        return new Turma(nome, ano, sem, prof, alunosTurma, avaliacoes);
    }

    private static Prova carregarProva(BufferedReader reader, Sistema sistema, ArrayList<Aluno> alunosTurma) throws IOException {
        String nome = reader.readLine();
        int dia = Integer.parseInt(reader.readLine());
        int mes = Integer.parseInt(reader.readLine());
        int ano = Integer.parseInt(reader.readLine());
        double valor = Double.parseDouble(reader.readLine());
        int numQuestoes = Integer.parseInt(reader.readLine());

        ArrayList<AlunoProva> alunosProvas = new ArrayList<>();
        for (Aluno aluno : alunosTurma) {
            double[] notas = new double[numQuestoes];
            for (int i = 0; i < numQuestoes; i++) {
                notas[i] = Double.parseDouble(reader.readLine());
            }
            alunosProvas.add(new AlunoProva(aluno, notas));
        }

        return new Prova(nome, new Data(dia, mes, ano), valor, numQuestoes, alunosProvas);
    }

    private static Trabalho carregarTrabalho(BufferedReader reader, Sistema sistema, ArrayList<Aluno> alunosTurma) throws IOException {
        String nome = reader.readLine();
        int dia = Integer.parseInt(reader.readLine());
        int mes = Integer.parseInt(reader.readLine());
        int ano = Integer.parseInt(reader.readLine());
        double valor = Double.parseDouble(reader.readLine());
        int numIntegrantes = Integer.parseInt(reader.readLine());
        int numGrupos = Integer.parseInt(reader.readLine());

        ArrayList<GrupoTrabalho> grupos = new ArrayList<>();
        for (int i = 0; i < numGrupos; i++) {
            int numAlunosGrupo = Integer.parseInt(reader.readLine());
            ArrayList<Aluno> alunosGrupo = new ArrayList<>();
            for (int j = 0; j < numAlunosGrupo; j++) {
                String matAluno = reader.readLine();
                Aluno aluno = sistema.encontrarAluno(matAluno);
                if (aluno != null) {
                    alunosGrupo.add(aluno);
                }
            }
            double notaGrupo = Double.parseDouble(reader.readLine());
            grupos.add(new GrupoTrabalho(alunosGrupo, notaGrupo));
        }

        return new Trabalho(nome, new Data(dia, mes, ano), valor, numIntegrantes, grupos);
    }
}
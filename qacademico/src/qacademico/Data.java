package qacademico;

public class Data {
    private int dia;
    private int mes;
    private int ano;

    public Data(int dia, int mes, int ano) {
        if (!valida(dia, mes, ano)) {
            throw new IllegalArgumentException("Data inválida!");
        }
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public static boolean valida(int dia, int mes, int ano) {
        if (ano < 0) return false;
        if (mes < 1 || mes > 12) return false;

        int[] diasPorMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (mes == 2 && (ano % 400 == 0 || (ano % 100 != 0 && ano % 4 == 0))) {
            return dia >= 1 && dia <= 29;
        }

        return dia >= 1 && dia <= diasPorMes[mes - 1];
    }

    // ISSO AQUI TA INÚTIIIIIIIIIIIL
    public boolean posterior(Data d2) {
        if (this.ano > d2.ano) return true;
        if (this.ano == d2.ano) {
            if (this.mes > d2.mes) return true;
            if (this.mes == d2.mes && this.dia > d2.dia) return true;
        }
        return false;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }
}
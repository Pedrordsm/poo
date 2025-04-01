package banco;

public class Banco {
    public static void main(String[] args) {
        Pessoa p1 = new Pessoa("Heitor","123.456.789-01",19,'M');
        Pessoa p2 = new Pessoa("Junin","111.222.333-44",21,'M');

        Conta c1 = new Conta("1234-5",p1,0,200);
        Conta c2 = new Conta("2345-6",p2,100000,200);

        c1.extrato();
        c2.extrato();

        c1.sacar(150);
        c1.transferir(100,c2);
        c1.sacar(100);
        c1.depositar(100);
        c1.transferir(200,c2);

        for (int i =1; i<=365;i++){
            c1.chequeEspecial(0.5);
            System.out.println("Saldo após " + i + "dia(s) : R$" + c1.saldo);
        }
    }
}

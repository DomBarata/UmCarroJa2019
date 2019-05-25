import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class UmCarroJaView {

    private Scanner input;


    public String userLogin() {
        out.println("Bem vindo a aplicação Um Carro Já!");
        out.println("Iremos validar o seu NIF, e se existir poderá iniciar sessão, senão poderá criar uma nova conta");
        out.println();
        out.print("Insira o seu NIF: ");
        input = new Scanner(System.in);

        return input.next();
    }

    public String passwordLogin(){
        out.print("Insira a password: ");
        input = new Scanner(System.in);

        return input.next();
    }

    public int wrongUser(){
        out.println("O NIF inserido nao existe!");
        out.println("Se e novo na aplicacao, vamos criar um user");
        out.println("Escolha a opcao pretendida");
        out.println("\t1 se é um proprietário de veículos");
        out.println("\t2 se é um cliente que pretende requesitar veículos");
        out.println("\t0 se se enganou a inserir o NIF e pretende iniciar sessão");
        input = new Scanner(System.in);

        return input.nextInt();
    }

    public void wrongPassword(int i){
            out.println("A password inserida está errada!");
            out.println("\tTem mais " + (5 - i) + " tentativas");

    }

    public void wrongPassword() {
        out.println("Inseriu a password errada demasiadas vezes");
        out.println("Boa sorte para um proxima ^^");
    }

    public String[] inserirNovoUser(String nif){
        List<String> dados = new ArrayList<>();
        boolean flag = true;

        out.println("Como se chama?");
        input = new Scanner(System.in);
        dados.add(input.next());
        out.println("Insira uma password");
        input = new Scanner(System.in);
        dados.add(input.next());
        do {
            out.println("Repita a password");
            input = new Scanner(System.in);
            if (!input.next().equals(dados.get(1))) {
                out.println("As passwords não coincidem");
            }
            else
                flag = false;
        }
        while(flag);
        dados.add(nif);
        out.println("Insira o seu email");
        input = new Scanner(System.in);
        dados.add(input.next());
        out.println("Insira a sua morada");
        input = new Scanner(System.in);
        dados.add(input.next());
        out.println("Insira a sua data de nascimento (DD-MM-AAAA)");
        input = new Scanner(System.in);
        String[] s = input.next().split("\\-");
        dados.add(s[0]);
        dados.add(s[1]);
        dados.add(s[2]);


        for(String str : dados)
            out.println(str);

        return dados.toArray(String[]::new);
    }

    public String[] localizacao(){
        String[] dados = new String[2];

        out.println("Insira as suas coodenadas em X");
        input = new Scanner(System.in);
        dados[0] = input.next();
        out.println("Insira as suas coodenadas em Y");
        input = new Scanner(System.in);
        dados[1] = input.next();

        return dados;
    }

    public int proprietarioMenu() {
        out.println("Bem vindo Proprietário!!");
        out.println();
        out.println("1 - Inserir veículo novo");
        out.println("2 - Sinalizar carro disponível para aluguer");
        out.println("3 - Abastecer veículo");
        out.println("4 - Alterar preço/km");
        out.println("5 - Clientes pendentes");
        out.println("6 - Consultar custo da úlima viagem");
        out.println("7 - Aceitar/recusar aluguer");
        out.println("8 - Ver o meu perfil");
        out.println("9 - Ver perfil de um determinado cliente");
        out.println("0 - Sair");

        input = new Scanner(System.in);

        return input.nextInt();
    }


    public String getMatricula()    {
        out.println("Insira a matricula do carro");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public String getNif(){
        out.println("Insira o NIF");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public String getTipo()    {
        out.println("Insira o tipo de carro (Electrico, Gasolina, Hibrido)");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public String getMarca()    {
        out.println("Insira a marca do carro");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public double getVelMedia() {
        out.println("Insira a velocidade média do carro");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    public double getPrecoKm() {
        out.println("Insira o preço/km");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    public double getConsumoKm() {
        out.println("Insira o consumo/km");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    public int getAutonomia(){
        out.println("Insira a autonomia do carro");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public Ponto<Double> getLocalizacao() {
        out.println("Insira as coordenadas do carro (X,Y)");
        Scanner scanner = new Scanner(System.in).useDelimiter(",");
        return new Ponto<>(scanner.nextDouble(), scanner.nextDouble());
    }

    public double abastecimento(String tipo, double precoCombustivel, double autonomia) {
        out.println("O carro selecionado e " + tipo +", e tem autonomia para " + autonomia + "kms");
        out.println("O preco do combustivel e " + precoCombustivel);
        out.println("Escolha o que pretende fazer");
        out.println("\t0 - Nao abastecer");
        out.println("\t1 - Atestar o deposito");
        out.println("\tInsira o valor em EUR que pretende abastecer");

        input = new Scanner(System.in);
        return input.nextDouble();
    }


    public void printClientesPendentes(Map<String, List<Pedido>> listaDeEspera) {
        for (Map.Entry<String, List<Pedido>> entry : listaDeEspera.entrySet()) {
            out.print(entry.getKey()+ ": ");
            List<Pedido> pedidos = entry.getValue();
            for (Pedido p: pedidos) {
                out.println("--------  " + p.getNumeroPedido() +"  --------");
                out.println("\tNome: " + p.getCliente().getNome());
                out.println("\tNIF: " + p.getCliente().getNif());
                out.println("\tInicio: " + p.getVeiculo().getLocalizacao());
                out.println("\tFim: " + p.getDestino());
            }
            out.println();
        }
    }

    public int validarAluguer() {
        out.println("Pretende \n\t1- Aceitar\n\t2 - Recusar");
        input = new Scanner(System.in);
        return input.nextInt();
    }

    public void getPerfil(User u){
        out.println("Nome: " + u.getNome());
        out.println("Nif: " + u.getNif());
        out.println("Email: " + u.getEmail());
        out.println("Morada: " + u.getMorada());
        out.println("Data de nascimento: " + u.getNascimento());
        if ((u.getClassificacao() == -1))
            out.println("e ainda não tem classificações");
        else
            out.println("e está classificado em " + u.getClassificacao() + "/100");
        out.print("Prima ENTER para ver histório de alugueres...");
        try {
            System.in.read();
            printAlugueres(u.getHistorico());
            System.in.read();
        }catch (IOException exc){out.println(exc);}


}

    private void printAlugueres(List<Aluguer> historico) {
        int i = 0;
        while (historico.size() > i && i <10){
            Aluguer a = historico.get(i);
            out.println("Veiculo: " + a.getVeiculoUsado().getMatricula());
            out.println("De: " + a.getInicioViagem());
            out.println("Até: " + a.getFimViagem());
            out.println("Percorreu: " + a.distanciaPercorrida() + "kms");
            out.println("-----------------------");
            i++;
        }
        if(historico.size() == 0){
            out.println("Não há histórico de alugueres para apresentar");
        }

    }

    public int clienteMenu() {
        out.println("Bem vindo Cliente!!");
        out.println();
        out.println("1 - Ver o veículo mais próximo");
        out.println("2 - Ver o veículo mais barato");
        out.println("3 - Ver o veículo mais barato dentro de uma determinada distância");
        out.println("4 - Ver os carros de um determinado tipo");
        out.println("5 - Ver os carros com uma determinada autonomia mínima");
        out.println("6 - Efetuar pedido de aluguer");
        out.println("7 - Verificar pedido de aluguer");
        out.println("8 - Ver o meu perfil");
        out.println("9 - Ver perfil de um determinado proprietário");
        out.println("0 - Sair");

        input = new Scanner(System.in);

        return input.nextInt();
    }


    public void printVeiculo(Veiculo v, double distancia){
        out.println("Carro a " + v.getTipo() + " com a matrícula: " + v.getMatricula());
        out.println("tem uma autonomia de " + v.getAutonomia() + "kms");
        out.println("consome " + v.getConsumoKm() + "l/km");
        out.println("tem um preco/km de " + v.getPrecoKm() + "€");
        out.println("anda a uma velocidade de " + v.getVelMedia() + "km/h");
        out.println("está a uma distancia de " + distancia + "kms");
        if ((v.getClassificacao() == -1))
            out.println("e o veículo ainda não tem classificações");
        else
            out.println("e o veículo está classificado em " + v.getClassificacao() + "/100");
    }

    public double getDistancia() {
        out.println("Que distância está disposto a percorrer até ao carro?");
        input = new Scanner(System.in);

        return input.nextDouble();
    }

    public void printListaVeiculos(List<Veiculo> listaDeCarros, Ponto<Double> clienteLocalizacao) {
        for (Veiculo v : listaDeCarros) {
            printVeiculo(v, v.getLocalizacao().distanceTo(clienteLocalizacao));
            out.println("-----------------------------");
        }
        input = new Scanner(System.in);
    }

    public Ponto<Double> getDestino() {
        out.println("Insira as coordenadas do destino (X,Y)");
        Scanner scanner = new Scanner(System.in).useDelimiter(",");
        return new Ponto<>(scanner.nextDouble(), scanner.nextDouble());
    }

    public void pedidoRejeitado() {
        out.println("O seu pedido foi rejeitado");
    }

    public void pedidoAceite() {
        out.println("O seu pedido foi aceite");
        out.println("O aluguer foi feito com sucesso e a viajem realizada");
    }

    public void pedidoPendente() {
        out.println("O seu pedido ainda está pendente");
    }
}

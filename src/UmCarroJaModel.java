import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.out;

public class UmCarroJaModel implements Serializable {

    //Map Key - NIF, Value - Prop
    private Map<String, Proprietario> proprietarioMap;
    //Map Key - NIF, Value - cliete
    private Map<String, Cliente> clientesMap;
    //Map Key - Matricula, Value - Veiculo
    private Map<String, Veiculo> veiculosMap;

    public UmCarroJaModel(){
        this.proprietarioMap = new HashMap<>();
        this.clientesMap = new HashMap<>();
        veiculosMap = new HashMap<>();
    }

    private static List<String> lerAllLines(String fichtxt) {
        List<String> linhas = new ArrayList<>();
        try{
            linhas = Files.readAllLines(Paths.get(fichtxt));
        }
        catch (IOException exc) {out.println(exc);}
        return linhas;
    }

    public void createData()
    {
        List<String> input = lerAllLines("logsPOO_carregamentoInicial.bak");
        String[] divisao;
        String[] argumentos;

        for (String s : input) {
            divisao = s.split(":");
            switch (divisao[0])
            {
                case "NovoProp":    argumentos = divisao[1].split(",");
                                    this.proprietarioMap.put(argumentos[2], new Proprietario(argumentos));
                                    break;
                case "NovoCliente": argumentos = divisao[1].split(",");
                                    this.clientesMap.put(argumentos[2], new Cliente(argumentos));
                                    break;
                case "NovoCarro":   argumentos = divisao[1].split(",");
                                    this.veiculosMap.put(argumentos[2], new Veiculo(argumentos));
                                    break;
                case "Aluguer":     argumentos = divisao[1].split(",");
                                    insereAluguer(argumentos);
                                    break;
                case "Classificar": argumentos = divisao[1].split(",");
                                    classifica(argumentos);
                                    break;
                default:            break;
            }
        }
    }

    private void classifica(String[] argumentos){
        if (this.proprietarioMap.containsKey(argumentos[0])) {
            Proprietario p = this.proprietarioMap.get(argumentos[0]);
            p.classificar(Integer.parseInt(argumentos[1]));
            this.proprietarioMap.put(p.getNif(), p);
        }else if(this.clientesMap.containsKey(argumentos[0])) {
            Cliente c = this.clientesMap.get(argumentos[0]);
            c.classificar(Integer.parseInt(argumentos[1]));
            this.clientesMap.put(c.getNif(), c);
        }else{
            Veiculo v = this.veiculosMap.get(argumentos[0]);
            v.classificar(Integer.parseInt(argumentos[1]));
            this.veiculosMap.put(v.getMatricula(), v);
        }
    }

    public void classifica(String nif, int classificacao){
        if (this.proprietarioMap.containsKey(nif)){
            Proprietario p = this.proprietarioMap.get(nif);
            p.classificar(classificacao);
            this.proprietarioMap.put(p.getNif(), p);
        }else if(this.clientesMap.containsKey(nif)) {
            Cliente c = this.clientesMap.get(nif);
            c.classificar(classificacao);
            this.clientesMap.put(c.getNif(), c);
        }else{
            Veiculo v = this.veiculosMap.get(nif);
            v.classificar(classificacao);
            this.veiculosMap.put(v.getMatricula(), v);
        }
    }

    private void insereAluguer(String[] argumentos) {
        String preferencia = argumentos[4];
        Veiculo v = null;

        if(preferencia.equals("MaisPerto")){
            v = getVeiculoMaisProximo(argumentos[0], argumentos[3]);
        }else if(preferencia.equals("MaisBarato")){
            v = getVeiculoMaisBarato(argumentos[3]);
        }

        if(v != null) {
            Pedido p = new Pedido(clientesMap.get(argumentos[0]), v,
                    new Ponto<>(Double.parseDouble(argumentos[1]), Double.parseDouble(argumentos[2])));
            pedidoAceite(p);
        }
    }


    public Veiculo getVeiculo(String matricula, String nif) {
        Veiculo v = this.veiculosMap.get(matricula);

        if(v == null)
            return v;
        else if(v.getNifProp().equals(nif))
            return v.clone();
        else
            return null;
    }

    public Veiculo getVeiculo(String matricula) {
        return this.veiculosMap.get(matricula);

    }


    public void inserirVeiculo(Veiculo v) {
        if(!veiculosMap.containsKey(v.getMatricula()))
            this.veiculosMap.put(v.getMatricula(), v);
    }

    public int existeNif(String nif) {
        if (this.proprietarioMap.containsKey(nif))
            return 1;
        else if(this.clientesMap.containsKey(nif))
            return 2;
        else if(nif.equals("0"))
            return 3;
        else
            return 0;
    }

    public boolean checkProprietarioPassword(String nif, String password) {
        return this.proprietarioMap.get(nif).validatePassword(password);
    }

    public boolean checkClientePassword(String nif, String password) {
        return this.clientesMap.get(nif).validatePassword(password);
    }

    public void inserirProprietario(Proprietario proprietario) {
        this.proprietarioMap.put(proprietario.getNif(), proprietario);
    }

    public void inserirCliente(Cliente cliente) {
        this.clientesMap.put(cliente.getNif(), cliente);
    }

    public User getUser(String nif) {
        if (this.proprietarioMap.containsKey(nif))
            return proprietarioMap.get(nif);
        else
            return clientesMap.get(nif);
    }

    public Proprietario getProprietario(String nif) {
        return this.proprietarioMap.get(nif);
    }

    public Cliente getCliente(String nif) {
       return this.clientesMap.get(nif);
    }

    public Set<String> getMatriculas(){
        return this.veiculosMap.keySet();
    }

    public List<Veiculo> getListaVeiculos(String tipo) {
        List<Veiculo> carros = new ArrayList<>();
        Set<String> matriculas = this.veiculosMap.keySet();
        for(String s : matriculas){
            Veiculo v = this.veiculosMap.get(s);
            if (v.getTipo().equals(tipo))
                carros.add(v);
        }
        return carros;
    }

    public int fazerPedido(String nif, String matricula, Ponto<Double> destino) {
        Pedido pedido = new Pedido(this.clientesMap.get(nif).clone(),
                this.veiculosMap.get(matricula).clone(),
                destino);

        Proprietario p = this.proprietarioMap.get(this.veiculosMap.get(matricula).getNifProp());
        int numeroEspera = p.addPedido(pedido);
        this.proprietarioMap.put(p.getNif(), p);

        return numeroEspera;
    }

    public Veiculo getVeiculoMaisProximo(String nifCliente){
        Set<String> matriculas = getMatriculas();
        Cliente c = getCliente(nifCliente);
        Veiculo maisPerto = null;
        for (String matricula: matriculas) {
            Veiculo v = getVeiculo(matricula);
            if(maisPerto == null &&
                    v.getDisponivel())
                maisPerto = v;
            else if(maisPerto != null &&
                    c.getLocalizacao().distanceTo(maisPerto.getLocalizacao()) >
                            c.getLocalizacao().distanceTo(v.getLocalizacao()) &&
                    v.getDisponivel())
                maisPerto = v;
        }
        return maisPerto;
    }

    public Veiculo getVeiculoMaisProximo(String nifCliente, String tipo){
        List<Veiculo> veiculos = getListaVeiculos(tipo);
        Cliente c = getCliente(nifCliente);
        Veiculo maisPerto = null;
        for (Veiculo v: veiculos) {
            if(maisPerto == null &&
                    v.getDisponivel())
                maisPerto = v;
            else if(maisPerto != null &&
                    c.getLocalizacao().distanceTo(maisPerto.getLocalizacao()) >
                            c.getLocalizacao().distanceTo(v.getLocalizacao()) &&
                    v.getDisponivel())
                maisPerto = v;
        }
        return maisPerto;
    }

    public Veiculo getVeiculoMaisBarato() {
        Set<String> matriculas = getMatriculas();
        Veiculo maisBarato = null;
        for (String matricula : matriculas) {
            Veiculo v = getVeiculo(matricula);
            if (maisBarato == null && v.getDisponivel())
                maisBarato = v;
            else if (maisBarato != null &&
                    v.getPrecoKm() < maisBarato.getPrecoKm() &&
                    v.getDisponivel())
                maisBarato = v;
        }
        return maisBarato;
    }

    private Veiculo getVeiculoMaisBarato(String tipo) {
        List<Veiculo> veiculos = getListaVeiculos(tipo);
        Veiculo maisBarato = null;
        for (Veiculo v : veiculos) {
            if (maisBarato == null && v.getDisponivel())
                maisBarato = v;
            else if (maisBarato != null &&
                    v.getPrecoKm() < maisBarato.getPrecoKm() &&
                    v.getDisponivel())
                maisBarato = v;
        }
        return maisBarato;
    }

    public Veiculo getVeiculoMaisBarato(String nifCliente, double dist){
        Set<String> matriculas = getMatriculas();
        Cliente c = getCliente(nifCliente);
        Veiculo maisBarato = null;
        for (String matricula : matriculas) {
            Veiculo v = getVeiculo(matricula);
            if (maisBarato == null
                    && v.getLocalizacao().distanceTo(c.getLocalizacao()) < dist
                    && v.getDisponivel())
                maisBarato = v;
            else if (maisBarato != null && v.getPrecoKm() < maisBarato.getPrecoKm()
                    && v.getLocalizacao().distanceTo(c.getLocalizacao()) < dist
                    && v.getDisponivel())
                maisBarato = v;
        }
        return maisBarato;
    }

    public List<Veiculo> getListaVeiculos(int aut) {
        List<Veiculo> carros = new ArrayList<>();
        Set<String> matriculas = this.veiculosMap.keySet();
        for(String s : matriculas){
            Veiculo v = this.veiculosMap.get(s);
            if (v.getAutonomia() >= aut)
                carros.add(v);
        }
        return carros;
    }

    public Pedido getPedido(String nif) {
        Cliente cliente = this.clientesMap.get(nif);
        Set<String> prop = this.proprietarioMap.keySet();
        Pedido pedido = null;
        for(String s : prop){
            Proprietario proprietario = this.proprietarioMap.get(s);
            Map<String, List<Pedido>> pedidos = proprietario.getListaDeEspera();
            Set<String> matriculas = pedidos.keySet();
            for(String str : matriculas){
                List<Pedido> espera = pedidos.get(str);
                for(Pedido p : espera){
                    if (p.getCliente().equals(cliente))
                        pedido = p.clone();
                }
            }
        }
        return pedido;
    }

    public void pedidoRejeitado(Pedido p) {
        Proprietario prop = proprietarioMap.get(p.getVeiculo().getNifProp());
        List<Pedido> pedidos = prop.getListaDeEspera().get(p.getVeiculo().getMatricula());
        pedidos.remove(p);
        prop.insereLista(p.getVeiculo().getMatricula(), pedidos);
        this.proprietarioMap.put(prop.getNif(), prop);
    }

    public void pedidoAceite(Pedido p) {
        Proprietario prop = this.proprietarioMap.get(p.getVeiculo().getNifProp());
        List<Pedido> pedidos = prop.getListaDeEspera().get(p.getVeiculo().getMatricula()); //lista de pedidos do prop

        if(pedidos != null){
            pedidos.remove(p); //remove o pedido em questão
            prop.insereLista(p.getVeiculo().getMatricula(), pedidos); //insere no proprietario a lista sem o pedido
        }


        Veiculo veiculo = p.getVeiculo();
        Aluguer alug = veiculo.alugar(p); //aluga o veiculo, adicionando o historico
        this.veiculosMap.put(veiculo.getMatricula(), veiculo);

        prop.addAluguer(alug);
        this.proprietarioMap.put(prop.getNif(), prop);

        Cliente cli = p.getCliente();
        cli.setLocalizacao(p.getDestino());
        cli.addAluguer(alug);
        this.clientesMap.put(cli.getNif(), cli);
    }

}
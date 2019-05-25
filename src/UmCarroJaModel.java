import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.out;

public class UmCarroJaModel {

    //Map Key - NIF, Value - Prop
    private Map<String, Proprietario> proprietarioMap;
    //Map Key - NIF, Value - cliete
    private Map<String, Cliente> clientesMap;
    //Map Key - Matricula, Value - Veiculo
    private Map<String, Veiculo> veiculosMap;


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
        this.proprietarioMap = new HashMap<>();
        this.clientesMap = new HashMap<>();
        veiculosMap = new HashMap<>();

        List<String> input = lerAllLines("logsPOO_carregamentoInicial.bak");
        String[] divisao;
        String[] argumentos;

        for (String s : input) {
            divisao = s.split(":");
            switch (divisao[0])
            {
                case "NovoProp":    argumentos = divisao[1].split(",");
                                    this.proprietarioMap.put(argumentos[1], new Proprietario(argumentos));
                                    break;
                case "NovoCliente": argumentos = divisao[1].split(",");
                                    this.clientesMap.put(argumentos[1], new Cliente(argumentos));
                                    break;
                case "NovoCarro":   argumentos = divisao[1].split(",");
                                    this.veiculosMap.put(argumentos[2], new Veiculo(argumentos));
                                    break;
                case "Aluguer":     argumentos = divisao[1].split(",");
                                    //terminar...
                                    break;
                case "Classificar": argumentos = divisao[1].split(",");
                                    if (argumentos[0].length() == 9)
                                    {
                                        if (this.proprietarioMap.containsKey(argumentos[0]))
                                        {
                                            Proprietario p = this.proprietarioMap.get(argumentos[0]);
                                            p.classificar(Integer.parseInt(argumentos[1]));
                                            this.proprietarioMap.put(p.getNif(), p);
                                        }
                                        else
                                        {
                                            Cliente c = this.clientesMap.get(argumentos[0]);
                                            c.classificar(Integer.parseInt(argumentos[1]));
                                            this.clientesMap.put(c.getNif(), c);
                                        }
                                    }
                                    else
                                    {
                                        Veiculo v = this.veiculosMap.get(argumentos[0]);
                                        v.classificar(Integer.parseInt(argumentos[1]));
                                        this.veiculosMap.put(v.getMatricula(), v);
                                    }
                                    break;
                default:            break;
            }
        }
    }


    public Veiculo getVeiculo(String matricula, String nif) {
        Veiculo v = this.veiculosMap.get(matricula);

        return (v.getNifProp().equals(nif)?v.clone():null);
    }

    public Veiculo getVeiculo(String matricula) {
        return this.veiculosMap.get(matricula);

    }


    public void inserirVeiculo(Veiculo v) {
        this.veiculosMap.put(v.getMatricula(), v);

    }

    public int existeNif(String nif) {
        if (this.proprietarioMap.containsKey(nif))
            return 1;
        else if(this.clientesMap.containsKey(nif))
            return 2;
        else return 0;
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
        return this.proprietarioMap.get(nif).clone();
    }

    public Cliente getCliente(String nif) {
        return this.clientesMap.get(nif).clone();
    }

    public Set<String> getMatriculas(){
        return this.veiculosMap.keySet();
    }

    public List<Veiculo> getCarrosTipo(String tipo) {
        List<Veiculo> carros = new ArrayList<>();
        Set<String> matriculas = this.veiculosMap.keySet();
        for(String s : matriculas){
            Veiculo v = this.veiculosMap.get(s);
            if (v.getTipo() == tipo)
                carros.add(v);
        }
        return carros;
    }
}

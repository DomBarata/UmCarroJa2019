import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Proprietario extends User implements Serializable {
    private Map<String,List<Pedido>> listaDeEspera;

    public Proprietario(String nome, String password, String nif,
                        String email, String morada, LocalDate nascimento) {
        super(nome, password, nif, email, morada, nascimento);
        this.listaDeEspera = new HashMap<>();
    }

    public Proprietario(Proprietario u) {
        super(u.getNome(), u.getPassword(), u.getNif(), u.getEmail(), u.getMorada(), u.getNascimento());
        this.listaDeEspera = new HashMap<>();
        this.listaDeEspera.putAll(u.getListaDeEspera());
    }

    public Proprietario(String[] s) {
        super(s[0], s[1], s[2], s[3], s[4],
                LocalDate.parse(s[5].subSequence(0,s[5].length())));
        this.listaDeEspera = new HashMap<>();
    }

    public Map<String, List<Pedido>> getListaDeEspera() {
        return listaDeEspera;
    }

    public int addPedido(Pedido p) {
        List<Pedido> pedidos = new ArrayList<>();
        int tam;
        if (this.listaDeEspera.containsKey(p.getVeiculo().getMatricula())) {
            pedidos = this.listaDeEspera.get(p.getVeiculo().getMatricula());
        }
        tam = pedidos.size();
        p.setNumeroPedido(tam);
        pedidos.add(p);
        this.listaDeEspera.put(p.getVeiculo().getMatricula(),pedidos);

        return tam;
    }

    public void validarAluguer(String matricula, int estado){
        List<Pedido> p = this.listaDeEspera.get(matricula);
        Pedido ped = p.get(0);
        ped.setEstado(estado);
    }

    public Proprietario clone(){
        return new Proprietario(this);
    }

    public void insereLista(String matricula, List<Pedido> pedidos) {
        this.listaDeEspera.put(matricula, pedidos);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("NovoProp:").append(super.getNome()).append(",")
                .append(super.getPassword()).append(",")
                .append(super.getNif()).append(",")
                .append(super.getEmail()).append(",")
                .append(super.getMorada()).append(",")
                .append(super.getNascimento());
        return sb.toString();
    }
}

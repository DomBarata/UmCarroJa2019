import java.util.*;

public class Proprietario extends User{
    private Map<String,List<Pedido>> listaDeEspera;

    public Proprietario(String nome, String password, String nif,
                        String email, String morada, GregorianCalendar nascimento) {
        super(nome, password, nif, email, morada, nascimento);
        this.listaDeEspera = new HashMap<>();
    }

    public Proprietario(Proprietario u) {
        super(u);
        this.listaDeEspera = new HashMap<>();
        this.listaDeEspera.putAll(u.getListaDeEspera());
    }

    public Proprietario(String[] s) {
        super(s[0], s[1], s[2], s[3], s[4],
                new GregorianCalendar(Integer.parseInt(s[5]),
                        Integer.parseInt(s[6]), Integer.parseInt(s[7])));
        this.listaDeEspera = new HashMap<>();
    }

    public Map<String, List<Pedido>> getListaDeEspera() {
        return listaDeEspera;
    }

    public void addPedido(Pedido p) {
        List<Pedido> pedidos = new ArrayList<>();
        if (this.listaDeEspera.containsKey(p.getVeiculo().getMatricula())) {
            pedidos = this.listaDeEspera.get(p.getVeiculo().getMatricula());
            p.setNumeroPedido(pedidos.size());
        }
        pedidos.add(p);
        this.listaDeEspera.put(p.getVeiculo().getMatricula(),pedidos);
    }

    public void validarAluguer(String matricula, int estado){
        List<Pedido> p = this.listaDeEspera.get(matricula);
        Pedido ped = p.get(0);
        ped.setEstado(estado);
    }

    public Proprietario clone(){
        return new Proprietario(this);
    }
}

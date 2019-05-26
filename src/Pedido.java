import java.io.Serializable;

public class Pedido implements Serializable {
    private int estado; //0 pendente, 1 aceite, 2 rejeitado
    private int numeroPedido;
    private Cliente cliente;
    private Veiculo veiculo;
    private Ponto<Double> destino;

    Pedido(Cliente cliente, Veiculo veiculo, Ponto<Double> destino)    {
        this.estado = 0;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.destino = destino;
    }

    public Pedido() {

    }

    public Pedido(Pedido p){
        this.estado = p.getEstado();
        this.numeroPedido = p.getNumeroPedido();
        this.cliente = p.getCliente();
        this.veiculo = p.getVeiculo();
        this.destino = p.getDestino();
    }

    public int getEstado(){
        return this.estado;
    }

    public void setEstado(int estado){
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente.clone();
    }

    public Veiculo getVeiculo() {
        return veiculo.clone();
    }

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Ponto<Double> getDestino() {
        return destino;
    }

    public Pedido clone(){
        return new Pedido(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido:")
                .append(this.veiculo.getNifProp()).append(",")
                .append(this.estado).append(",")
                .append(this.numeroPedido).append(",")
                .append(this.cliente.getNif()).append(",")
                .append(this.veiculo.getMatricula()).append(",")
                .append(this.destino);
        return sb.toString();
    }
}

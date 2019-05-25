public class Pedido {
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

    public int getEstado(){
        return this.estado;
    }

    public void setEstado(int estado){
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
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
}

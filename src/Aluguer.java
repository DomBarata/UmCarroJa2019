import java.io.Serializable;

public class Aluguer implements Serializable {
    private Veiculo veiculoUsado;
    private User userAlugado;
    private Ponto<Double> inicioViagem;
    private Ponto<Double> fimViagem;

    public Aluguer(Veiculo veiculoUsado, User userAlugado, Ponto<Double> inicioViagem, Ponto<Double> fimViagem) {
        this.veiculoUsado = veiculoUsado;
        this.userAlugado = userAlugado;
        this.inicioViagem = inicioViagem;
        this.fimViagem = fimViagem;
    }

    public String getNifProprietario(){
        return veiculoUsado.getNifProp();
    }

    public String getNifUser(){
        return userAlugado.getNif();
    }

    public double getPrecoViagem(){
        double preco = this.veiculoUsado.getPrecoKm();
        double distancia = inicioViagem.distanceTo(fimViagem);

        return preco*distancia;
    }

    public double distanciaPercorrida(){
        return inicioViagem.distanceTo(fimViagem);
    }

    public Veiculo getVeiculoUsado() {
        return veiculoUsado;
    }

    public User getUserAlugado() {
        return userAlugado;
    }

    public Ponto<Double> getInicioViagem() {
        return inicioViagem;
    }

    public Ponto<Double> getFimViagem() {
        return fimViagem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Alugado:")
                .append(this.veiculoUsado.getMatricula()).append(",")
                .append(this.userAlugado.getNif()).append(",")
                .append(this.inicioViagem).append(",")
                .append(this.fimViagem);
        return sb.toString();
    }
}

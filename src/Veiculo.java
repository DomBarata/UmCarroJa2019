import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Veiculo implements Serializable {
	private final String tipo;
	private String marca;
	private String matricula;
	private String nifProp;
	private double velMedia;
	private double precoKm;
	private double consumoKm;
	private int autonomia;
	private double classificacao;
	private Ponto<Double> localizacao;
	private int numeroAvaliacoes = 0;
	private List<Aluguer> historico;
	private boolean disponivel;

	public Veiculo(String tipo, String marca, String matricula, String nifProp,
				   double velMedia, double precoKm, double consumoKm, int autonomia,
				   Ponto<Double> localizacao) {
		this.tipo = tipo;
		this.marca = marca;
		this.matricula = matricula;
		this.nifProp = nifProp;
		this.velMedia = velMedia;
		this.precoKm = precoKm;
		this.consumoKm = consumoKm;
		this.autonomia = autonomia;
		this.classificacao = 0;
		this.localizacao = localizacao;
		this.historico = new ArrayList<>();
		this.disponivel = true;
	}

	public Veiculo(Veiculo v)
	{
		this.tipo = v.getTipo();
		this.marca = v.getMarca();
		this.matricula = v.getMatricula();
		this.nifProp = v.getNifProp();
		this.velMedia = v.getVelMedia();
		this.precoKm = v.getPrecoKm();
		this.consumoKm = v.getConsumoKm();
		this.autonomia = v.getAutonomia();
		this.classificacao = v.getClassificacao();
		this.localizacao = v.getLocalizacao();
		this.numeroAvaliacoes = v.getNumeroAvaliacoes();
		this.historico = new ArrayList<>();
		this.historico.addAll(v.getHistorico());
		this.disponivel = v.getDisponivel();
	}

	public Veiculo(String[] s)
	{
		this.tipo = s[0];
		this.marca = s[1];
		this.matricula = s[2];
		this.nifProp = s[3];
		this.velMedia = Double.parseDouble(s[4]);
		this.precoKm = Double.parseDouble(s[5]);
		this.consumoKm = Double.parseDouble(s[6]);
		this.autonomia = Integer.parseInt(s[7]);
		this.classificacao = 0;
		this.localizacao = new Ponto<>(Double.parseDouble(s[8]), Double.parseDouble(s[9]));
		this.historico = new ArrayList<>();
		this.disponivel = true;
	}

	public final String getTipo() {
		return tipo;
	}

	public String getMarca() {
		return marca;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getNifProp() {
		return nifProp;
	}

	public double getVelMedia() {
		return velMedia;
	}

	public double getPrecoKm() {
		return precoKm;
	}

	public double getConsumoKm() {
		return consumoKm;
	}

	public int getAutonomia() {
		return autonomia;
	}

	public double getClassificacao() {
		if(this.numeroAvaliacoes == 0)
			return -1;
		else
			return this.classificacao;
	}

	public Ponto<Double> getLocalizacao() {
		return localizacao;
	}

	private int getNumeroAvaliacoes() {
		return this.numeroAvaliacoes;
	}

	public void classificar(int valor) {
		if(this.numeroAvaliacoes == 0){
			this.classificacao = valor;
			this.numeroAvaliacoes++;
		}
		else
		{
			this.classificacao = (this.classificacao*this.numeroAvaliacoes + valor)/++this.numeroAvaliacoes;
		}
	}


	public void setVelMedia(double velMedia) {
		this.velMedia = velMedia;
	}

	public void setPrecoKm(double precoKm) {
		this.precoKm = precoKm;
	}

	public void setConsumoKm(double consumoKm) {
		this.consumoKm = consumoKm;
	}

	public void setAutonomia(int i) {
		this.autonomia = i;
	}

	public List<Aluguer> getHistorico() {
		return historico;
	}

	public void setHistorico(Aluguer aluguer) {
		this.historico.add(aluguer);
	}

	public boolean getDisponivel() {
		return this.disponivel;
	}

	public void setDisponivel() {
		this.disponivel = true;
	}

	public void setIndisponive(){
		this.disponivel = false;
	}

	public Aluguer alugar(Pedido p) {
		Aluguer aluguer = new Aluguer(p.getVeiculo(),
				p.getCliente(), p.getVeiculo().getLocalizacao(),
				p.getDestino());
		this.autonomia -= localizacao.distanceTo(p.getDestino());
		this.localizacao = p.getDestino();
		this.disponivel = false;
		this.historico.add(aluguer);

		return aluguer;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("NovoCarro:")
				.append(this.tipo).append(",")
				.append(this.marca).append(",")
				.append(this.matricula).append(",")
				.append(this.nifProp).append(",")
				.append(this.velMedia).append(",")
				.append(this.precoKm).append(",")
				.append(this.consumoKm).append(",")
				.append(this.autonomia).append(",")
				.append(this.localizacao);
		return sb.toString();
	}

    @Override
    public Veiculo clone() {
        return new Veiculo(this);
    }
}

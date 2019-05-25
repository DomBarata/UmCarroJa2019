import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Cliente extends User{
	private Ponto<Double> localizacao;

	public Cliente(String nome, String password, String nif, String email,
				   String morada, LocalDate nascimento, double x, double y){
		super(nome, password, nif, email, morada, nascimento);
		this.localizacao = new Ponto<>(x,y);
	}
	
	public Cliente(Cliente c) {
		super(c);
		this.localizacao = c.getLocalizacao();
	}


	public Cliente(String [] dados)
	{
		super(dados[0], dados[1], dados[2], dados[3], dados[4], LocalDate.parse(dados[5].subSequence(0,dados[5].length())));
		this.localizacao = new Ponto<>(Double.parseDouble(dados[6]),
				Double.parseDouble(dados[7]));
	}

	public Ponto<Double> getLocalizacao() {
		return this.localizacao;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NovoCliente:").append(super.getNome()).append(",")
				.append(super.getNif())
				.append(",").append(super.getEmail()).append(",")
				.append(super.getMorada())
				.append(",").append(localizacao.getX())
				.append(",").append(localizacao.getY());
		return sb.toString();
	}

	public void addAluguer(Aluguer alug) {
		super.addAluguer(alug);
		this.localizacao = alug.getFimViagem();
	}

	@Override
	public Cliente clone() {
		return new Cliente(this);
	}

    public void setLocalizacao(Ponto<Double> destino) {
		this.localizacao = destino;
    }
}

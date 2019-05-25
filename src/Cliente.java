import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class Cliente extends User{
	private Ponto<Double> localizacao;

	public Cliente(String nome, String password, String nif, String email,
				   String morada, GregorianCalendar nascimento, double x, double y){
		super(nome, password, nif, email, morada, nascimento);
		this.localizacao = new Ponto<>(x,y);
	}
	
	public Cliente(Cliente c) {
		super(c);
		this.localizacao = c.getLocalizacao();
	}


	public Cliente(String [] dados)
	{
		super(dados[0], dados[1], dados[2], dados[3], dados[4],
				new GregorianCalendar(Integer.parseInt(dados[5]),
						Integer.parseInt(dados[6]), Integer.parseInt(dados[7])));
		this.localizacao = new Ponto<>(Double.parseDouble(dados[8]),
				Double.parseDouble(dados[9]));
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

	@Override
	public Cliente clone() {
		return new Cliente(this);
	}
}

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class User {
	private String nome;
	private String password;
	private String nif;
	private String email;
	private String morada;
	private GregorianCalendar nascimento;
	private double classificacao;
	private int numeroAvaliacoes = 0;
	private List<Aluguer> historico;

	public User(String nome, String password, String nif, String email, String morada, GregorianCalendar nascimento) {
		this.email = email;
		this.password = password;
		this.nif = nif;
		this.nome = nome;
		this.morada = morada;
		this.nascimento = nascimento;
		this.classificacao = 0;
		this.historico = new ArrayList<>();
	}

	public User(User u) {
		this.email = u.getEmail();
		this.password = u.getPassword();
		this.nif = u.getNif();
		this.nome = u.getNome();
		this.morada = u.getMorada();
		this.getNascimento();
		this.classificacao = u.getClassificacao();
		this.numeroAvaliacoes = u.getNumeroAvaliacoes();
		this.historico = new ArrayList<>();
		this.historico.addAll(u.getHistorico());
	}

	private String getPassword() {
		return password;
	}

	private int getNumeroAvaliacoes() {
		return this.numeroAvaliacoes;
	}

	public String getNif() { return this.nif; }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMorada() {
		return morada;
	}

	public void setMorada(String morada) {
		this.morada = morada;
	}

	public User clone(){
		return new User(this);
	}

	public double getClassificacao() {
		return classificacao;
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

	public GregorianCalendar getNascimento() {
		return (GregorianCalendar) nascimento.clone();
	}


	public boolean validatePassword(String pass){
	    return this.password.equals(pass);
    }

	public List<Aluguer> getHistorico() {
		return historico;
	}

	public void setHistorico(Aluguer aluguer) {
		this.historico.add(0, aluguer);
	}
}

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
	private String nome;
	private String password;
	private String nif;
	private String email;
	private String morada;
	private LocalDate nascimento;
	private double classificacao;
	private int numeroAvaliacoes = 0;
	private List<Aluguer> historico;

	public User(String nome, String password, String nif, String email, String morada, LocalDate nascimento) {
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
		this.nascimento = u.getNascimento();
		this.classificacao = u.getClassificacao();
		this.numeroAvaliacoes = u.getNumeroAvaliacoes();
		this.historico = new ArrayList<>();
		this.historico.addAll(u.getHistorico());
	}

	protected String getPassword() {
		return password;
	}

	private int getNumeroAvaliacoes() {
		return this.numeroAvaliacoes;
	}

	public String getNif() { return this.nif; }

	public String getEmail() {
		return email;
	}

	public String getNome() {
		return nome;
	}

	public String getMorada() {
		return morada;
	}

	public User clone(){
		return new User(this);
	}

	public double getClassificacao() {
		if(this.numeroAvaliacoes == 0)
			return -1;
		else
			return this.classificacao;
	}

	public void classificar(int valor) {
		if(this.numeroAvaliacoes == 0){
			this.classificacao = valor;
			this.numeroAvaliacoes++;
		}else{
			this.classificacao = (this.classificacao*this.numeroAvaliacoes + valor)/++this.numeroAvaliacoes;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this) return true;
		if(obj==null || obj.getClass() != this.getClass()) return false;
		User le = (User) obj;
		return le.getNif().equals(this.nif);
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public boolean validatePassword(String pass){
	    return this.password.equals(pass);
    }

	public List<Aluguer> getHistorico() {
		return historico;
	}

    public void addAluguer(Aluguer alug) {
		this.historico.add(alug);
    }

}

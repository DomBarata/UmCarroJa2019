import java.util.List;
import java.util.Set;

import static java.lang.System.exit;
import static java.lang.System.out;


public class UmCarroJaController {
    private UmCarroJaModel model;
    private UmCarroJaView view;
    private String nif;

    public void setModel(UmCarroJaModel model) {
        this.model = model;
    }

    public void setView(UmCarroJaView view) {
        this.view = view;
    }

    public void start() {
        String password = "hello";
        int errado = 0;
        while (true) {
            this.nif = this.view.userLogin();
            switch (this.model.existeNif(nif)) {
                case 1:
                    while (password != null) {
                        password = this.view.passwordLogin();
                        if (this.model.checkProprietarioPassword(nif, password)){
                            password = null;
                            proprietarioControl();
                        }else{
                            errado++;
                            password = this.view.wrongPassword(errado);
                            if (errado == 5) {
                                this.view.wrongPassword();
                                exit(0);
                            }
                        }
                    }
                break;
                case 2:
                    while (password != null) {
                        password = this.view.passwordLogin();
                        if (this.model.checkClientePassword(nif, password)) {
                            password = null;
                            clienteControl();
                        }else{
                            errado++;
                            password = this.view.wrongPassword(errado);
                            if (errado == 5) {
                                this.view.wrongPassword();
                                exit(0);
                            }
                        }
                    }
                    break;
                case 0:
                    int op = this.view.wrongUser();
                    if (op == 1)
                    {
                        model.inserirProprietario(new Proprietario(this.view.inserirNovoUser(nif)));
                    }else if(op == 2){
                        String[] dados = this.view.inserirNovoUser(nif);
                        String[] local = this.view.localizacao();

                        dados[7] = local[0];
                        dados[8] = local[1];
                        model.inserirCliente(new Cliente(dados));
                    }
                    break;
            }
        }
    }

    private void proprietarioControl() {
        int op;
        do {
            op = this.view.proprietarioMenu();
            switch (op) {
                case 1: this.model.inserirVeiculo(new Veiculo(
                        this.view.getTipo(), this.view.getMarca(),
                        this.view.getMatricula(), this.nif,
                        this.view.getVelMedia(), this.view.getPrecoKm(),
                        this.view.getConsumoKm(), this.view.getAutonomia(),
                        this.view.getLocalizacao()));
                        break;
                case 2: String matricula = this.view.getMatricula();
                        Veiculo v = this.model.getVeiculo(matricula, this.nif);
                        if(v.getDisponivel())
                            out.println("Esse veículo já se encontra disponível");
                        else{
                            v.setDisponivel(true);
                            this.model.inserirVeiculo(v);
                        }
                        break;
                case 3: abastecimento();
                        break;
                case 4: String mat = this.view.getMatricula();
                        Veiculo vei = this.model.getVeiculo(mat, this.nif);
                        double precoKm = this.view.getPrecoKm();
                        vei.setPrecoKm(precoKm);
                        this.model.inserirVeiculo(vei);
                        break;
                case 5: this.view.printClientesPendentes(this.model.getProprietario(nif).getListaDeEspera());
                        break;
                case 6: Proprietario p = (Proprietario) this.model.getUser(nif);
                        List<Aluguer> hist = p.getHistorico();
                        Aluguer aluguer= hist.get(0);
                        out.println(aluguer.getPrecoViagem() + "€");
                        break;
                case 7: String matr = this.view.getMatricula();
                        Proprietario prop = this.model.getProprietario(nif);
                        prop.validarAluguer(matr, this.view.validarAluguer());
                        this.model.inserirProprietario(prop);
                        break;
                case 8: this.view.proprietarioPerfil(this.model.getProprietario(nif));
                        break;
                case 9: String nifCheck = this.view.getNif();
                    this.view.clientePerfil(this.model.getCliente(nifCheck));
                        break;
                case 0: break;
            }

        }while (op != 0) ;
    }

    private void abastecimento() {
        Veiculo v = this.model.getVeiculo(view.getMatricula(), this.nif);
        int autonomia = v.getAutonomia();
        double consumo = v.getConsumoKm();
        double total;
        double op = this.view.abastecimento(v.getTipo(), precoCombusivel(v.getTipo()), v.getAutonomia());

        if (op == 0) {
        } else if (op == 1) {
            total = (((1000 - autonomia) * consumo) / 100) * precoCombusivel(v.getTipo());
            v.setAutonomia(1000);
            this.model.inserirVeiculo(v);
            out.println("O veiculo ficou com uma autonomia de " + v.getAutonomia() + "Kms, e gastou " + total + "EUR");
        } else {
            autonomia = (int) (op / precoCombusivel(v.getTipo()) * 100 / consumo);
            v.setAutonomia(autonomia);
            this.model.inserirVeiculo(v);
            out.println("O veiculo ficou com uma autonoma de " + autonomia + "Kms");
        }
    }

    private void clienteControl() {
        int op;
        do {
            op = this.view.clienteMenu();
            switch (op) {
                case 1: this.view.printVeiculo(veiculoMaisProximo(),
                        veiculoMaisProximo().getLocalizacao().distanceTo(
                                this.model.getCliente(nif).getLocalizacao()
                        ));
                        break;
                case 2: this.view.printVeiculo(veiculoMaisBarato(),
                        veiculoMaisProximo().getLocalizacao().distanceTo(
                                this.model.getCliente(nif).getLocalizacao()
                        ));
                        break;
                case 3: double distancia = this.view.getDistancia();
                        this.view.printVeiculo(veiculoMaisBarato(distancia),
                            veiculoMaisProximo().getLocalizacao().distanceTo(
                                    this.model.getCliente(nif).getLocalizacao()
                            ));
                        break;
                case 4: String tipo = this.view.getTipo();
                        List<Veiculo> listaDeCarros = this.model.getCarrosTipo(tipo);
                        this.view.printVeiculosTipo(listaDeCarros);
                        break;
                case 5: break;
                case 6: break;
                case 7: break;
                case 8: break;
                case 9: break;
                case 0: break;
            }

        }while (op != 0) ;
    }

    private void exitSaveStatus() {
    }

    private double precoCombusivel(String str){
        final double preco = 1.559;
        double ret = 0;
        switch (str){
            case "Gasolina": ret = preco;
                             break;
            case "Eletrico": ret = 0;
                             break;
            case "Hibrido":  ret = preco/2.1;
                             break;
        }
        return ret;
    }

    private Veiculo veiculoMaisProximo(){
        Set<String> matriculas = this.model.getMatriculas();
        Cliente c = this.model.getCliente(nif);
        Veiculo maisPerto = null;
        for (String matricula: matriculas) {
            Veiculo v = this.model.getVeiculo(matricula);
            if(maisPerto == null &&
                    v.getDisponivel())
                maisPerto = v;
            else if(maisPerto != null &&
                    c.getLocalizacao().distanceTo(maisPerto.getLocalizacao()) >
                    c.getLocalizacao().distanceTo(v.getLocalizacao()) &&
                    v.getDisponivel())
                maisPerto = v;
        }
        return maisPerto;
    }

    private Veiculo veiculoMaisBarato() {
        Set<String> matriculas = this.model.getMatriculas();
        Cliente c = this.model.getCliente(nif);
        Veiculo maisBarato = null;
        for (String matricula : matriculas) {
            Veiculo v = this.model.getVeiculo(matricula);
            if (maisBarato == null && v.getDisponivel())
                maisBarato = v;
            else if (maisBarato != null &&
                    v.getPrecoKm() < maisBarato.getPrecoKm() &&
                    v.getDisponivel())
                maisBarato = v;
        }
        return maisBarato;
    }

    private Veiculo veiculoMaisBarato(double dist){
        Set<String> matriculas = this.model.getMatriculas();
        Cliente c = this.model.getCliente(nif);
        Veiculo maisBarato = null;
        for (String matricula : matriculas) {
            Veiculo v = this.model.getVeiculo(matricula);
            if (maisBarato == null
                    && v.getLocalizacao().distanceTo(c.getLocalizacao()) < dist
                    && v.getDisponivel())
                maisBarato = v;
            else if (maisBarato != null && v.getPrecoKm() < maisBarato.getPrecoKm()
                    && v.getLocalizacao().distanceTo(c.getLocalizacao()) < dist
                    && v.getDisponivel())
                maisBarato = v;
        }
        return maisBarato;
    }
}

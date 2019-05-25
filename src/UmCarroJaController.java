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
                case 1: while (errado < 5) {
                            password = this.view.passwordLogin();
                            if (this.model.checkProprietarioPassword(nif, password)){
                                password = null;
                                proprietarioControl();
                            }else{
                                errado++;
                                this.view.wrongPassword(errado);
                                if (errado == 5) {
                                    this.view.wrongPassword();
                                    exit(0);
                                }
                            }
                        }
                        break;
                case 2: while (errado < 5) {
                            password = this.view.passwordLogin();
                            if (this.model.checkClientePassword(nif, password)) {
                                password = null;
                                clienteControl();
                            }else{
                                errado++;
                                this.view.wrongPassword(errado);
                                if (errado == 5) {
                                    this.view.wrongPassword();
                                    exit(0);
                                }
                            }
                        }
                        break;
                case 3: exit(0);
                case 0: int op = this.view.wrongUser();
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
                case 8: this.view.getPerfil(this.model.getProprietario(nif));
                        break;
                case 9: String nifCheck = this.view.getNif();
                        this.view.getPerfil(this.model.getCliente(nifCheck));
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
                case 1: Veiculo maisProx = this.model.getVeiculoMaisProximo(nif);
                        this.view.printVeiculo(maisProx,
                        maisProx.getLocalizacao().distanceTo(
                                this.model.getCliente(nif).getLocalizacao()
                        ));
                        break;
                case 2: Veiculo maisBar = this.model.getVeiculoMaisBarato();
                        this.view.printVeiculo(maisBar,
                        maisBar.getLocalizacao().distanceTo(
                                this.model.getCliente(nif).getLocalizacao()
                        ));
                        break;
                case 3:
                        double distancia = this.view.getDistancia();
                        Veiculo maisBaratDentroDist = this.model.getVeiculoMaisBarato(nif,distancia);
                        //confirmar que o veiculo nao é null
                        this.view.printVeiculo(maisBaratDentroDist,
                            maisBaratDentroDist.getLocalizacao().distanceTo(
                                    this.model.getCliente(nif).getLocalizacao()
                            ));
                        break;
                case 4: String tipo = this.view.getTipo();
                        List<Veiculo> listaDeCarros = this.model.getListaVeiculos(tipo);
                        this.view.printListaVeiculos(listaDeCarros, this.model.getCliente(nif).getLocalizacao());
                        break;
                case 5: int aut = this.view.getAutonomia();
                        List<Veiculo> veiculosPossiveis = this.model.getListaVeiculos(aut);
                        this.view.printListaVeiculos(veiculosPossiveis, this.model.getCliente(nif).getLocalizacao());
                        break;
                case 6: String matricula = this.view.getMatricula();
                        Ponto<Double> destino =  this.view.getDestino();
                        int queue = this.model.fazerPedido(nif, matricula, destino);
                        out.println("Está na posição " + queue + " na lista de espera");
                        break;
                case 7: Pedido p = this.model.getPedido(nif);
                        if(p.getEstado() == 2)                        {
                            this.view.pedidoRejeitado();
                            this.model.pedidoRejeitado(p);
                        }else if(p.getEstado() == 1){
                            this.view.pedidoAceite();
                            this.model.pedidoAceite(p); //efetua aluguer
                            this.model.classifica(p.getVeiculo().getNifProp(), this.view.getAvaliacaoProprietario());
                            this.model.classifica(p.getVeiculo().getMatricula(), this.view.getAvaliacaoVeiculo());
                        }else{
                            this.view.pedidoPendente();
                        }
                        break;
                case 8: this.view.getPerfil(this.model.getCliente(nif));
                        break;
                case 9: String nifCheck = this.view.getNif();
                        this.view.getPerfil(this.model.getProprietario(nifCheck));
                        break;
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


}

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Cliente extends UnicastRemoteObject implements ClienteItf{

	public ClienteFrame clienteFrame;
	ServerItf Server;
	Registry registry;
	String endereco;

	public Cliente(String enderecoServidor, ClienteFrame frame) throws RemoteException {
		super();
		this.setClienteFrame(frame);
		this.endereco = enderecoServidor;
		//System.out.println("Cliente criado!");
		try {
			registry = LocateRegistry.getRegistry(enderecoServidor,1099);
			//System.out.println("Referência do Servidor de Nomes adquirida!");
			Server = (ServerItf)registry.lookup("ServerRef");

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public ClienteFrame getClienteFrame() {
		return clienteFrame;
	}

	public void setClienteFrame(ClienteFrame clienteFrame) {
		this.clienteFrame = clienteFrame;
	}

	/**
	 * Método que recebe as informações do chat.
	 */
	@Override
	public void escutaServidorChat(String msg) throws RemoteException {
		/**
		 * Quando um dos jogadores fecha a janela, o jogo não poderá mais ser realizado,
		 * por isso desabilito os botões do jogo.
		 */

		if(msg.equals("O Jogador 1 fechou a janela do jogo!") || msg.equals("O Jogador 2 fechou a janela do jogo!")){

			clienteFrame.botaoIniciar.setEnabled(false);
			clienteFrame.botaoReiniciar.setEnabled(false);
			clienteFrame.desabilitarBotoes();
		}

		clienteFrame.textoRecebido.append(msg + "\n");
		clienteFrame.textoRecebido.setCaretPosition(clienteFrame.textoRecebido.getText().length()-1);
	}

	/**
	 * Método que mistura as peças do tabluleiro.
	 */
	@Override
	public void misturarPecasCliente(int indicePeca) throws RemoteException {
		Collections.swap(clienteFrame.pecasTabuleiro, 0, indicePeca);
	}

	/**
	 * Método que vira as peças de acordo com o índice recebido.
	 */
	@Override
	public void virarPecasCliente(int indicePeca) throws RemoteException {

		ClienteFrame.MyButton btn = (ClienteFrame.MyButton) clienteFrame.tabuleiro.getComponent(indicePeca);

		btn.setVisible(false);
		clienteFrame.tabuleiro.remove(indicePeca);
		btn = clienteFrame.pecasTabuleiro.get(indicePeca);
		btn.hasImage = true;
		btn.beenDisabled = false;
		btn.setVisible(true);
		btn.putClientProperty("indice", new Point(indicePeca, 0));
		btn.setBorder(BorderFactory.createLineBorder(Color.gray));
		clienteFrame.tabuleiro.add(btn,indicePeca);	
		clienteFrame.tabuleiro.validate();
	}

	/**
	 * Método que desvira as peças de acordo com o índice recebido.
	 */
	@Override
	public void desvirarPecasCliente(int indicePeca) throws RemoteException {

		ClienteFrame.MyButton btn1 = (ClienteFrame.MyButton) clienteFrame.tabuleiro.getComponent(indicePeca);

		btn1.setVisible(false);
		clienteFrame.tabuleiro.remove(indicePeca);
		btn1 = clienteFrame.new MyButton();
		btn1.hasImage = false;
		btn1.beenDisabled = false;
		btn1.setVisible(true);
		btn1.putClientProperty("indice", new Point(indicePeca, 0));
		btn1.setBorder(BorderFactory.createLineBorder(Color.gray));
		clienteFrame.tabuleiro.add(btn1, indicePeca);
		clienteFrame.tabuleiro.validate();
	}

	/**
	 * Método que exclui as peças de acordo com o índice recebido.
	 */
	@Override
	public void excluirPecasCliente(int indicePeca) throws RemoteException {
		ClienteFrame.MyButton btn = (ClienteFrame.MyButton) clienteFrame.tabuleiro.getComponent(indicePeca);

		btn.setVisible(false);
		clienteFrame.tabuleiro.remove(indicePeca);

		ImageIcon iv = new ImageIcon(getClass().getResource("/Vazio.png"));
		Image imagemVazio = iv.getImage();
		btn = clienteFrame.new MyButton(imagemVazio);

		btn.putClientProperty("indice", new Point(indicePeca, 0));
		btn.setBorder(BorderFactory.createLineBorder(Color.gray));
		btn.setEnabled(false);
		clienteFrame.tabuleiro.add(btn, indicePeca);
		clienteFrame.tabuleiro.validate();

		btn.beenDisabled = true;
		clienteFrame.pecasTabuleiro.get(indicePeca).beenDisabled = true;

		if(clienteFrame.minhaVez.equals("Jogador1"))
			clienteFrame.placar1++;

		else if(clienteFrame.minhaVez.equals("Jogador2"))
			clienteFrame.placar2++;
	}

	/**
	 * Método que recebe o valor da soma dos dados e atualiza o respectivo campo.
	 */
	@Override
	public void jogarDadosCliente(int valorDado) throws RemoteException {
		clienteFrame.valorDados.setText(Integer.toString(valorDado));		
	}

	/**
	 * Método chamado quando uma jogada é finalizada.
	 * Recebe o índice do jogador que finalizou a jogada e também o seu placar
	 * para o que mesmo possa ser atualizado.
	 */
	@Override
	public void finalizarJogadaCliente(int indiceJogador, int placar) throws RemoteException {

		String pontuacao =  Integer.toString(placar);

		/**
		 * A string minhaVez é umas das variáveis que ajudam no controle de turno da partida
		 * e também serve para informar o jogador atual.
		 */
		if(indiceJogador == 1){
			clienteFrame.minhaVez = "Jogador2";
			clienteFrame.jogadorAtual.setText(clienteFrame.minhaVez);
			clienteFrame.placarJogador1.setText(pontuacao);
		}

		if(indiceJogador == 2){
			clienteFrame.minhaVez = "Jogador1";
			clienteFrame.jogadorAtual.setText(clienteFrame.minhaVez);
			clienteFrame.placarJogador2.setText(pontuacao);
		}

		/**
		 * Configuração para o controle de turno.
		 */
		if(!clienteFrame.nomeJogador.equals(clienteFrame.minhaVez)){
			clienteFrame.desabilitarBotoes();
		}

		if(clienteFrame.nomeJogador.equals(clienteFrame.minhaVez)){
			clienteFrame.habilitarBotoes();
		}

		/**
		 * Verificação das peças que não formaram palavra ao finalizar uma jogada.
		 * Caso haja alguma peça que o Jogador esqueceu de desvirar, essa é desvirada
		 * ao finalizar a jogada.
		 */
		for(int i = 0; i < 64 ; i++){

			ClienteFrame.MyButton btn = (ClienteFrame.MyButton) clienteFrame.tabuleiro.getComponent(i);

			if(btn.hasImage == true && btn.beenDisabled == false){

				Server.desvirarPecasServidor(i);
			}
		}

		/**
		 * Verificação se ainda existem peças a serem desviradas.
		 * Caso não haja mais peças, os placares dos jogadores são verificados e
		 * o jogador vencedor é informado.
		 */
		clienteFrame.verificarTermino();

		/**
		 * Variáveis que também ajudam no controle de turno da partida.
		 * numDados é zerado para evitar das jogadas atuais com as anteriores.
		 * numDesv é zerado para evitar que o jogador oponente desvire as peças do jogador atual.
		 */
		clienteFrame.numDados = 0;
		clienteFrame.numDesv = 0;

	}

	/**
	 * Método chamado quando o jogo é iniciado.
	 * Nesse método é setada uma variável(minhavez) que ajuda no controle de fluxo do jogo.
	 */
	@Override
	public void iniciarJogoCliente(int indiceJogador) throws RemoteException {

		clienteFrame.botaoIniciar.setEnabled(false);
		clienteFrame.botaoReiniciar.setEnabled(false);

		/**
		 * Variável minhaVez ajudando novamente no controle de fluxo.
		 */
		if(indiceJogador == 1){
			clienteFrame.minhaVez = "Jogador1";
			clienteFrame.jogadorAtual.setText(clienteFrame.minhaVez);
		}

		if(indiceJogador == 2){
			clienteFrame.minhaVez = "Jogador2";
			clienteFrame.jogadorAtual.setText(clienteFrame.minhaVez);
		}

		if(!clienteFrame.nomeJogador.equals(clienteFrame.minhaVez)){
			clienteFrame.desabilitarBotoes();
		}

		if(clienteFrame.nomeJogador.equals(clienteFrame.minhaVez)){
			clienteFrame.habilitarBotoes();
		}

		clienteFrame.botaoIniciar.setEnabled(false);
		clienteFrame.gameStarted = true;
	}

	/**
	 * Método chamado quando há um vencedor.
	 * Esse método informa os jogadores com mensagens no chat.
	 */
	@Override
	public void informarVencedorCliente(int indiceJogador) throws RemoteException {

		if(indiceJogador == 1){
			informarVencedorMensagem("Fim do Jogo!\nJogador 1 é o Vencedor!" + "\n");
		}
		else if(indiceJogador == 2){
			informarVencedorMensagem("Fim do Jogo!\nJogador 2 é o Vencedor!" + "\n");
		}
		else if(indiceJogador == 3){
			informarVencedorMensagem("Fim do Jogo!\nOcorreu Empate!" + "\n");
		}

		/**
		 * Quando alguém termina ou desiste da partida, desabilito os botões finalizar jogada
		 *  e jogar dados e habilito o botão iniciar.
		 */
		clienteFrame.botaoReiniciar.setEnabled(true);
		clienteFrame.gameStarted = false;
	}

	/**
	 * Método que recebe a mensagem do vencedor e a exibe no espaço reservado ao chat. 
	 */
	public void informarVencedorMensagem(String msg){
		clienteFrame.textoRecebido.append(msg);
		clienteFrame.textoRecebido.setCaretPosition(clienteFrame.textoRecebido.getText().length()-1);
		clienteFrame.desabilitarBotoes();
	}
	
	/**
	 * Método chamado quando o jogo é iniciado.
	 */
	@Override
	public void reiniciarJogoCliente(String msg) throws RemoteException {

		if(msg.equals("Reiniciar Jogo"))
			clienteFrame.reiniciarJogo();
		
		clienteFrame.textoRecebido.setText("");
		clienteFrame.gameStarted = false;
	}

	public static void main(String[] args) {
		ServerItf Server;
		Registry registry;

		String enderecoServidor = null;
		String entrada = null;

		entrada = JOptionPane.showInputDialog("Informe o endereço do Servidor de Nomes!");
		//Se o botão cancelar for clicado.
		if(entrada == null){
			System.exit(0);
		}

		//Validação do endereço recebido.
		// O formato do endereçamento é o IPV4.
		else if(entrada.equals("localhost") || entrada.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}"))
			enderecoServidor = entrada;

		else{
			JOptionPane.showMessageDialog(null, "Endereço inválido");
			System.exit(0);
		}

		/**
		 * Pego a referência do Servidor para olhar a sua lista de escritores.
		 * De acordo com a resposta, vejo quem será o jogador 1 e o jogador 2.
		 */
		try {
			registry = LocateRegistry.getRegistry(enderecoServidor,1099);
			Server = (ServerItf)registry.lookup("ServerRef");

			if(Server.tamanhoListaEscritores() == 0){
				ClienteFrame frame = new ClienteFrame();
				frame.endereco = enderecoServidor;
				frame.nomeJogador = "Jogador1";
				Cliente cliente = new Cliente(enderecoServidor,frame);

				registry.rebind("ClienteRef1", cliente);
				Server.escutarClienteChat(enderecoServidor,"Cliente Registrado");
			}

			else if(Server.tamanhoListaEscritores() == 1){
				ClienteFrame frame = new ClienteFrame();
				frame.endereco = enderecoServidor;
				frame.nomeJogador = "Jogador2";
				Cliente cliente = new Cliente(enderecoServidor,frame);

				registry.rebind("ClienteRef2", cliente);
				Server.escutarClienteChat(enderecoServidor,"Cliente Registrado");
			}
			else if(Server.tamanhoListaEscritores() == 2){
				JOptionPane.showMessageDialog(null, "O jogo já possui 2 jogadores!");
			}

		} catch (Exception e) {
			/**
			 * Envia uma mensagem para o jogador caso a conexão não seja estabelecida.
			 * Essa resposta pode demorar aproximadamente 2 minutos.
			 */
			JOptionPane.showMessageDialog(null, "Conexão Recusada!");
			//System.err.println(e + " Conexão Recusada");
		}
	}
}

/**
 * @author Ytallo de Lima Gadelha
 * Projeto Vira Letras
 * Versão 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ClienteFrame extends JFrame {

	public JButton botaoEnviar,botaoIniciar,botaoReiniciar,
	botaoTerminar,botaoDesistir,botaoFinalizarJogada,botaoJogarDados;
	public JTextField jogadorAtual,valorDados,textoParaEnviar,jogador1,
	jogador2,placarJogador1,placarJogador2;
	public String nomeJogador = null;
	public JTextArea textoRecebido;
	public JScrollPane scrollTextoRecebido;
	public ArrayList<MyButton> pecasTabuleiro = new ArrayList<>();
	public Container tabuleiro;
	public int numDados = 0;
	public int numDesv = 0;
	public String minhaVez;
	public int placar1 = 0;
	public int placar2 = 0;
	public ServerItf Server;
	public String endereco = null;
	public boolean gameStarted = false;

	public ClienteFrame(){

		super();
		Registry registry;

		try {
			registry = LocateRegistry.getRegistry(endereco,1099);
			Server = (ServerItf)registry.lookup("ServerRef");
			System.out.println("Servidor Localizado!");

		} catch (Exception e) {
			System.err.println(e);
		}

		/**
		 * Configuração de uma fonte
		 */
		Font fonte = new Font("Serif", Font.PLAIN, 30);

		/**
		 * Configuração do botão enviar
		 */
		botaoEnviar = new JButton("Enviar");
		botaoEnviar.addActionListener(new EnviarListenerChat());
		configurarBotao(botaoEnviar);
		botaoEnviar.setFont(new Font("Serif", Font.PLAIN, 26));


		/**
		 * Configuração do botão iniciar partida
		 */
		botaoIniciar = new JButton("Iniciar");
		botaoIniciar.addActionListener(new BotaoIniciarListener());
		configurarBotao(botaoIniciar);

		/**
		 * Configuração do botão reiniciar partida
		 */
		botaoReiniciar = new JButton("Reiniciar");
		botaoReiniciar.addActionListener(new BotaoReiniciarListerner());
		configurarBotao(botaoReiniciar);

		/**
		 * Configuração do botão terminar partida
		 */
		botaoTerminar = new JButton("Terminar");
		botaoTerminar.addActionListener(new BotaoTerminarListener());
		configurarBotao(botaoTerminar);

		/**
		 * Configuração do botão desistir da partida
		 */
		botaoDesistir = new JButton("Desistir");
		botaoDesistir.addActionListener(new BotaoDesistirListener());
		configurarBotao(botaoDesistir);

		/**
		 * Configuração do botão finalizar jogada
		 */
		botaoFinalizarJogada = new JButton("Finalizar Jogada");
		botaoFinalizarJogada.addActionListener(new BotaoFinalizarJogadaListener());
		configurarBotao(botaoFinalizarJogada);

		/**
		 * Configuração do botão jogar dados
		 */
		botaoJogarDados = new JButton("Jogar Dados");
		botaoJogarDados.addActionListener(new BotaoJogarDadosListener());
		configurarBotao(botaoJogarDados);

		botaoIniciar.setEnabled(true);
		botaoReiniciar.setEnabled(false);
		desabilitarBotoes();

		/**
		 * Configuração do textField do jogador atual
		 */
		jogadorAtual = new JTextField("Jogador Atual");
		configurarTextField(jogadorAtual);

		/**
		 * Configuração do textField que contém o valor dos dados
		 */
		valorDados = new JTextField("Valor dos Dados");
		configurarTextField(valorDados);

		/**
		 * Configuração do textField do jogador 1
		 */
		jogador1 = new JTextField("Jogador 1: ");
		configurarTextField(jogador1);

		/**
		 * Configuração do textField do jogador 2
		 */
		jogador2 = new JTextField("Jogador 2:");
		configurarTextField(jogador2);

		/**
		 * Configuração do textField do placar do jogador 1
		 */
		placarJogador1 = new JTextField("0");
		configurarTextField(placarJogador1);

		/**
		 * Configuração do textField do placar do jogador 2
		 */
		placarJogador2 = new JTextField("0");
		configurarTextField(placarJogador2);

		/**
		 * Configuração do textField composto pelas informações a serem enviadas pelo chat
		 */
		textoParaEnviar = new JTextField();
		textoParaEnviar.setBackground(new Color(255, 250, 240));
		textoParaEnviar.setFont(fonte);

		/**
		 * Configurando a textArea composta pelas informações do chat vindas do servidor
		 */
		textoRecebido = new JTextArea();
		textoRecebido.setBackground(new Color(255, 250, 240));
		textoRecebido.setLineWrap(true);
		textoRecebido.setFont(new Font("Serif", Font.PLAIN, 20));
		textoRecebido.setEditable(false);

		/**
		 * Configuração do scroll composto pela textArea textoRecebido
		 */
		scrollTextoRecebido = new JScrollPane(textoRecebido);
		scrollTextoRecebido.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollTextoRecebido.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTextoRecebido.setPreferredSize(new Dimension(scrollTextoRecebido.getWidth(), 120));

		/**
		 * Container com os botões principais do jogo
		 */
		Container botoes = new JPanel();
		botoes.setBackground(new Color(100, 149, 237));
		((JComponent) botoes).setBorder(BorderFactory.createLineBorder(Color.gray));
		botoes.setLayout(new GridLayout(3,4,4,4));
		botoes.add(botaoIniciar,new Point(0,0));
		botoes.add(botaoReiniciar,new Point(0,1));
		botoes.add(botaoDesistir, new Point(0,2));
		botoes.add(botaoTerminar, new Point(0,3));
		botoes.add(jogadorAtual, new Point(1,0));
		botoes.add(botaoJogarDados, new Point(1,1));
		botoes.add(valorDados, new Point(1,2));
		botoes.add(botaoFinalizarJogada, new Point(1,3));
		botoes.add(jogador1, new Point(2,0));
		botoes.add(placarJogador1, new Point(2,1));
		botoes.add(jogador2, new Point(2,2));
		botoes.add(placarJogador2, new Point(2,3));
		getContentPane().add(BorderLayout.NORTH, botoes);

		/**
		 * Container com as peças do tabuleiro
		 */
		tabuleiro = new JPanel();
		((JComponent) tabuleiro).setBorder(new LineBorder(new Color(128, 128, 128), 1, true));
		tabuleiro.setLayout(new GridLayout(8, 8, 2, 2));
		tabuleiro.setBackground(Color.white);

		/**
		 * Carregamento as imagens de vogais e consoantes nos botões e adiciona no arrayList pecasTabuleiro
		 */
		carregarImagensVogais();
		carregarImagensConsoante();

		/**
		 * Inclusão de peças no tabuleiro(peças vazias)
		 */
		iniciarTabuleiro();

		/**
		 * Adição do tabuleiro no container principal
		 */
		getContentPane().add(BorderLayout.CENTER, tabuleiro);

		/**
		 * Container que possui apenas o textField e botaoEnviar
		 */
		Container envioTextFieldBotao = new JPanel();
		envioTextFieldBotao.setLayout(new BorderLayout());
		envioTextFieldBotao.add(BorderLayout.CENTER,textoParaEnviar);
		envioTextFieldBotao.add(BorderLayout.EAST,botaoEnviar);

		/**
		 * Container que possui todos os componetes de envio(scroll,textField e botaoEnviar)
		 */
		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, scrollTextoRecebido);
		envio.add(BorderLayout.SOUTH, envioTextFieldBotao);

		/**
		 * Adição do container Envio no container principal
		 */
		getContentPane().add(BorderLayout.SOUTH, envio);

		/**
		 * Configurações da Janela
		 */
		setSize(680, 720);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter());
	}


	class WindowAdapter implements WindowListener{

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowOpened(WindowEvent e) {}

		/**
		 * Método chamado quando o usuário fecha a janela.
		 * Esse método é utilizado para que o fechamento da janela seja tratado 
		 * como uma desistência do usuário que provocou o fechamento.
		 * Obs: Só será tratado com desistência se o jogo já tiver sido iniciado.
		 * Caso o jogo ainda não tenha sido iniciado, ocorrerá simplesmente o fechamento da janela.
		 */
		@Override
		public void windowClosing(WindowEvent arg0) {
			/**
			 * Verifico se o jogo já foi iniciado e se a partida já está acontecendo. 
			 * Caso sim, qualquer tentativa de
			 * fechamento da janela, será tratada como desistência.
			 */
			if(botaoIniciar.isEnabled() == false && gameStarted == true){
				if (!(JOptionPane.showConfirmDialog(null, "Deseja mesmo encerrar o jogo?") == JOptionPane.OK_OPTION)) {
					return;
				}
				else{

					/**
					 * Se jogador 1 desistiu, então jogador 2 é o vencedor
					 */
					if(nomeJogador.equals("Jogador1")){
						try {
							Server.desistirJogoServidor(2);
							Server.encaminharParaTodosChat("O Jogador 1 fechou a janela do jogo!");
						} catch (IOException e) {
							System.err.println(e+" jogador 1 fechou a janela");
						}
					}

					/**
					 * Se jogador 2 desistiu, então jogador 1 é o vencedor
					 */
					else if(nomeJogador.equals("Jogador2")){
						try {
							Server.desistirJogoServidor(1);
							Server.encaminharParaTodosChat("O Jogador 2 fechou a janela do jogo!");
						} catch (IOException e) {
							System.err.println(e+" jogador 2 fechou a janela");
						}
					}
				}
			}

			/**
			 * Caso a partida já tenha sido encerrada e algum jogador feche a janela, um aviso 
			 * identificando esse jogador será informado.
			 */
			else if(gameStarted == false){
				if(nomeJogador.equals("Jogador1")){
					try {
						Server.encaminharParaTodosChat("O Jogador 1 fechou a janela do jogo!");
					} catch (IOException e) {
						System.err.println(e+" jogador 1 fechou a janela");
					}
				}

				else if(nomeJogador.equals("Jogador2")){
					try {
						Server.encaminharParaTodosChat("O Jogador 2 fechou a janela do jogo!");
					} catch (IOException e) {
						System.err.println(e+" jogador 2 fechou a janela");
					}
				}

			}
			System.exit(0);
		}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowClosed(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowIconified(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowDeiconified(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowActivated(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowDeactivated(WindowEvent e) {}
	}

	/**
	 * Método que carrega as imagens das vogais para o ArrayList pecasTabuleiro
	 */
	public void carregarImagensVogais(){

		for(int i=0; i<7; i++){
			carregarLetra("/Letra A.png");
		}

		for(int i=0; i<7; i++){
			carregarLetra("/Letra E.png");
		}

		for(int i=0; i<6; i++){
			carregarLetra("/Letra I.png");
		}

		for(int i=0; i<7; i++){
			carregarLetra("/Letra O.png");
		}

		for(int i=0; i<4; i++){
			carregarLetra("/Letra U.png");
		}
	}

	public void carregarLetra(String nomeLetra){
		ImageIcon ii;
		Image imagemLetra;
		MyButton button;

		ii = new ImageIcon(getClass().getResource(nomeLetra));
		imagemLetra = ii.getImage();
		button = new MyButton(imagemLetra);
		pecasTabuleiro.add(button);
	}

	public void configurarBotao(JButton botao){

		botao.setBackground(new Color(255, 255, 255));
		botao.setForeground(new Color(31, 58, 147));
		botao.setFont(new Font("Serif", Font.PLAIN, 20));
	}

	public void configurarTextField(JTextField textField){

		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setEditable(false);
		textField.setBackground(new Color(255, 255, 255));
		textField.setForeground(Color.DARK_GRAY);
		textField.setFont(new Font("Serif", Font.PLAIN, 20));
	}

	/**
	 * Método que carrega as imagens das consoantes para o ArrayList pecasTabuleiro
	 */
	public void carregarImagensConsoante(){

		for(int i=0; i<2; i++){
			carregarLetra("/Letra B.png");
		}

		for(int i=0; i<2; i++){
			carregarLetra("/Letra C.png");
		}

		for(int i=0; i<2; i++){
			carregarLetra("/Letra D.png");
		}

		carregarLetra("/Letra F.png");

		carregarLetra("/Letra G.png");

		carregarLetra("/Letra H.png");

		carregarLetra("/Letra J.png");


		for(int i=0; i<3; i++){
			carregarLetra("/Letra L.png");
		}

		for(int i=0; i<2; i++){
			carregarLetra("/Letra M.png");
		}

		for(int i=0; i<2; i++){
			carregarLetra("/Letra N.png");
		}

		for(int i=0; i<2; i++){
			carregarLetra("/Letra P.png");
		}

		carregarLetra("/Letra Qu.png");

		for(int i=0; i<3; i++){
			carregarLetra("/Letra R.png");
		}


		for(int i=0; i<4; i++){
			carregarLetra("/Letra S.png");
		}

		for(int i=0; i<2; i++){
			carregarLetra("/Letra T.png");
		}

		for(int i=0; i<2; i++){
			carregarLetra("/Letra V.png");
		}

		carregarLetra("/Letra X.png");

		carregarLetra("/Letra Z.png");
	}


	/**
	 * Método chamado quando o botão Jogar Dados é clicado.
	 * Nesse caso, o valor relativo a soma dos 2 dados é enviado ao servidor.
	 */
	public class BotaoJogarDadosListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			/**
			 * Verifico se o jogo já foi iniciado. Se sim, o jogador atual pode jogar os dados.
			 */
			int dado1,dado2;
			Random gerador = new Random();
			dado1 = gerador.nextInt(6)+1;
			dado2 = gerador.nextInt(6)+1;
			numDados = dado1 + dado2;
			numDesv = numDados;
			botaoJogarDados.setEnabled(false);

			try {
				Server.jogarDadosServidor(numDados);
			} catch (IOException f) {
				try {
					Server.encaminharParaTodosChat("O outro jogador saiu!!!");
					System.err.println(f+"Botão jogar dados");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}

	/**
	 * Método chamado quando o botão Finalizar Jogada é clicado.
	 * Nesse caso, a informação de quem finalizou a jogada e o seu placar são enviados ao servidor.
	 */
	public class BotaoFinalizarJogadaListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			/**
			 * Verifico se o jogo já foi iniciado. Se sim, o jogador atual pode finalizar a jogada.
			 */
			botaoJogarDados.setEnabled(true);
			if(nomeJogador.equals("Jogador1")){
				try {
					Server.finalizarJogadaServidor(1, placar1);
				} catch (Exception a) {
					System.err.println(a+"Finaliza jogada: Jogador1");
				}
			}
			else if(nomeJogador.equals("Jogador2")){
				try {
					Server.finalizarJogadaServidor(2, placar2);
				} catch (Exception b) {
					System.err.println(b+"Finaliza jogada: Jogador2");
				}
			}
		}
	}

	/**
	 * Método chamado quando o botão Iniciar é clicado.
	 * Nesse caso, a informação de quem iniciou o jogo é enviada ao servidor.
	 * Ao iniciar o jogo, também é enviado o índice para misturar as peças do tabuleiro.
	 */
	public class BotaoIniciarListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			//Verifica se o jogo já possui 2 jogadores
			try {
				if(Server.tamanhoListaEscritores() == 2){
					if(nomeJogador.equals("Jogador1")){
						try {
							Server.iniciarJogoServidor(1);
						} catch (IOException a) {
							System.err.println(a+"Botão iniciar: Jogador1");
						}
					}
					else if(nomeJogador.equals("Jogador2")){
						try {
							Server.iniciarJogoServidor(2);
						} catch (IOException b) {
							System.err.println(b+"Botão iniciar: Jogador2");
						}
					}
				}
			} catch (RemoteException e1) {
				System.out.println(e1);
			}

			/**
			 * Envio do índice necessário para misturar as peças.
			 */
			try {
				Random gerador = new Random();
				int ind;

				for(int i = 0; i < 70; i++){
					ind = gerador.nextInt(63)+1;
					Server.misturarPecasServidor(ind);
				}

			} catch (Exception c) {
				System.err.println(c+"Botão iniciar: misturando as peças do tabuleiro");
			}
		}
	}

	/**
	 * Método chamado quando o botão Desistir é clicado.
	 * Nesse caso, a informação de quem será beneficiado com a desistência,
	 * ou seja, aquele que ganhará a partida é enviada ao servidor.
	 */
	public class BotaoDesistirListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			/**
			 * Verifico se o jogo já foi iniciado. Se sim, o Jogadoratual pode desistir da partida.
			 */
			/**
			 * Se Jogador1 desistiu, então Jogador2 é o vencedor.
			 */
			if(nomeJogador.equals("Jogador1")){
				try {
					Server.desistirJogoServidor(2);
				} catch (IOException a) {
					System.err.println(a+"Botão desistir Jogador1");
				}
			}

			/**
			 * Se Jogador2 desistiu, então Jogador1 é o vencedor
			 */
			else if(nomeJogador.equals("Jogador2")){
				try {
					Server.desistirJogoServidor(1);
				} catch (IOException b) {
					System.err.println(b+"Botão desistir Jogador2");
				}
			}
		}
	}

	/**
	 * Método chamado quando o botão Terminar é clicado.
	 * Nesse caso, a informação de quem vencerá a partida é enviada ao servidor.
	 */
	public class BotaoTerminarListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			/**
			 * Verifico se o jogo já foi iniciado. Se sim, o Jogadoratual pode terminar a jogada
			 */
			int placar1 = Integer.parseInt(placarJogador1.getText());
			int placar2 = Integer.parseInt(placarJogador2.getText());

			if(placar1 > placar2){
				try {
					Server.terminarJogoServidor(1);
				} catch (IOException a) {
					System.err.println(a+"Botão terminar: 1º venceu");
				}
			}

			else if(placar2 > placar1){

				try {
					Server.terminarJogoServidor(2);
				} catch (IOException b) {
					System.err.println(b+"Botão terminar: 2º venceu");
				}
			}

			else if(placar1 == placar2){

				try {
					Server.terminarJogoServidor(3);
				} catch (IOException c) {
					System.err.println(c+"Botão terminar: Empate");
				}
			}
		}
	}

	/**
	 * Método chamado quando o botão Reiniciar é clicado.
	 * Nesse caso, o comando informando o reinício da partida é enviado ao servidor.
	 */	
	public class BotaoReiniciarListerner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			/**
			 * Verifico se o jogo já foi iniciado. Se sim, o Jogadoratual pode reiniciar a jogada
			 */
			try {
				Server.reiniciarJogoServidor("Reiniciar Jogo");
			} catch (Exception a) {
				System.err.println(a+"Botão reiniciar");
			}
		}
	}

	/**
	 * Método que envia as informações do chat para o servidor
	 */
	public class EnviarListenerChat implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			try{
				String textoEnviado = textoParaEnviar.getText();

				if(!textoEnviado.isEmpty() && !textoEnviado.trim().isEmpty()){
					Server.encaminharParaTodosChat(nomeJogador + ": " + textoParaEnviar.getText());
					System.out.println(nomeJogador + ": " + textoParaEnviar.getText());
					textoParaEnviar.setText("");
					textoParaEnviar.requestFocus();
				}

			}catch(Exception b){
				System.err.println(b);
			}
		}
	} 

	/**
	 * Método que verificará se a partida terminou após a finalização de cada jogada.
	 */
	public void verificarTermino(){

		int placar1 = Integer.parseInt(placarJogador1.getText());
		int placar2 = Integer.parseInt(placarJogador2.getText());

		//OBS: O dado que é passado ao método desistirJogoServidor é o índice de quem venceu a partida. 
		if(placar1+placar2 == pecasTabuleiro.size()){

			if(placar1 > placar2){
				try {
					Server.desistirJogoServidor(1);
				} catch (IOException e) {
					System.err.println(e+"Botão terminar: 1º venceu");
				}
			}

			else if(placar2 > placar1){

				try {
					Server.desistirJogoServidor(2);
				} catch (IOException e) {
					System.err.println(e+"Botão terminar: 2º venceu");
				}
			}

			else if(placar1 == placar2){

				try {
					Server.desistirJogoServidor(3);
				} catch (IOException e) {
					System.err.println(e+"Botão terminar: Empate");
				}
			}
		}
	}

	/**
	 * Método que reiniciará o jogo quando necessário.
	 */
	public void reiniciarJogo(){

		placar1 = 0;
		placar2 = 0;
		botaoIniciar.setEnabled(true);
		botaoReiniciar.setEnabled(false);
		desabilitarBotoes();
		numDados = 0;
		numDesv = 0;
		jogadorAtual.setText("Jogador Atual");
		valorDados.setText("Valor dos Dados");
		placarJogador1.setText("0");
		placarJogador2.setText("0");
		tabuleiro.removeAll();
		tabuleiro.validate();
		for (MyButton myButton : pecasTabuleiro) {
			myButton.beenDisabled = false;
		}
		iniciarTabuleiro();

	}

	/**
	 * Método que inicia o tabuleiro com peças vazias
	 */
	public void iniciarTabuleiro(){

		for (int i = 0; i < 64; i++) {
			MyButton btn = new MyButton();
			btn.setBorder(BorderFactory.createLineBorder(Color.gray));
			btn.putClientProperty("indice", new Point(i, 0));
			btn.hasImage = false;
			btn.beenDisabled = false;
			tabuleiro.add(btn);
		}
		tabuleiro.validate();
	}

	/**
	 * Método que desabilita os botões que julguei necessário no controle de turno.
	 */
	public void desabilitarBotoes(){
		botaoJogarDados.setEnabled(false);
		botaoFinalizarJogada.setEnabled(false);
		botaoDesistir.setEnabled(false);
		botaoTerminar.setEnabled(false);
	}

	/**
	 * Método que habilita os botões que julguei necessário no controle de turno.
	 */
	public void habilitarBotoes(){
		botaoJogarDados.setEnabled(true);
		botaoFinalizarJogada.setEnabled(true);
		botaoDesistir.setEnabled(true);
		botaoTerminar.setEnabled(true);
	}

	/**
	 * Classe personalizada que possui as configurações de um botão(MyButton)
	 */
	class MyButton extends JButton {

		boolean hasImage = false;
		boolean beenDisabled = false;

		public MyButton() {

			super();
			initUI();
		}

		public MyButton(Image image) {

			super(new ImageIcon(image));

			initUI();
		}

		private void initUI() {

			BorderFactory.createLineBorder(Color.gray);

			addMouseListener(new MouseAdapter()); 
		}

		class MouseAdapter implements MouseListener{

			long tempoInicial, tempoFinal, resultado;

			/**
			 * Método chamado quando uma peça é clicada
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				MyButton btn = (MyButton)e.getSource();
				Point p = (Point) btn.getClientProperty("indice");
				int indice = p.x;

				/**
				 * Quando o botão possui imagem, a ação para desvirar a peça é enviada ao servidor
				 */
				if(btn.hasImage == true && btn.beenDisabled == false){

					if(numDesv > 0){
						try {
							Server.desvirarPecasServidor(indice);
						} catch (IOException e1) {
							System.err.println(e1+"Envio indice da peça a ser desvirada");
						}
						numDesv --;
					}
				}

				/**
				 * Quando o botão não possui imagem, a ação para virar a peça é enviada ao servidor
				 */
				else if(btn.hasImage == false && btn.beenDisabled == false){
					if(numDados > 0){

						try {
							Server.virarPecasServidor(indice);
						} catch (IOException e1) {
							System.err.println(e1+"Envio indice da peça a ser virada");
						}
						numDados--;
					}
				}
			}

			/**
			 * Método que deveria ser chamado quando uma peça é pressionada por um tempo determinado.
			 * Esse método é muito parecido com o método mouseClicked e é utilizado para pegar 
			 * o tempo do sistema quando uma peça é pressionada. 
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				tempoInicial = (long) System.currentTimeMillis();
			}

			/**
			 * Método chamado quando uma peça é liberada.
			 * Nesse método, pego novamente o tempo do sistema para tentar simular um long press.
			 * Com os valores obtidos, é realizada uma subtração e verificado se esse resultado
			 * é maior do que um determinado valor, no caso 300, que julguei ser equivalente a um long press.
			 */
			@Override
			public void mouseReleased(MouseEvent e) {

				MyButton btn = (MyButton)e.getSource();
				Point p = (Point) btn.getClientProperty("indice");
				int indice = p.x;
				tempoFinal = (long) System.currentTimeMillis();
				resultado = tempoFinal - tempoInicial;

				/**
				 * Caso o "long press" seja confirmado, a ação para excluir a peça é enviada ao servidor
				 */
				if(resultado >= 250 && btn.hasImage == true){

					if(numDesv > 0){
						/**
						 * É necessário mudar o estado do booleano beenDisable para 
						 * evitar a entrada no método mouseClicked e mande o comando para desvirar a peça
						 */
						btn.beenDisabled = true;
						try {
							Server.excluirPecasServidor(indice);
						} catch (IOException e1) {
							System.err.println(e1+"Envio indice da peça a ser excluída");
						}
						numDesv--;
					}
				}
			}

			/**
			 * Método não utilizado
			 */
			@Override
			public void mouseEntered(MouseEvent e) {}

			/**
			 * Método não utilizado
			 */
			@Override
			public void mouseExited(MouseEvent e) {}
		}
	}
}

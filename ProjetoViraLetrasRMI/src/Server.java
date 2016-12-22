import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class Server extends UnicastRemoteObject implements ServerItf {

	List<ClienteItf> escritores = new ArrayList<>();

	public Server() throws RemoteException{
		super();
		//System.out.println("Servidor criado!");
	}

	/**
	 * Método utilizado para mandar as mensagens referentes ao chat para todos os jogadores.
	 */
	public void encaminharParaTodosChat(String msg) throws RemoteException{

		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.escutaServidorChat(msg);
			} catch (Exception e) {
				//System.out.println(e + " encaminharParaTodosChat");
				System.out.println("Clientes não estão mais conectados, logo não será possível o envio das mensagens!");
			}
		}
	}

	/**
	 * Método utilizado para pegar as referências dos jogadores que foram registrados no 
	 * servidor de nomes e adicioná-las a lista de escritores. 
	 */
	public void escutarClienteChat(String enderecoServidor, String msg) throws RemoteException{

		Registry registry = LocateRegistry.getRegistry(enderecoServidor,1099);
		if(msg.equals("Cliente Registrado")){
			if(escritores.size() == 0){

				try {
					ClienteItf clienteRef1 = (ClienteItf)registry.lookup("ClienteRef1");
					System.out.println("Cliente Localizado!");
					escritores.add(clienteRef1);
					encaminharParaTodosChat("Jogador atual registrado!\n2º jogador ainda não foi conectado!");

				} catch (Exception e) {
					System.out.println(e + "Buscando referencia do jogador 1");
				}
			}

			else if(escritores.size() == 1){

				try {
					ClienteItf clienteRef2 = (ClienteItf)registry.lookup("ClienteRef2");
					System.out.println("Cliente Localizado!");
					escritores.add(clienteRef2);
					encaminharParaTodosChat("Jogador atual registrado!\n2º jogador conectado. Jogo pronto para ser iniciado!!!");

				} catch (Exception e) {
					System.out.println(e + "Buscando referencia do jogador 2");
					encaminharParaTodosChat("2º jogador ainda não foi conectado!");
				}
			}
		}
	}

	/**
	 * Método que informa o tamanho da lista de escritores.
	 */
	@Override
	public int tamanhoListaEscritores() throws RemoteException {
		return escritores.size();
	}

	/**
	 * Método chamado quando o botão Jogar Dados é clicado.
	 */
	@Override
	public void jogarDadosServidor(int valorDado) throws RemoteException {

		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.jogarDadosCliente(valorDado);
			} catch (Exception e) {
				System.out.println(e + " jogarDadosServidor");
			}
		}
	}

	/**
	 * Método chamado quando o botão Finalizar Jogada é clicado.
	 */
	@Override
	public void finalizarJogadaServidor(int indice, int placar) throws RemoteException {
		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.finalizarJogadaCliente(indice, placar);
			} catch (Exception e) {
				System.out.println(e + " finalizarJogadaServidor");
			}
		}
	}

	/**
	 * Método chamado quando o botão Iniciar é clicado.
	 */
	@Override
	public void iniciarJogoServidor(int indiceJogador) throws RemoteException {
		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.iniciarJogoCliente(indiceJogador);
			} catch (Exception e) {
				System.out.println(e + " iniciarJogoServidor");
			}
		}
	}

	/**
	 * Método chamado quando o botão Desistir é clicado.
	 */
	@Override
	public void desistirJogoServidor(int indiceJogador) throws RemoteException {
		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.informarVencedorCliente(indiceJogador);
			} catch (Exception e) {
				System.out.println(e + " desistirJogoServidor");
			}
		}
	}

	/**
	 * Método chamado quando o botão Terminar é clicado.
	 */
	@Override
	public void terminarJogoServidor(int indiceJogador) throws RemoteException {

		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.informarVencedorCliente(indiceJogador);
			} catch (Exception e) {
				System.out.println(e + " terminarJogoServidor");
			}
		}
	}

	/**
	 * Método chamado quando o botão Reiniciar é clicado.
	 */
	@Override
	public void reiniciarJogoServidor(String msg) throws RemoteException {

		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.reiniciarJogoCliente(msg);
			} catch (Exception e) {
				System.out.println(e + " reiniciarJogoServidor");
			}
		}
	}

	/**
	 * Método chamado quando uma peça deve ser virada.
	 */
	@Override
	public void virarPecasServidor(int indicePeca) throws RemoteException {
		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.virarPecasCliente(indicePeca);
			} catch (Exception e) {
				System.out.println(e + " virarPecasServidor");
			}
		}
	}

	/**
	 * Método chamado quando uma peça deve ser desvirada.
	 */
	@Override
	public void desvirarPecasServidor(int indicePeca) throws RemoteException {
		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.desvirarPecasCliente(indicePeca);
			} catch (Exception e) {
				System.out.println(e + " desvirarPecasServidor");
			}
		}
	}

	/**
	 * Método chamado quando uma peça deve ser excluída.
	 */
	@Override
	public void excluirPecasServidor(int indicePeca) throws RemoteException {
		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.excluirPecasCliente(indicePeca);
			} catch (Exception e) {
				System.out.println(e + " excluirPecasServidor");
			}
		}
	}

	/**
	 * Método chamado quando as peças estão sendo misturadas.
	 */
	@Override
	public void misturarPecasServidor(int indicePeca) throws RemoteException {
		for (ClienteItf clienteItf : escritores) {
			try {
				clienteItf.misturarPecasCliente(indicePeca);
			} catch (Exception e) {
				System.out.println(e + " misturarPecasServidor");
			}
		}
	}

	public static void main(String args[]) {
		try{
			Registry registry = LocateRegistry.createRegistry(1099);
			System.out.println("Servidor de Nomes lançado!");

			registry.rebind("ServerRef",new Server());
			System.out.println("Servidor Registrado!");

		} catch (Exception e){
			JOptionPane.showMessageDialog(null, "Essa porta já está sendo usada por outra aplicação!");
			System.out.println(e);
			System.out.println("Essa porta já está sendo usada por outra aplicação!");
		}
	}
}


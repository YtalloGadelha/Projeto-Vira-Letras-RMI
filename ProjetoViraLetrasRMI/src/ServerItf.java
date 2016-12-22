import java.rmi.Remote; 
import java.rmi.RemoteException;

public interface ServerItf extends Remote {

	void escutarClienteChat(String enderecoServidor, String msg) throws RemoteException;
	
	void encaminharParaTodosChat(String msg) throws RemoteException;
	
	int tamanhoListaEscritores() throws RemoteException;
	
	void misturarPecasServidor(int indicePeca)throws RemoteException;
	
	void virarPecasServidor(int indicePeca)throws RemoteException;
	
	void desvirarPecasServidor(int indicePeca)throws RemoteException;
	
	void excluirPecasServidor(int indicePeca)throws RemoteException;
	
	void jogarDadosServidor(int valorDado)throws RemoteException;
	
	void finalizarJogadaServidor(int indice, int placar)throws RemoteException;
	
	void iniciarJogoServidor(int indiceJogador)throws RemoteException;
	
	void desistirJogoServidor(int indiceJogador)throws RemoteException;
	
	void terminarJogoServidor(int indiceJogador)throws RemoteException;
	
	void reiniciarJogoServidor(String msg)throws RemoteException;
	
}

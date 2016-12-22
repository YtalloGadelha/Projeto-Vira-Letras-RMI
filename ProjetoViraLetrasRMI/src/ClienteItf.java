import java.rmi.Remote; 
import java.rmi.RemoteException;

public interface ClienteItf extends Remote {

	void escutaServidorChat(String msg) throws RemoteException;
	
	void misturarPecasCliente(int indicePeca)throws RemoteException;
	
	void virarPecasCliente(int indicePeca)throws RemoteException;
	
	void desvirarPecasCliente(int indicePeca)throws RemoteException;

	void excluirPecasCliente(int indicePeca)throws RemoteException;

	void jogarDadosCliente(int valorDado)throws RemoteException;
	
	void finalizarJogadaCliente(int indice, int placar)throws RemoteException;
	
	void iniciarJogoCliente(int indiceJogador)throws RemoteException;

	void informarVencedorCliente(int indiceJogador)throws RemoteException;
	
	void reiniciarJogoCliente(String msg)throws RemoteException;
	
}

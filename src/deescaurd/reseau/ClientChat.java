package deescaurd.reseau;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import deescaurd.Controleur;
import deescaurd.ihm.connexion.FrameJoin;

public class ClientChat implements Runnable
{
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private String pseudo;
	private Controleur ctrl;

	public ClientChat(String adresseServeur, int port, Controleur ctrl) 
	{
		this.ctrl = ctrl;

		try 
		{
			// Connexion au serveur
			socket = new Socket(adresseServeur, port);
			JOptionPane.showMessageDialog(new JFrame(), "Sucess", "success", JOptionPane.INFORMATION_MESSAGE);

			
			// Initialisation des flux
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Gérer l'authentification (choix du pseudonyme)
			this.authentification();
 
			// Démarrer un thread pour lire les messages du serveur
			new Thread(this).start();

			// Boucle pour envoyer des messages
			this.boucle();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(new JFrame(), e, "Erreur", JOptionPane.ERROR_MESSAGE);
			new FrameJoin(this.ctrl);
		} 
		finally 
		{
			close();
		}
	}

	private void authentification() throws IOException 
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println(in.readLine()); // Message de bienvenue et demande de pseudonyme
		while (true) 
		{
			pseudo = this.ctrl.getPseudo();
			out.println(pseudo);
			String reponse = in.readLine(); // Réponse du serveur
			System.out.println(reponse);

			if (reponse.substring(0, 9).equals("Bienvenue")) 
			{
				break; 
			}
		}
	}
	
	private void boucle() 
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Vous pouvez maintenant envoyer des messages (tapez 'quit' pour quitter) : \n /quiestenligne pour voir la liste des connectés");
		while (true) 
		{
			String message = scanner.nextLine();
			if (message.equalsIgnoreCase("quit")) 
			{
				System.out.println("Déconnexion...");
				break;
			}
			out.println(message);
		}
	}

	private void close() 
	{     
		try 
		{
			if (socket != null) 
			{
				socket.close();
			}
			System.out.println("Connexion fermée.");
		} 
		catch (IOException e) 
		{
			System.err.println("Erreur lors de la fermeture : " + e.getMessage());
		}
	}

	@Override
	public void run() 
	{
		try 
		{
			String message;
			while ((message = in.readLine()) != null) 
			{
				System.out.println(message);
			}
		} 
		catch (IOException e) 
		{
			System.err.println("Connexion au serveur perdue.");
		} 
		finally 
		{
			close();
		}
	}
}

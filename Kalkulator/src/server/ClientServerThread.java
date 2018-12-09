package server;

import java.io.*;
import java.net.*;

public class ClientServerThread extends Thread {

	Socket ClientSocket;
	String ClientUsername;
	String ClientPassword;
	static BufferedReader tokOdKlijenta;
	static PrintStream tokKaKlijentu;
	File fajl = new File("Korisnici.txt");
	File ClientFile;
	
	public ClientServerThread(Socket ClientSocket) {
		this.ClientSocket=ClientSocket;
	}
	
	@Override
	public void run() {
		
		try {
			tokOdKlijenta = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
			tokKaKlijentu = new PrintStream (ClientSocket.getOutputStream());
			
			String odlukaKlijenta = izborNacinaRada();
			
			if(odlukaKlijenta.equals("***KRAJ")) kraj();
			
			if(odlukaKlijenta.equalsIgnoreCase("gost")) {
				
				if(gost()==false) return;
			}
			
			else if(odlukaKlijenta.equalsIgnoreCase("Registracija")) registaracija();
			else if(odlukaKlijenta.equalsIgnoreCase("Sign")) sign();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void kraj() {
		String porukaServera = "Odlucili ste da napustite aplikaciju";
		tokKaKlijentu.println(porukaServera);
		if(ClientUsername==null) {
			tokKaKlijentu.println("gost");
			tokKaKlijentu.println("Dovidjenja!");
			try {
				ClientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(ClientUsername!=null) {
		
			tokKaKlijentu.println("Prijavljen korisnik");
			tokKaKlijentu.println("Da li zelite da dobije fajl sa operacijama? DA/NE");
			
			String odlukaKlijenta="NE";
			try {
				odlukaKlijenta = tokOdKlijenta.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(odlukaKlijenta.equalsIgnoreCase("DA")) {
				tokKaKlijentu.println("Dovidjenja "+ClientUsername);
				slanjeFajla(ClientUsername);
			}
			else tokKaKlijentu.println("Dovidjenja "+ClientUsername);
			
			try {
				ClientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		try {
			ClientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  String izborNacinaRada() {
		
		String odlukaKlijenta="greska";
		
		try {
			tokKaKlijentu.println("Izaberite nacin rada: Gost/Sign/Registracija");
			tokKaKlijentu.println("Ako zelite da prekinetu posaljite ***KRAJ");
			
			odlukaKlijenta = tokOdKlijenta.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return odlukaKlijenta;
	}

	public void registaracija() {
		
        registracijaUsername();
		registacijaPassword();
		
		upisUFajl();
		prijavaljeniKorisnik();
		
		
	}
	
	
	public void registracijaUsername() {
		
		
		try {
			String username = tokOdKlijenta.readLine();
			

			while(proveraImena(username)==false) {
				username = tokOdKlijenta.readLine();
			
			}
			ClientUsername = username;
			
			ClientFile = new File(ClientUsername+".txt");
			tokKaKlijentu.println("Uspesan unos imena");
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}
	
	public void registacijaPassword() {
		
		
		try {
			String password = tokOdKlijenta.readLine();
			
			
			while(proveraSifre(password)==false) {
				password = tokOdKlijenta.readLine();
				
			}
			ClientPassword = password;
			tokKaKlijentu.println("Uspesan unos sifre");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private boolean proveraSifre(String password) {
		int brojVelikihSlova=0;
		int brojCifara=0; 
					
		
		if(password.length()<8) {
			tokKaKlijentu.println("Sifra mora da sadrzi bar 8 karaktera!");
			return false;
		}
		
		for(int i=0;i<password.length();i++) {
			if(password.charAt(i)>='A' && password.charAt(i)<='Z') brojVelikihSlova++;
			if(password.charAt(i)>='0' && password.charAt(i)<='9')	brojCifara++;
		}
		
		if(brojVelikihSlova==0 && brojCifara==0) {
			tokKaKlijentu.println("Sifra mora da sadrzi veliko slovo i cifru ");
		return false;
	}
		
		if(brojVelikihSlova==0)  {
			tokKaKlijentu.println("Sifra mora da ima veliko slovo");
		return false;
		}
		
		if(brojCifara==0)  {
			tokKaKlijentu.println("Sifra mora da ima cifru");
		return false;
		}
		return true;
	}
	
	private boolean proveraImena(String ime) throws IOException {
		if(ime.contains(" ")) {
			tokKaKlijentu.println("Ne smete imati razmak u imenu!");
			return false;
		}
			try {
				FileReader fr = new FileReader(fajl);
				BufferedReader br = new BufferedReader(fr);
				String sadrzaj=" ";
				while(sadrzaj!=null) {
				 sadrzaj = br.readLine();
				 if(sadrzaj==null) return true;
				 if(sadrzaj.startsWith(ime+" ")) {
				 		 tokKaKlijentu.println("Ime vec postoji!");
				 		 return false;
				 }
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
	}
	
	public void upisUFajl() {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fajl,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(ClientUsername+" "+ClientPassword+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean operacija() {
		
		try {
			String operacija = tokOdKlijenta.readLine();
			if(operacija.equals("***KRAJ")) {
				kraj();
				return false;
			}
			
			
			String ulaz = tokOdKlijenta.readLine();
			
			if(ulaz.equals("***KRAJ")) {
				kraj();
				return false;
			}
			
			double broj1 = Double.parseDouble(ulaz);
			
			
			ulaz=tokOdKlijenta.readLine();
			
			if(ulaz.equals("***KRAJ")) {
				kraj();
				return false;
			}
			
			double broj2 = Double.parseDouble(ulaz);
			
			double rezultat;
			
			if(operacija.equals("+")) rezultat=broj1+broj2;
			else if(operacija.equals("-")) rezultat=broj1-broj2;
			else if(operacija.equals("*")) rezultat=broj1*broj2;
			else rezultat=broj1/broj2;
			String iskaz = broj1+operacija+broj2+"="+rezultat;
			
			tokKaKlijentu.println(iskaz);
			if(ClientUsername!=null) upisOperacija(iskaz);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	
	public boolean gost() {
		
		
			int brojac=3;
			
			while(brojac>0) {
				tokKaKlijentu.println("Dozvoljen rad");
				boolean validnost = operacija();
				if(validnost==false) return false;
				brojac--;
			}
			tokKaKlijentu.println("Iskoristili ste sve 3 operacija. Dovidjenja!");
			
		return true;
	}
	
	public void sign() {
		
		if(ClientSocket.isClosed()) return;
		
		try {
			String username = tokOdKlijenta.readLine();
			String password = tokOdKlijenta.readLine();
			
			FileReader fr = new FileReader(fajl);
			BufferedReader br = new BufferedReader(fr);
			String line =" ";
			while(line!=null) {
				line=br.readLine();
				if(line==null)  {
					tokKaKlijentu.println("Pogresno ime ili lozinka.");
					sign();
					return;
				}
				String[] korisnik=line.split(" ");
				if(korisnik[0].equals(username) && korisnik[1].equals(password)) {
					tokKaKlijentu.println("Uspesno ste se prijavili!");
					
					ClientUsername=username;
					ClientPassword=password;
					
					prijavaljeniKorisnik();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void prijavaljeniKorisnik() {
		while(true) {
			if(operacija()==false) return;
		}
	}
	
	private void upisOperacija(String statusOperacije) throws IOException {
		FileWriter fw = new FileWriter(ClientUsername+".txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write(statusOperacije+"\r\n");
		
		bw.close();
	}
	
    private void slanjeFajla(String klijent) {
    	
    	try {
			FileInputStream fr = new FileInputStream(klijent+".txt");
			byte b[] = new byte[2048];
			fr.read(b,0,b.length);
			OutputStream out = ClientSocket.getOutputStream();
			out.write(b);
			out.write(b, 0, b.length);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
	
}


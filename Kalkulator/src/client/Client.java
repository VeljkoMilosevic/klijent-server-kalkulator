package client;

import java.io.*;
import java.net.*;

public class Client { 
	
	static Socket soketZaKomunikaciju;
	static BufferedReader tokOdServera;
	static PrintStream tokKaServeru;
	static BufferedReader unosSaKonzole;
	static InputStream in;

	public static void main(String[] args) {
	
		try {
			Socket soketZaKomunikaciju = new Socket("localhost",9011);
			tokOdServera=new BufferedReader(
			new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			tokKaServeru = new PrintStream(soketZaKomunikaciju.getOutputStream());
			unosSaKonzole = new BufferedReader(new InputStreamReader(System.in));
			in = soketZaKomunikaciju.getInputStream();
	
			
			
			String meni = tokOdServera.readLine();
			String porukaOKraju = tokOdServera.readLine();
			System.out.println(meni);
			System.out.println(porukaOKraju);
			
			String odlukaKlijenta = izborRada();
	
			
			if(odlukaKlijenta.equals("greska")) {
				while(odlukaKlijenta.equals("greska")) {
				System.out.println("Pogresan unos. Pokusajte ponovo.");
				odlukaKlijenta=izborRada();
			}
	}
			tokKaServeru.println(odlukaKlijenta);
			if(odlukaKlijenta.equals("***KRAJ")) kraj();
			if(odlukaKlijenta.equalsIgnoreCase("Registracija")) registracija();
			else if(odlukaKlijenta.equalsIgnoreCase("Gost")) {
				if(gost()==false) return;
			}
			else if(odlukaKlijenta.equalsIgnoreCase("Sign")) sign();			
			
			
		} catch (UnknownHostException e) {
			System.err.println("Nepoznat server");
		} catch (IOException e) {
			System.err.println("IOException"+e);
		}
		
	}
	
	public static String izborRada() {
		String odlukaKlijenta="greska";
		try {
			 odlukaKlijenta = unosSaKonzole.readLine();

		} catch (IOException e) {
			System.err.println("IOException"+e);
		}
		if(odlukaKlijenta.equals("***KRAJ")) return "***KRAJ";
		else if(proveraUnosa(odlukaKlijenta)==true) return odlukaKlijenta;
		return "greska";
	}
	
	public static void kraj() {
		
		try {
			String recenica = tokOdServera.readLine();
			System.out.println(recenica);
			String nacinRada= tokOdServera.readLine();
			if(nacinRada.equals("gost")) {
				String porukaServera = tokOdServera.readLine();
				System.out.println(porukaServera);
				return;
			}
			if(nacinRada.equals("Prijavljen korisnik")) {
		
				String porukaServera = tokOdServera.readLine();
				System.out.println(porukaServera);
				
				String odlukaKlijenta = unosSaKonzole.readLine();
				tokKaServeru.println(odlukaKlijenta);
				
				if(odlukaKlijenta.equalsIgnoreCase("DA")) {
					String pozdravOdServera =  tokOdServera.readLine();
					System.out.println(pozdravOdServera);
					vratiFajl();
				}
				
				else {
					String pozdravOdServera =  tokOdServera.readLine();
				    System.out.println(pozdravOdServera);
				}
			}
		} catch (IOException e) {
			System.err.println("IOException"+e);
		}
	}
	
	public static boolean proveraUnosa(String odlukaKlijenta) {
		
		
		if(odlukaKlijenta.equalsIgnoreCase("Registracija")) return true;
		else if(odlukaKlijenta.equalsIgnoreCase("Gost")) return true;
		else if(odlukaKlijenta.equalsIgnoreCase("Sign")) return true;
		return false;
	}
	
	public static void registracija() {
		
		registracijaUsername();
		registracijaPassword();
		prijavljeniKorisnik();
		
	}
	
	public static void korisnikovUnos(String unos) {
		
		try {
			if(unos.equals("***KRAJ")) {
				tokKaServeru.println(unos);
				String porukaServera = tokOdServera.readLine();
				System.out.println(porukaServera);
			}
		} catch (IOException e) {
			System.err.println("IOException"+e);
		}
		
	}
	
	
	public static void registracijaUsername() {
		
		try {
			
			System.out.println("Unesite ime: ");
			String username = unosSaKonzole.readLine();
			
			
			tokKaServeru.println(username);
			
			String porukaServera = tokOdServera.readLine();
		
			System.out.println(porukaServera);
			if(!porukaServera.equals("Uspesan unos imena")) registracijaUsername();
		} catch (IOException e) {
			System.err.println("IOException"+e);
		}
		
	}
	
	
public static void registracijaPassword() {
		
		try {
			
			System.out.println("Unesite sifru: ");
			String password = unosSaKonzole.readLine();
			
			
			tokKaServeru.println(password);
			
			String porukaServera = tokOdServera.readLine();
			
			System.out.println(porukaServera);
			if(!porukaServera.equals("Uspesan unos sifre")) registracijaPassword();
		} catch (IOException e) {
			System.err.println("IOException"+e);
		}
	}

private static boolean operacija() throws IOException  {
	
	
	System.out.println("Unesite operaciju:");
	String operacija = unosSaKonzole.readLine();
	if(operacija.equals("***KRAJ")) {
		tokKaServeru.println("***KRAJ");
		kraj();
		return false;
	}
	
	if(!operacija.equals("+") && !operacija.equals("-") &&
			!operacija.equals("*") && !operacija.equals("/")) {
			while(!operacija.equals("+") && !operacija.equals("-") &&
			!operacija.equals("*") && !operacija.equals("/") &&
			!operacija.equals("kraj")) {
				
			if(operacija.equals("***KRAJ")) {
				tokKaServeru.println("***KRAJ");
				kraj();
				return false;
			}
				
			System.out.println("Pogresan unos operacije!");
			System.out.println("Unesite ponovo operaciju:");
			operacija=unosSaKonzole.readLine();
		
		}
	}
	tokKaServeru.println(operacija);
	
	System.out.println("Unesite prvi broj:");
	String prviBroj = unosSaKonzole.readLine();
	
	if(prviBroj.equals("***KRAJ")) {
		tokKaServeru.println("***KRAJ");
		kraj();
		return false;
	}
	
	if(proveraBroja(prviBroj)==false) {
	while(proveraBroja(prviBroj)==false) {
		System.out.println("Pogresan unos broj");
		System.out.println("Unesite prvi broj:");
		prviBroj = unosSaKonzole.readLine();
		
	}
}
	tokKaServeru.println(prviBroj);
	
	System.out.println("Unesite drugi broj:");
	String drugiBroj = unosSaKonzole.readLine();
	
	if(drugiBroj.equals("***KRAJ")) {
		tokKaServeru.println("***KRAJ");
		kraj();
		return false;
	}

	if(proveraBroja(drugiBroj)==false) {
	while(proveraBroja(drugiBroj)==false) {
		System.out.println("Pogresan unos broj");
		System.out.println("Unesite drugi broj:");
		drugiBroj = unosSaKonzole.readLine();
		
	}
}
	tokKaServeru.println(drugiBroj);
	
	String izraz = tokOdServera.readLine();
	System.out.println(izraz);
	return true;
}

public static boolean proveraBroja(String broj) {
	if(broj.isEmpty()) return false;
	for(int i=0; i<broj.length();i++) {
		if(broj.charAt(i)<'0' || broj.charAt(i)>'9') {
			return false;
		}
	}
	return true;
}

public void meni() {
	
	try {
		System.out.println("Unesite nacin rada");
		System.out.println("Gost/Prijava/Registracija");
		String izbor = unosSaKonzole.readLine();
	
		
		if(izbor=="gost") {
			tokKaServeru.println("gost");
			gost();
		}
	} catch (IOException e) {
		System.err.println("IOException"+e);
	}
}

public static boolean gost() {
	
	try {
		while(true) {
			String porukaServera=tokOdServera.readLine();
			if(porukaServera.equals("Iskoristili ste sve 3 operacija. Dovidjenja!")) {
				System.out.println(porukaServera);
				return true;
			}
			boolean validnost = operacija();
			if(validnost==false) return false;
		}
	} catch (IOException e) {
		System.err.println("IOException"+e);
	}
	return true;
}

private static void sign() throws IOException {
	
	System.out.println("Unesite ime");
	String ime = unosSaKonzole.readLine();
	
	tokKaServeru.println(ime);
	System.out.println("Unesite sifru");
	String sifra = unosSaKonzole.readLine();
	
	tokKaServeru.println(sifra);
	
	String porukaServera = tokOdServera.readLine();
	System.out.println(porukaServera);
	if(porukaServera.equals("Pogresno ime ili lozinka.")) {
		sign();
	}
	else prijavljeniKorisnik();
	
}



public static boolean prijavljeniKorisnik() {
	
	while(true) {
	   try {
		boolean validnost = operacija();
		if(validnost==false) {
			return false;
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
   }
}

public static void vratiFajl()  {
	
	try {
		byte b[]= new byte[2048];
		FileOutputStream fileOut = new FileOutputStream("vasFajl.txt");
		in.read(b,0,b.length);
		fileOut.write(b,0,b.length);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

}

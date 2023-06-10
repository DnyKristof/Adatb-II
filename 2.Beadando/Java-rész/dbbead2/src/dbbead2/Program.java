package dbbead2;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;


public class Program {
	
	static DBMethods dbm = new DBMethods();
	public static void main(String[] args)  {
		Scanner scanner = new Scanner(System.in);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		boolean kilepes = false;
		//dbm.Reg();
		int i;
		int count;
		String input;
		String[] params;
		while(!kilepes) {
			System.out.println("\n");
			System.out.println("0.Kilépés");
			System.out.println("1.Autó tábla feltöltése random értékekkel");
			System.out.println("2.Fileból orvos tábla feltöltés (kérem ne válassza)");
			System.out.println("3.Új bejelentés felvitele");
			System.out.println("4.Adott napra napi hívásszám");
			System.out.println("5.Kivonulás feltöltése random értékekkel");
			System.out.println("6.Bejelentés adminisztrálása");
			System.out.println("7.Hívás szűrése névre");
			System.out.println("8.Hívás szűrése időszakra");
			System.out.print("Kérlek válassz egy lehetőséget: ");
			i = scanner.nextInt();
			
			switch(i) {
			case 0:
				kilepes = true;
				break;
				
			case 1:
				System.out.print("Mennyi autót szeretne felvinni? :");
				count = scanner.nextInt();
				dbm.RANDOM_AUTO(count);
				break;
			case 2:
				dbm.FILEBOL_ORVOS();
				break;
			case 3:
				System.out.println("Név,Telefonszám,Hely,Diszpécser,Kikuldve");
				scanner.nextLine();
				input = scanner.nextLine();
				params = input.split(",");
				dbm.UJ_HIVAS(params[0], params[1], params[2], params[3], Integer.parseInt(params[4]));
				break;
				
			case 4:
				System.out.println("Kérem adja meg a napot (yyyy-MM-dd) : ");
				scanner.nextLine();
				input = scanner.nextLine();
				try {
					java.util.Date utilDate = dateFormat.parse(input);
			        long timeInMillis = utilDate.getTime();
			        java.sql.Date sqlDate = new java.sql.Date(timeInMillis);
				System.out.println(dbm.NAPI_HIVASSZAM(sqlDate));
				}catch(ParseException e) {
					System.out.println("Nem jó dátum formátum");
				}
				break;
			case 5:
				System.out.println("Kérem adja meg a kivonulások számát");
				scanner.nextLine();
				count = scanner.nextInt();
				dbm.KIVONLUAS_FELTOLTES(count);
				break;
			case 6:
				System.out.println("Kérem adja meg az adminisztrálni kívánt hívás azonosítóját,és hogy ki lett e küldve egyseég (id,0/1)");
				scanner.nextLine();
				input = scanner.nextLine();
				params = input.split(",");
				dbm.HIVAS_ADMINISZTRALASA(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
			case 7:
				System.out.println("Kérem adja meg a nevet: ");
				scanner.nextLine();
				input = scanner.nextLine();
				dbm.HIVAS_NEVRE(input);
			case 8:
				System.out.println("Kérem adja meg a kezdő és végdátumot, vesszővel elválasztva (yyyy-mm-dd,yyyy-mm-dd) : ");
				scanner.nextLine();
				input = scanner.nextLine();
				params = input.split(",");
				try {
				Date eleje = (Date) dateFormat.parse(params[0]);
				Date vége = (Date) dateFormat.parse(params[1]);
				dbm.HIVAS_IDOSZAK(eleje, vége);
				}catch(ParseException e) {
					System.out.println("Nem jó dátum formátum");
				}
				
				
			}
			
			
		}
		
		
	}
}


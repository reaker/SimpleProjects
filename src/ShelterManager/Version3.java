package ShelterManager;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

/**
 Created by sebastian on 2017-03-25.

 Wariant 3
 To samo co poprzednio plus:
- Gdy schronisko ma mniej niż 5 wolnych miejsc wysyłany jest email infomarcyjny do osób pracujacych w schronisku
- Możliwość edycji poszczególnych zwierząt. Dodanie pól takich jak np. stan zdrowia, płeć itp.

 + od samego siebie: zmieniam dostęp z pliku na bazę danych SQL

 Wariant 2
 To samo co poprzednio plus:
 - Zamiast programu mamy proste UI
 - Lista zwierząt jest zapisywana do pliku tekstowego lub bazy danych
 - Po wyłączeniu programu i ponownym odpaleniu ma zostać załądowany ostatni stan z bazy/pliku

 Wariant 1
 Program, w którym dodajemy do bazy/usuwamy z bazy zwierzęta oraz sprawdzamy stan schroniska (pełne, przepełnione, puste etc.)
 - Program konsolowy
 - Schronisko ma mieć określoną ilość miejsc
 - Możliwość dodania/usunięcia zwierzaka do/z listy zwierząt w schronisku ( na razie lista nie musi być zapisywana do żadnej bazy danych, ani pliku tekstowego)
 - Po wpisaniu "status" program ma wypisać aktualną listę zwierząt i wyrzucić komunikat, czy ilość zwierząt powoduje, że schronisko ma jeszcze miejsce, jest pełne lub jest przepełnione
 - Zaimplementuj logikę, która uniemożliwia dodanie zwierzaka, jeśli schronisko jest pełne.
 */

public class Version3 {

    private  static void save(int number){
        //funkcja zrobiona po to aby szybciej w kodzie zapisywać plik
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("baza.txt"));
            String str= Integer.toString(number);
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scanner read(String fileName){
        //szybsze wczytywanie pliku w kodzie
        Scanner scan=null;
        try {
            scan= new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        };
        return scan;
    }

    public static void insertSQL(Statement statement, String animalName, String breed, String sex, int health) throws SQLException{
        statement.executeUpdate("Insert into ShelterManager2 values(null,'"+animalName+"','"+breed+"','"+sex+"'," +health+")");
    }

    public static void deleteSQL(Statement statement, int ID) throws SQLException{
        statement.executeUpdate("Delete from ShelterManager2 where ID="+ID+";");
    }

    public static void statusSQL(Statement statement) throws SQLException{
        JTable table= new JTable();
        String sql="Select * from ShelterManager2;";
        ResultSet rs = statement.executeQuery( sql );
    }


    
    public static void main(String[] args)  throws SQLException{


        //połączenie z bazą danych
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://wijas.eu:3306/reaker_wijas";
        String uid = "reaker_wijas";
        String pwd = "Mercedes1986";
        Connection con=null;

        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, uid, pwd);
        } catch (ClassNotFoundException  exc)  {  // brak klasy sterownika
            System.out.println("Brak klasy sterownika");
            System.out.println(exc);
            System.exit(1);
        } catch(SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + url);
            System.out.println(exc);
            System.exit(1);
        }

        //tworzenie tabel
        Statement statement= null;

        statement = con.createStatement();
        String[] st={"CREATE TABLE if not exists reaker_wijas.ShelterManager2 (\n" +
                    "ID int(7) PRIMARY KEY Auto_increment,\n" +
                    "AnimalName varchar(25) NOT NULL,\n" +
                    "Breed varchar(25) NOT NULL,\n" +
                    "Sex varchar(1) NOT NULL,\n" +
                    "Health int(1) NOT NULL);",
                    "create table if not exists sex_list (sex varchar(1) primary key);" ,
                    "insert into sex_list values('M'),('F');" ,
                    "create table if not exists health_list (health int(1) primary key);" ,
                    "insert into health_list values(1),(2),(3),(4),(5),(6),(7),(8),(9),(10);" ,
                    "ALTER TABLE ShelterManager2 add FOREIGN KEY (Sex) REFERENCES sex_list(sex);" ,
                    "ALTER TABLE ShelterManager2  add FOREIGN KEY (Health) REFERENCES health_list(health);"};

        DatabaseMetaData dbm = con.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "ShelterManager2", null);
        if (!tables.next()) {
            for (String s : st) {statement.executeUpdate(s);}
        }


        int capacity=100;
        int actualAmount=0;
        int amount=0;

        //Dane zwierzaka
        int id=0;
        String animalName=null;
        String breed=null;
        String sex=null;
        int health=0;

        animalName="ddd";
        breed="ddd";
        sex="M";
        health=1;

        insertSQL(statement,animalName,breed,sex,health);

        String fname= "baza.txt";

        Scanner in= read(fname);

        //odczytujemy zawartość pliku
        while (in.hasNext()){
            actualAmount=in.nextInt();
        }

        String[] opcje =  { "Dodaj", "Usuń", "Status"};

        while (true) {

            try {
                //MENU
                String msg="Miejsc w schronisku: "+capacity + "\nIlość zwierzaków: "+actualAmount+"\nCo zrobić?";
                int chooser = JOptionPane.showOptionDialog(null, msg,"Schelter Manager", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null, opcje, opcje[0]);

                // Case'y dla trzech wyborów
                if (chooser<0){return;}
                switch (chooser) {
                    //dla DODAJ
                    case 0: {
                        amount= Integer.parseInt(JOptionPane.showInputDialog(null,"Ile zwierzaków dodać? Wpisz liczbę: ","Schelter Manager",JOptionPane.DEFAULT_OPTION).trim());

                        if (amount>=0) {
                            if (actualAmount + amount > capacity) {
                                JOptionPane.showMessageDialog(null,"Nie można dodać tylu zwierzaków!","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                break;
                            }
                            else {
                                actualAmount += amount;
                                JOptionPane.showMessageDialog(null,"Poprawnie dodano. Liczba zwiarzaków w schronisku: " + actualAmount,"Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                save(actualAmount);
                                break;
                            }
                        }
                        else JOptionPane.showMessageDialog(null,"Podałeś liczbę ujemną! :)","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                        break;
                    }

                    // USUŃ
                    case 1: {
                        amount= Integer.parseInt(JOptionPane.showInputDialog(null,"Ile zwierzaków odjąć? Wpisz liczbę: ","Schelter Manager",JOptionPane.DEFAULT_OPTION).trim());
                        if (amount >= 0) {
                            if (actualAmount - amount < 0) {
                                JOptionPane.showMessageDialog(null,"Nie ma tylu zwierzaków w schronisku!","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                break;
                            } else {
                                actualAmount -= amount;
                                JOptionPane.showMessageDialog(null,"Poprawnie odjęto. Liczba zwiarzaków w schronisku: " + actualAmount,"Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                save(actualAmount);
                                break;
                            }
                        }
                        else JOptionPane.showMessageDialog(null,"Podałeś liczbę ujemną! :)","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                        break;
                    }

                    //STATUS
                    case 2: {
                        JOptionPane.showMessageDialog(null,"Liczba zwierzaków w schronisku: " + actualAmount + "\nPojemność schroniska: " + capacity,"Schelter Manager",JOptionPane.DEFAULT_OPTION);
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(null,"Wpisałeś błędną wartość! ","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                        break;
                }

            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null,"Błędne dane wejściowe!","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                break;
            }
        }
    }
}

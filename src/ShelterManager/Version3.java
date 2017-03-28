package ShelterManager;

import javax.swing.*;
import java.sql.*;
/**
 Created by sebastian on 2017-03-25.

 Wariant 3
 Staralem sie kod pisac po angielsku a program po polsku :)
 To samo co poprzednio plus:
- Gdy schronisko ma mniej niż 5 wolnych miejsc wysyłany jest email infomarcyjny do osób pracujacych w schronisku
- Możliwość edycji poszczególnych zwierząt. Dodanie pól takich jak np. stan zdrowia, płeć itp.

 + od samego siebie: zmieniam dostęp z pliku na bazę danych SQL.
 // Nie jest to doskonała wersja tego projektu, ale wszystkie funkcjonalności SĄ :)
 // brakuje miejscami obsługi wyjątków

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

    private static void insertSQL(Statement statement, String animalName, String breed, char sex, int health) throws SQLException{
        statement.executeUpdate("Insert into ShelterManager2 values(null,'"+animalName+"','"+breed+"','"+sex+"'," +health+")");
    }

    private static void deleteSQL(Statement statement, int ID) throws SQLException{
        statement.executeUpdate("Delete from ShelterManager2 where ID="+ID+";");
    }

    private static int countSQL(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("select * from ShelterManager2");
        int count = 0;
        while (rs.next()) count++;
        return count;
    }

    private static void statusSQL(Statement statement, Connection con) throws SQLException{
       // new TableFromMySqlDatabase(con);
        TableFromMySqlDatabase frame = new TableFromMySqlDatabase(con);
        frame.setTitle("Manager Schroniska - baza danych");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)  throws SQLException{

        //connecting with database
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://wijas.eu:3306/reaker_wijas";
        String uid = "reaker_wijas";
        String pwd = "123";
        Connection con=null;

        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, uid, pwd);
        } catch (ClassNotFoundException  exc)  {  // brak klasy sterownika
            System.out.println("Brak klasy sterownika");
            System.exit(1);
        } catch(SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + url);
            System.exit(1);
        }

        //creating tables
        Statement statement;

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

        int capacity=100, amount;
        int actualAmount=countSQL(statement);

        // animal data
        String animalName, breed;
        char sex;
        int id,health;

        String fname= "baza.txt";

        String[] opcje =  { "Dodaj", "Usuń", "Status", "Edytuj"};

        while (true) {

            try {
                //MENU
                String msg="Miejsc w schronisku: "+capacity + "\nIlość zwierzaków: "+actualAmount+"\nCo zrobić?";
                int chooser = JOptionPane.showOptionDialog(null, msg,"Manager schroniska", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null, opcje, opcje[0]);

                // Case's for 4 posibilities
                if (chooser<0){return;}
                switch (chooser) {
                    // Add
                    case 0: {
                        if ((actualAmount+1)<capacity) {
                            animalName = JOptionPane.showInputDialog(null, "Wpisz nazwę zwierzaka. ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE).trim();
                            breed = JOptionPane.showInputDialog(null, "Wpisz rasę zwierzęcia", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE).trim();
                            sex = JOptionPane.showInputDialog(null, "Jakiej płci jest zwierzę? Wpisz M lub F: ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE).toUpperCase().trim().charAt(0);
                            if (!(sex=='M') && !(sex=='F')){
                                JOptionPane.showMessageDialog(null,"Wprowadziłeś błędną wartość!","Manager schroniska",JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                            health = Integer.parseInt(JOptionPane.showInputDialog(null, "Stan zdrowia zwierzaka? Wpisz liczbę w skali od 1 do 10: ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE).trim());
                            if (!(health>=1 && health<=10)){
                                JOptionPane.showMessageDialog(null,"Wprowadziłeś błędną wartość!","Manager schroniska",JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }

                            insertSQL(statement, animalName, breed, sex, health);
                            actualAmount++;
                            break;
                        }

                        else {
                                JOptionPane.showMessageDialog(null,"Nie można dodać tylu zwierzaków!","Manager schroniska",JOptionPane.INFORMATION_MESSAGE);
                                break;
                            }
                        }

                    // Delete
                    case 1: {
                        id = Integer.parseInt(JOptionPane.showInputDialog(null, "Wpisz ID zwierzaka. ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE).trim());

                        int countBefore=countSQL(statement);
                        deleteSQL(statement,id);
                        int countAfter=countSQL(statement);

                        if (countAfter==countBefore) {JOptionPane.showMessageDialog(null,"Nie ma zwierzaka o takim ID.","Manager schroniska",JOptionPane.INFORMATION_MESSAGE);break;}
                            actualAmount--;
                            JOptionPane.showMessageDialog(null,"Poprawnie odjęto. Liczba zwiarzaków w schronisku: " + actualAmount,"Manager schroniska",JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }

                    // STATUS
                    case 2: {
                        statusSQL(statement,con);
                        JOptionPane.showMessageDialog(null,"Liczba zwierzaków w schronisku: " + countSQL(statement)+ "\n Pojemność schroniska: " + capacity,"Manager schroniska",JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }

                    // Edit
                    case 3: {
                        // edit animal name
                        id = Integer.parseInt(JOptionPane.showInputDialog(null, "Wpisz ID zwierzaka. ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE).trim());
                        ResultSet result = statement.executeQuery("select * from ShelterManager2 where id="+id);
                        result.next();
                        animalName = result.getString(2);
                        animalName = JOptionPane.showInputDialog(null, "Obecna nazwa zwierzęcia: " + animalName + "\nWprowadź nową nazwę: ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE);

                        //edit animal breed
                        breed = result.getString(3);
                        breed = JOptionPane.showInputDialog(null, "Obecna rasa zwierzęcia: " + breed + "\nWprowadź nową rasę: ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE);

                        //edit animal sex
                        sex = result.getString(4).charAt(0);
                        sex = JOptionPane.showInputDialog(null, "Obecna płeć: " + sex + "\nWprowadź nową płeć: ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE).trim().toUpperCase().charAt(0);

                        //edit animal health
                        health = Integer.parseInt(result.getString(5));
                        health= Integer.parseInt(JOptionPane.showInputDialog(null, "Obecny poziom zdrowia: " + health + "\nWprowadź poziom zdrowia: ", "Manager schroniska", JOptionPane.INFORMATION_MESSAGE));

                        // write all values into database
                        statement.executeUpdate("update ShelterManager2 set AnimalName='" + animalName +"', Breed='" + breed + "', Sex='"+sex+"', Health='"+health+"' where ID=" + id);
                        break;
                    }
                    default:
                        JOptionPane.showMessageDialog(null,"Wpisałeś błędną wartość! ","Manager schroniska",JOptionPane.INFORMATION_MESSAGE);
                        break;
                }

            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null,"Błędne dane wejściowe!","Manager schroniska",JOptionPane.INFORMATION_MESSAGE);
                exc.printStackTrace();

                break;
            }
        }
    }
}

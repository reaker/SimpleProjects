package ShelterManager;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

/**
 *
 Created by sebastian on 2017-03-22.
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

public class Version2 {

    private static void save(int number){
        //funkcja zrobiona po to aby szybciej w kodzie zapisywać plik
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter("baza.txt"));
            String str= Integer.toString(number);
            System.out.println(str);
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Scanner read(String fileName){
        //szybsze wczytywanie pliku w kodzie
        Scanner scan=null;
        try {
            scan= new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return scan;
    }

    public static void main(String[] args) {

        int capacity=100;
        int actualAmount=0;
        int amount;

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
                                System.out.println(actualAmount+"  "+ amount);
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

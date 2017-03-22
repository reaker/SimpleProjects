package ShelterManager;

import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 Created by sebastian on 2017-03-22.
Wariant 2
To samo co poprzednio plus:
 - Zamiast programu mamy proste UI
 - Lista zwierząt jest zapisywana do pliku tekstowego lub bazy danych
  - Powyłączeniu programu i ponownym odpaleniu ma zostać załądowany ostatni stan z bazy/pliku

 Wariant 1
 Program, w którym dodajemy do bazy/usuwamy z bazy zwierzęta oraz sprawdzamy stan schroniska (pełne, przepełnione, puste etc.)
 - Program konsolowy
 - Schronisko ma mieć określoną ilość miejsc
 - Możliwość dodania/usunięcia zwierzaka do/z listy zwierząt w schronisku ( na razie lista nie musi być zapisywana do żadnej bazy danych, ani pliku tekstowego)
 - Po wpisaniu "status" program ma wypisać aktualną listę zwierząt i wyrzucić komunikat, czy ilość zwierząt powoduje, że schronisko ma jeszcze miejsce, jest pełne lub jest przepełnione
 - Zaimplementuj logikę, która uniemożliwia dodanie zwierzaka, jeśli schronisko jest pełne.
 */

public class Version2 {

    public static void main(String[] args) {

        int capacity=100;
        int actualAmount=0;
        int amount=0;

        String msg="Ilość miejsc w schronisku: "+capacity + "\nCo zrobić?";
        String[] opcje =  { "Dodaj", "Usuń", "Status"};



        while (true) {

            try {
                int chooser = JOptionPane.showOptionDialog(null, msg,"Schelter Manager", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,null, opcje, opcje[0]);

                switch (chooser) {
                    case 0: {
                        amount= Integer.parseInt(JOptionPane.showInputDialog(null,"Ile zwierzaków dodać? Wpisz liczbę: ","Schelter Manager",JOptionPane.DEFAULT_OPTION).trim());
                        if (amount>=0) {
                            if (actualAmount + amount > capacity) {
                                JOptionPane.showMessageDialog(null,"Nie można dodać tylu zwierzaków!","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                break;
                            } else {
                                actualAmount += amount;
                                JOptionPane.showMessageDialog(null,"Poprawnie dodano. Liczba zwiarzaków w schronisku: " + actualAmount,"Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                break;
                            }
                        }
                        else JOptionPane.showMessageDialog(null,"Podałeś liczbę ujemną! :)","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                        break;
                    }

                    case 1: {
                        amount= Integer.parseInt(JOptionPane.showInputDialog(null,"Ile zwierzaków odjąć? Wpisz liczbę: ","Schelter Manager",JOptionPane.DEFAULT_OPTION).trim());
                        if (amount >= 0) {
                            if (actualAmount - amount < 0) {
                                JOptionPane.showMessageDialog(null,"Nie ma tylu zwierzaków w schronisku!","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                break;
                            } else {
                                actualAmount -= amount;
                                JOptionPane.showMessageDialog(null,"Poprawnie odjęto. Liczba zwiarzaków w schronisku: " + actualAmount,"Schelter Manager",JOptionPane.DEFAULT_OPTION);
                                break;
                            }
                        }
                        else JOptionPane.showMessageDialog(null,"Podałeś liczbę ujemną! :)","Schelter Manager",JOptionPane.DEFAULT_OPTION);
                        break;
                    }

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

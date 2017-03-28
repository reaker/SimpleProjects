package ShelterManager;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
Created by sebastian on 2017-03-22.

Wariant 1
Program, w którym dodajemy do bazy/usuwamy z bazy zwierzęta oraz sprawdzamy stan schroniska (pełne, przepełnione, puste etc.)
- Program konsolowy
- Schronisko ma mieć określoną ilość miejsc
- Możliwość dodania/usunięcia zwierzaka do/z listy zwierząt w schronisku ( na razie lista nie musi być zapisywana do żadnej bazy danych, ani pliku tekstowego)
- Po wpisaniu "status" program ma wypisać aktualną listę zwierząt i wyrzucić komunikat, czy ilość zwierząt powoduje, że schronisko ma jeszcze miejsce, jest pełne lub jest przepełnione
- Zaimplementuj logikę, która uniemożliwia dodanie zwierzaka, jeśli schronisko jest pełne.
*/

public class Version1 {


    public static void main(String[] args) {

        int capacity=100;
        int actualAmount=0;
        int chooser;
        int amount;
        Scanner scan= new Scanner(System.in);
        System.out.println("Ilość miejsc w schronisku: "+capacity);

        while (true) {
            try {
                System.out.println("\nMENU:   1 - dodaj    2 - usuń    3 - status ");

                chooser = scan.nextInt();

                switch (chooser) {
                    case 1: {
                        System.out.println("Ile zwierzaków dodać? Wpisz liczbę: ");
                        amount = scan.nextInt();
                        if (amount>=0) {
                            if (actualAmount + amount > capacity) {
                                System.out.println("Nie można dodać tylu zwierzaków!");
                                TimeUnit.SECONDS.sleep(1);
                                break;
                            } else {
                                actualAmount += amount;
                                System.out.println("Poprawnie dodano. Liczba zwiarzaków w schronisku: " + actualAmount);
                                break;
                            }
                        }
                        else System.out.println("Podałeś liczbę ujemną! :)");
                        break;
                    }

                    case 2: {
                        System.out.println("Ile zwierzaków odjąc? Wpisz liczbę: ");
                        amount = scan.nextInt();
                        if (amount >= 0) {
                            if (actualAmount - amount < 0) {
                                System.out.println("Nie ma tylu zwierzaków w schronisku!");
                                TimeUnit.SECONDS.sleep(1);
                                break;
                            } else {
                                actualAmount -= amount;
                                System.out.println("Poprawnie odjęto. Liczba zwiarzaków w schronisku: " + actualAmount);
                                break;
                            }
                        }
                        else System.out.println("Podałeś liczbę ujemną! :)");
                        break;
                    }

                    case 3: {
                        System.out.println("Liczba zwierzaków w schronisku: " + actualAmount + ". Pojemność schroniska: " + capacity);
                        TimeUnit.SECONDS.sleep(1);
                        break;
                    }
                    default:
                        System.out.println("Wpisałeś błędną wartość! ");
                        TimeUnit.SECONDS.sleep(1);
                        break;
                }

            } catch (Exception exc) {
                System.out.println("Błędne dane wejściowe!");
                break;
            }
        }
    }
}

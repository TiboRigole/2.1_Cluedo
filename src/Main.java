import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc =new Scanner(System.in);

    //reservering van het aantal variabelen

        //integer inlezen, converteren naar integer
        final int aantalSpelletjes =Integer.parseInt(sc.nextLine());

        //worden 1keer per spel ingelezen
        int aantalMogPersonen;
        int aantalMogLocaties;
        int aantalMogWapens;
        int aantalEntries; // aantal vragen en antwoorden
        int aantalKaartjesPerPersoon;

        //variabelen voor het beheren van de vraag
        String vraag;
        int vraagSteller = 0;
        char antwoorder = '0';
        char k1 = '0';
        char k2 = '0';
        char k3 = '0';

        //een stringbuilder die het antwoord weergeeft
        StringBuilder antwoord = new StringBuilder();

        //aanmaak van 5 spelers in een arraylist
        //keuze om er 5 te maken en dan enkel te werken met de laatste 4
        ArrayList<Speler> spelers = new ArrayList<Speler>();
        for(int i = 0; i<5; i++){
            spelers.add(new Speler());
        }



        for(int spelId = 0; spelId < aantalSpelletjes; spelId++) {
            //behandeling per spel
            // System.out.println("nieuw spel : spel id = "+spelId);

            //resetten van de spelers
            for (Speler s : spelers) {
                s.reset();
            }
            //System.out.println("de spelers zijn gereset");

            //inlezen van de begingegevens
            aantalMogPersonen = sc.nextInt();
            aantalMogLocaties = sc.nextInt();
            aantalMogWapens = sc.nextInt();
            aantalEntries = sc.nextInt();

            //berekening van aantal kaartjes per persoon
            aantalKaartjesPerPersoon = (aantalMogLocaties + aantalMogPersonen + aantalMogWapens - 3) / 4;

            /*System.out.println("personen: "+aantalMogPersonen);
            System.out.println("locaties: "+aantalMogLocaties);
            System.out.println("wapens: "+aantalMogWapens);
            System.out.println("vragen: "+aantalEntries);
            System.out.println("# kaartjes pp: "+aantalKaartjesPerPersoon);*/

            //weggegooide string want het eerste leest hij in als einde van de lijn
            vraag = sc.nextLine();

            for (int j = 0; j < aantalEntries; j++) {
                //inlezen van alle vragen en antwoorden
                vraag = sc.nextLine();


                //System.out.println("vraag: "+vraag);
                //welk formaat -> string

                vraagSteller = ((int) (vraag.charAt(0))) - 48;
                k1 = vraag.charAt(2);
                k2 = vraag.charAt(3);
                k3 = vraag.charAt(4);
                antwoorder = vraag.charAt(6);

                /*System.out.println("is de conversie correct gebeurd?");
                System.out.println("vraagsteller: " + vraagSteller);
                System.out.println("kaartjes:" + k1+k2+k3);
                System.out.println("antwoorder: "+antwoorder);
                System.out.println(""); */


                //verwerken van de vraag

                //als het antwoord X is , hebben geen van de 3 andere spelers een kaartje van het spel
                if (antwoorder == 'X') {

                    String andereSpelers = genereerTOVSpelers(vraagSteller);
                    //    System.out.println("andere spelers: "+andereSpelers);

                    for (int i = 0; i < andereSpelers.length(); i++) {
                        int spelersId = andereSpelers.charAt(i) - 48;
                        spelers.get(spelersId).voegZekerNietKaartjesToe(k1, k2, k3);
                    }

                } else {
                    //nu is de antwoordern en zowieso een getal, dus mogen we deze converteren
                    int antwrdr = antwoorder - 48;
                    //als een speler antwoord vb vraagsteller = 2, antwoorder = 1
                    String tussenLiggendeSpelers = genereerTussenliggendeSpelers(vraagSteller, antwrdr);
                    //dan hebben alle tussenliggendespelers geen van de 3 kaartjes
                    //dan heeft antwoorder (1) minstens 1 van de 3 kaartjes

                    //nog opletten als de string leeg is, wat gebeurt der dan? best debuggen

                    //andtwoorder heeft 1 van de 3 kaartjes
                    spelers.get(antwrdr).voegComboMinstensEenToe(k1, k2, k3);

                    //tussenliggende spelers hebben deze kaartjes zeker niet
                    for (int i = 0; i < tussenLiggendeSpelers.length(); i++) {
                        spelers.get(tussenLiggendeSpelers.charAt(i) - 48).voegZekerNietKaartjesToe(k1, k2, k3);
                    }
                }
            }
            //nu zijn alle vragen en gegevens ingelezen

            //verdere verwerking van de hashlijsten
            /*System.out.println("testing sets");
            for(int i=1; i<spelers.size(); i++){
                System.out.println("spelerid" + i);
                spelers.get(i).printLijsten();
                System.out.println();
                System.out.println();
            }*/

            //voor elke speler de kaartjes die hij zeker niet heeft halen uit de combinaties
            //opgelet: enkel voor de lijsten van de speler zelf
            for (int i = 1; i < spelers.size(); i++) {
                    spelers.get(i).filter();
                    //3x want deze methode kan maar 1 aanpassing per keer doen voor elke 3 char tellende string

                /*System.out.println("na het filteren van de eigen kaartjes");
                System.out.println("speler "+i+": ");*/
                spelers.get(i).vindKaartjes();
                /*System.out.println("///////////////////////");
                System.out.println("///////////////////////");
                System.out.println("///////////////////////");
                System.out.println("///////////////////////");
                System.out.println("na het vinden van de kaartjes");*/
            }

            /*System.out.println("//////////////////////////////////////////////");
            for (int i = 1; i < spelers.size(); i++) {
                System.out.println("spelerid" + i);
                spelers.get(i).printLijsten();
                System.out.println();
                System.out.println();
            }*/
            


            boolean [] spKlaar = new boolean[5];
            for(int i =0 ; i< 5 ; i++){spKlaar[i]=false;}
            boolean spelerklaar = false;

            while (!spKlaar[1] || !spKlaar[2] || !spKlaar[3] || !spKlaar[4]) {
                for(int j=1 ; j<5 ; j++){
                    spelerklaar = spKlaar[j];
                    if(!spelerklaar){
                        String andereSpelers = genereerTOVSpelers(j);

                        for(int i=0; i<andereSpelers.length(); i++) {
                            int spelersId = andereSpelers.charAt(i) - 48;

                            spelers.get(j).voegAndermansKaartjesAanEigenZekerNiet(spelers.get(spelersId).getGevondenKaartjes());
                        }
                        spelers.get(j).filter();


                        spelers.get(j).vindKaartjes();

                        if(spelers.get(j).getAantalGevondenKaartjes()==aantalKaartjesPerPersoon){
                            spKlaar[j]=true;
                        }
                    }
                }

            //eind van de while lus
            }

            //nu zouden alle kaartjes moeten gevonden zijn
            /*for (int i = 1; i < spelers.size(); i++) {
                System.out.println("spelerid" + i);
                spelers.get(i).printLijsten();
                System.out.println();
                System.out.println();
            }*/


            //genereren van de output
            antwoord.append(spelId+1);
            antwoord.append(" ");

            for(int i=1; i<5 ; i++){
                Speler s = spelers.get(i);

                List sortedList = new ArrayList(s.getGevondenKaartjes());
                Collections.sort(sortedList);

                StringBuilder kaartjesLijst = new StringBuilder();
                for(Object o : sortedList){kaartjesLijst.append(o);}


                antwoord.append(kaartjesLijst);
                if(i!=4){antwoord.append(" ");}
            }
            if(spelId != aantalSpelletjes-1) {
                antwoord.append("\n");
            }


            //einde van 1 spelletje
        }
        System.out.println(antwoord.toString());



    }

    private static String genereerTussenliggendeSpelers(int vraagSteller, int antwoorder) {
        //als 1 en 3 -> 2
        //als 3 en 2 -> 41
        int antwrdr = antwoorder;

        int teller = vraagSteller;

        StringBuilder sb = new StringBuilder();
            teller += 1;
            if(teller == 5){teller = 1;}
        while (teller != antwrdr){
                sb.append(teller);
                teller++;
                if(teller ==5){teller = 1;}
        }

        return sb.toString();
    }


    private static String genereerTOVSpelers(int vraagSteller) {
        // genereert een string met de tegenovergestelde spelers
        // als 1 -> 234, als 2 -> 341, als 3 -> 412, als 4 -> 123
        switch (vraagSteller){
            case 1: return "234";
            case 2: return "134";
            case 3: return "124";
            case 4: return "123";
            default: return "00";
        }
    }
    
}




class Speler {


    private int aantalGevondenKaartjes;

    private HashSet<Character> zekereKaartjes;
    private HashSet<Character> zekerNietKaartjes;
    private HashSet<String> combinatiesMinstensEen;

    public Speler(){

        aantalGevondenKaartjes=0;
        zekereKaartjes = new HashSet<Character>();
        zekerNietKaartjes = new HashSet<Character>();
        combinatiesMinstensEen = new HashSet<String>();
    }

    public void reset(){
        zekereKaartjes.clear();
        zekerNietKaartjes.clear();
        aantalGevondenKaartjes=0;
        combinatiesMinstensEen.clear();
    }

    /**
     * bij een X als antwoord, zijn de 3 kaartjes zeker niet in het bezit van deze speler
     *
     * @param k1 kaartje1
     * @param k2 kaartje2
     * @param k3 kaartje3
     */
    public void voegZekerNietKaartjesToe(char k1, char k2, char k3) {
        zekerNietKaartjes.add(k1);
        zekerNietKaartjes.add(k2);
        zekerNietKaartjes.add(k3);
    }

    /**
     * voegt de 3 kaartjes waarvan en minstens 1 juist is toe als 1 string
     *
     * @param k1 kaartje1
     * @param k2 kaartje2
     * @param k3 kaartje3
     */
    public void voegComboMinstensEenToe(char k1, char k2, char k3) {
        //voegt de gehele set van 3 kaartjes aan de combinatieset toe
        //voegt de 3 kaartjes ook onafhankelijk toe aan minstens 1
        StringBuilder sb = new StringBuilder();
        sb.append(k1);
        sb.append(k2);
        sb.append(k3);
        combinatiesMinstensEen.add(sb.toString());
    }

    /**
     *  //verwijderen van de kaartjes die in zeker niet zitten EN in een combinatie zitten
     */
    public void filterCombinaties(){

        HashSet<String> nieuweSet = new HashSet<String>();
        nieuweSet.addAll(combinatiesMinstensEen);

        //voor elk kaartje in de zekerNietKaartjes set
        for( String s : combinatiesMinstensEen ) {

            //itereer over alle combinaties waarvan er minstens 1 in de combinatie zit (de lijst met strings)
            for( Character c : zekerNietKaartjes) {

                //als deze in de string zit, moet je deze eruit halen
                if(s.contains(c.toString())){
                    //haal de string uit de combinatieMinstensEen set
                    nieuweSet.remove(s);

                    //haal het karakter uit deze string
                    StringBuilder sb = new StringBuilder(s);
                    int plaats = sb.indexOf(c.toString());
                    sb.deleteCharAt(plaats);

                    //voeg de nieuwe string terug toe aan de set
                    nieuweSet.add(sb.toString());

                }
            }

        }
        combinatiesMinstensEen = nieuweSet;
    }

    /**
     * zoeken in de set waarvan 1ervan zeker in de set zit
     *      als we een string tegenkomen met maar 1 kaartje in
     *      dan is dit een kaartje die de speler heeft,
     *
     *      voeg dit kaartje dan ook toe aan de gevondenset
     *      verhoog aantalgevondenkaartjes
     */
    public void vindKaartjes(){
        //itereer over elke string van minstensEenKaartje
        for( String s : combinatiesMinstensEen){

            //als de string maar 1 karakter meer bevat
            if(s.length()==1){
                zekereKaartjes.add(s.charAt(0));
                aantalGevondenKaartjes = zekereKaartjes.size();
            }

        }

    }

    public int getAantalGevondenKaartjes(){return aantalGevondenKaartjes;}

    /**
     * printmethode om te checken wat in elke speler zijn lijsten zit
     */
    public void printLijsten() {
        System.out.println("lijst met zekerNietKaartjes");
        for(Character c : zekerNietKaartjes){
            System.out.println(c);
        }

        System.out.println();
        System.out.println("lijst waarvan minstens 1 er in zit");
        for(String s : combinatiesMinstensEen){
            System.out.println(s);
        }

        /*System.out.println("lijst met gevonden kaartjes");
        for(Character c : getGevondenKaartjes()){
            System.out.println(c);
        }*/
    }

    public HashSet<Character> getGevondenKaartjes(){
        return zekereKaartjes;
    }

    public void voegAndermansKaartjesAanEigenZekerNiet(HashSet<Character> andermansGevondenKaartjes) {
        for (Character c : andermansGevondenKaartjes){zekerNietKaartjes.add(c);}
    }

    /**
     * //voert de methode filtercombinaties gewoon 3x uit
     */
    public void filter() {
        for(int i =0; i<3 ; i++){
            filterCombinaties();
        }
    }

}


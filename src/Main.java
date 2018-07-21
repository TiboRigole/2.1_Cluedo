import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc =new Scanner(System.in);

    //reserveren van het aantal variabelen

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


        //voor ieder spel
        for(int spelId = 0; spelId < aantalSpelletjes; spelId++) {

            //resetten van de spelers
            for (Speler s : spelers) {
                s.reset();
            }
            
            //inlezen van de begingegevens
            aantalMogPersonen = sc.nextInt();
            aantalMogLocaties = sc.nextInt();
            aantalMogWapens = sc.nextInt();
            aantalEntries = sc.nextInt();

            //berekening van aantal kaartjes per persoon
            aantalKaartjesPerPersoon = (aantalMogLocaties + aantalMogPersonen + aantalMogWapens - 3) / 4;

            //weggegooide string want het eerste leest hij in als einde van de lijn
            vraag = sc.nextLine();

            //voor iedere vraag
            for (int j = 0; j < aantalEntries; j++) {
            	
                //inlezen 1 vraag en antwoord
                vraag = sc.nextLine();

                //conversie van de input naar variabelen
                vraagSteller = ((int) (vraag.charAt(0))) - 48;
                k1 = vraag.charAt(2);
                k2 = vraag.charAt(3);
                k3 = vraag.charAt(4);
                antwoorder = vraag.charAt(6);
                
                //verwerken van de vraag
                
                //als het antwoord X is, hebben de 3 andere spelers geen kaartjes van de gevraagde
                if (antwoorder == 'X') {

     
               	 	//genereren van de andere spelers
                		//als speler 3 de vraagsteller is krijgen we 124
                		//als speler 4 de vraagsteller is krijgen we 123
                    String andereSpelers = genereerTOVSpelers(vraagSteller);

                    //voor elke andere speler
                    for (int i = 0; i < andereSpelers.length(); i++) {
                    	
                    	// de 3 kaartjes toevoegen aan zijn lijst met kaartjes die hij zeker niet heeft
                        int spelersId = andereSpelers.charAt(i) - 48;
                        spelers.get(spelersId).voegZekerNietKaartjesToe(k1, k2, k3);
                    }
                 
                //einde van wat te doen als het antwoord een 'X' is
                } 
                
                //nu antwoord er dus zowieso een speler, antwoorder is dus zowieso een getal
                else {

                	//conversie naar een int
                    int antwrdr = antwoorder - 48;
                    
                    //genereren van de spelers tussen vrager en antwoorder
                    	//als speler1= vrager, speler4 = antwoorder, dan krijgen we 23,
                    	//als speler 3 vrager, speler2 = antwoorder, dan krijgen we 41
                    	//als speler 3 vrager, speler4 = antwoorder, dan krijgen we een lege string
                    String tussenLiggendeSpelers = genereerTussenliggendeSpelers(vraagSteller, antwrdr);

                    //de antwoorder heeft minstens 1 van de 3 kaartjes
                    spelers.get(antwrdr).voegComboMinstensEenToe(k1, k2, k3);

                    //de tussenliggendespelers hebben geen van de 3 kaartjes
                    for (int i = 0; i < tussenLiggendeSpelers.length(); i++) {
                        spelers.get(tussenLiggendeSpelers.charAt(i) - 48).voegZekerNietKaartjesToe(k1, k2, k3);
                    }
                    
                //einde van wat te doen als de antwoorder een getal is
                }

            //einde van het inlezen van de gegevens
            }
            //nu zijn alle vragen en gegevens ingelezen

            
            //verdere verwerking van de hashSets
            
            //voor elke speler
            for (int i = 1; i < spelers.size(); i++) {
            	
        		//als de speler kaartje 'a' zeker niet heeft, maar het zit nog steeds in 1 van de combinaties
        		//dan mag de character 'a' uit deze combinatie gehaald worden
                spelers.get(i).filter();
                //opgelet: enkel voor de lijsten van de speler zelf

                //als er combinaties zijn die maar uit 1 char bestaan, dan is dit een kaartje die de speler heeft
                //toevoegen van deze kaartjes aan de set met gevondenkaarten
                spelers.get(i).vindKaartjes();

            }
            
            //booleans : als alle kaartjes van de speler gevonden zijn dan zetten we deze op true
            boolean [] spKlaar = new boolean[5];
            
            //deze booleans allemaal op false zetten
            for(int i =0 ; i< 5 ; i++){spKlaar[i]=false;}
            
            //een variabele die we zullen gebruiken
            boolean spelerklaar = false;

            //zolang niet alle kaartjes van alle spelers gevonden zijn
            while (!spKlaar[1] || !spKlaar[2] || !spKlaar[3] || !spKlaar[4]) {
            	
            	//overloop alle spelers
                for(int j=1 ; j<5 ; j++){
                	
                    spelerklaar = spKlaar[j];
        
                	//als de speler nog niet klaar is
                    if(!spelerklaar){
                    	
                    	//kijk naar de 3 andere spelers
                        String andereSpelers = genereerTOVSpelers(j);

                        //deze 3 andere spelers overlopen
                        for(int i=0; i<andereSpelers.length(); i++) {
                            int spelersId = andereSpelers.charAt(i) - 48;

                            //als speler 1 een kaartje a heeft gevonden, mag het uit de combinaties van de andere spelers gehaald worden
                            spelers.get(j).voegAndermansKaartjesAanEigenZekerNiet(spelers.get(spelersId).getGevondenKaartjes());
                        }
                        
                        //opnieuw de combinaties checken, als ze maar uit 1 karakter bestaan vinden we het kaartje
                        spelers.get(j).filter();
                        spelers.get(j).vindKaartjes();

                        //als de speler nu wel klaar is, zet zijn overeenkomstige boolean klaar op true
                        if(spelers.get(j).getAantalGevondenKaartjes()==aantalKaartjesPerPersoon){
                            spKlaar[j]=true;
                        }
                        
                    }
                    
                }

            //eind van de while lus
            }

            //nu zijn alle kaartjes gevonden
            
            
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
        
        //printen van de output
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

	//attributen
    private int aantalGevondenKaartjes;
    private HashSet<Character> zekereKaartjes;
    private HashSet<Character> zekerNietKaartjes;
    private HashSet<String> combinatiesMinstensEen;

    //constructoren
    public Speler(){
        aantalGevondenKaartjes=0;
        zekereKaartjes = new HashSet<Character>();
        zekerNietKaartjes = new HashSet<Character>();
        combinatiesMinstensEen = new HashSet<String>();
    }

    //getters
    public int getAantalGevondenKaartjes(){return aantalGevondenKaartjes;}
    
    public HashSet<Character> getGevondenKaartjes(){
        return zekereKaartjes;
    }
    
    /**
     * bij een nieuw spelletje maken we geen nieuwe variabelen aan
     * we wissen de inhoud van het oude spelletje
     */
    public void reset(){
        zekereKaartjes.clear();
        zekerNietKaartjes.clear();
        aantalGevondenKaartjes=0;
        combinatiesMinstensEen.clear();
    }

    /**
     * bij een X als antwoord, zijn de 3 kaartjes zeker niet in het bezit van deze speler
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

    /**
     * printmethode om te checken wat in elke speler zijn lijsten zit
     * wordt niet gebruikt in de main, wel om te debuggen
     */
    /*public void printLijsten() {
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
        }
    }*/

    //spreekt voor zich
    public void voegAndermansKaartjesAanEigenZekerNiet(HashSet<Character> andermansGevondenKaartjes) {
        for (Character c : andermansGevondenKaartjes){zekerNietKaartjes.add(c);}
    }

    public void filter() {
        for(int i =0; i<3 ; i++){
        	//deze methode kan maar 1 aanpassing per keer doen voor elke 3 char tellende string
        	//daarom voeren we ze 3 keer uit
            filterCombinaties();
            
        }
    }

}


import java.util.HashSet;

public class Speler {


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

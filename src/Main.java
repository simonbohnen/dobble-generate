import java.util.ArrayList;

/**
 * Created by Simon on 04.01.2017.
 * Provides methods for generating the configuration with least different symbols for a given card number and number of symbols per card
 */

class Main {
    private static Karte bestSet[];
    private static int bestSymbolCount = -1; //value indicates that no minimum has been found yet

    public static void main(String args[]) {
        int cards = 14;
        int symbolsPerCard = 4;
        long earl2 = System.nanoTime();
        System.out.println(cards + " Karten, " + symbolsPerCard + " Symbole: " + getMinSymbolCount2(cards, symbolsPerCard, false));
        long late2 = System.nanoTime();
        System.out.println("Time elapsed using new method: " + ((late2 - earl2) / 1000000000.0) + " seconds");
        printCardSet(bestSet);
        /*
        for(int i = 1; i < 8; ++i) {
            StringBuilder s = new StringBuilder();
            for (int i2 = 1; i2 < 8; ++i2) {
                //System.out.println(cards + " Karten, " + symbolsPerCard + " Symbole: " + getMinSymbolCount2(cards, symbolsPerCard, false));
                s.append(" ").append(getMinSymbolCount(i2, i, false)).append(" ");
                //printCardSet(bestSet);
                bestSymbolCount = -1;
            }
            System.out.println(s);
        }
        */
    }

    private static void printCardSet(Karte cards[]) {
        for(int i = 0; i < cards[0].getSymbolCount(); ++i) {
            StringBuilder s = new StringBuilder();
            for (Karte aBestSet : cards) {
                s.append(" ").append(aBestSet.getSymbol(i)).append(" ");
            }
            System.out.println(s);
        }
        System.out.println();
    }

    //Version 2
    private static final ArrayList<GenState> states = new ArrayList<>();

    @SuppressWarnings("SameParameterValue")
    private static int getMinSymbolCount2(int cards, int symbolsPerCard, boolean doPrinting) {
        if(cards == 0) {
            return 0;
        }
        if(symbolsPerCard == 0) {
            return 0;
        }
        Karte karten[] = new Karte[cards];
        for(int i = 0; i < cards; ++i) {
            karten[i] = new Karte(symbolsPerCard);
        }
        //Die erste Karte kann mit den Zahlen 1 - symbolsPerCard (einschließlich) gefüllt werden
        int starterpack[] = new int[symbolsPerCard];
        for(int i = 0; i < symbolsPerCard; ++i) {
            starterpack[i] = i + 1;
        }
        karten[0] = new Karte(starterpack);

        int filledCards = 1;
        int symbolCountSoFar = symbolsPerCard;
        //Falls es mehr als eine Karte gibt, kann die zweite karte nach einem standardschema befüllt werden, das es keine anderen auswahlmöglichkeiten gibt
        if(cards > 1) {
            int starterpack2[] = new int[symbolsPerCard];
            starterpack2[0] = 1;
            for(int i1 = 1; i1 < symbolsPerCard; ++i1) {
                starterpack2[i1] = symbolsPerCard + i1;
            }
            filledCards = 2;
            symbolCountSoFar = symbolsPerCard * 2 - 1;
            karten[1] = new Karte(starterpack2);
        }
        boolean satisfied[] = new boolean[cards];

        recGetMinSymbolCount2(karten, symbolsPerCard, cards, filledCards, 0, symbolCountSoFar, satisfied, doPrinting);
        while(!states.isEmpty()) {
            states.sort((a, b) -> a.score < b.score ? -1 : a.score == b.score ? (a.filledCards * symbolsPerCard + a.filledSymbols > b.filledCards * symbolsPerCard + b.filledSymbols ? 1 : a.filledCards * symbolsPerCard + a.filledSymbols == b.filledCards * symbolsPerCard + b.filledSymbols? 0 : -1) : 1); //subtraction trick will work because of small numbers
            GenState next = states.get(states.size() - 1);
            recGetMinSymbolCount2(next.cards, next.symbolsPerCard, next.cardCount, next.filledCards, next.filledSymbols, next.symbolCountSoFar, next.satisfied, doPrinting);
            states.remove(next);
            if(bestSymbolCount != -1) {
                states.removeIf(s -> s.symbolCountSoFar >= bestSymbolCount);
                /*
                if (states.get(i).symbolCountSoFar >= bestSymbolCount) {
                    states.removeAll(states.subList(i, states.size() - 1));
                    break;
                }
                */
            }
            //System.out.println(states.size());
        }
        return bestSymbolCount;
    }

    /*
     * Sollte nur von getMinSymbolCount aufgerufen werden
     * Liefert die minimale Anzahl an unterschiedlichen Symbolen bei vorgegebenen Bedingungen (2. Version)
     * Parameter:
     * cards: Array an Karten, die bis hierhin generiert wurden
     * symbolsPerCard: Anzahl der Symbole, die eine voll ausgefüllte Karte trägt
     * cardCount: die Anzahl der Karten, die am Ende ausgefüllt werden sollen
     * filledCards: die Anzahl der bis jetzt komplett ausgefüllten Karten
     * filledSymbols: die Anzahl der ausgefüllten Symbole auf der aktuell bearbeiteten Karte
     * symbolCountSoFar: Die Anzahl der Symbole, die bis jetzt benutzt wurden (gleichbedeutend mit dem höchsten bis jetzt benutzten Symbol)
     * satisfied: gibt für jede Karte an, ob diese durch die aktuell zu füllende Karte schon "befriedigt" ist
     */
    private static void recGetMinSymbolCount2(Karte cards[], int symbolsPerCard, int cardCount, int filledCards, int filledSymbols, int symbolCountSoFar, boolean satisfied[], boolean doPrinting) {
        //printCardSet(cards);
        //wurde soeben eine neue Karte fertiggestellt, so wird dieser block durchlaufen
        if(filledSymbols == symbolsPerCard) {
            filledSymbols = 0;
            filledCards++;
            satisfied = new boolean[cardCount];
        }
        //alle karten wurden befüllt
        if(filledCards == cardCount) {
            //neuer Rekord?
            if(symbolCountSoFar < bestSymbolCount || bestSymbolCount == -1) {
                bestSymbolCount = symbolCountSoFar;
                bestSet = cards;
                if(doPrinting) {
                    System.out.println("New best: Only " + symbolCountSoFar + " symbols!");
                    printCardSet(cards);
                }
            }
            return;
        }
        //der index der ersten karte, die noch nicht 'befriedigt' wurde
        int i = 0;
        while(satisfied[i]) {
            ++i;
        }
        if(i == filledCards) {
            //es wurden bereits alle befüllten karten befriedigt und die aktuelle karte muss mit neuen symbolen aufgefüllt werden, außer diese config ist schon zu kacke
            if(bestSymbolCount == -1 || symbolCountSoFar < bestSymbolCount) {
                //Falls das Auffüllen über das aktuelle minimum hinausgeht (oder gleich ist), wird abgebrochen
                if(symbolCountSoFar + symbolsPerCard - filledSymbols >= bestSymbolCount && bestSymbolCount != -1) {
                    return;
                }
                //Kopieren des Kartenarrays
                Karte ncards[] = new Karte[cards.length];
                for(int r = 0; r < cards.length; ++r) {
                    ncards[r] = cards[r].getCopy();
                }
                //Die restlichen karten werden mit neuen symbolen befüllt
                for(int i3 = filledSymbols; i3 < symbolsPerCard; ++i3) {
                    ncards[filledCards].setSymbol(i3, symbolCountSoFar + i3 - filledSymbols + 1);
                }
                if(filledCards == cardCount - 1) {
                    //Alle Karten wurden befüllt
                    if(bestSymbolCount == -1 || symbolCountSoFar + symbolsPerCard - filledSymbols < bestSymbolCount) {
                        bestSymbolCount = symbolCountSoFar + symbolsPerCard - filledSymbols;
                        bestSet = ncards;
                        if(doPrinting) {
                            System.out.println("New best: Only " + (symbolCountSoFar + symbolsPerCard - filledSymbols) + " symbols!");
                            printCardSet(ncards);
                        }
                    }
                } else {
                    states.add(new GenState(ncards, symbolsPerCard, cardCount, filledCards, symbolsPerCard, symbolCountSoFar + symbolsPerCard - filledSymbols, satisfied));
                }
            }
        } else {
            for (int j = 0; j < symbolsPerCard; ++j) {
                boolean possibleToSetj = true;
                //Das symbol, das versucht wird, zu setzen
                int symbolToSet = cards[i].getSymbol(j);
                //Falls eine andere karte bereits befriedigt ist und das zu setzende symbol hat, gehts nicht
                for (int k = 0; k < filledCards && possibleToSetj; ++k) {
                    if (cards[k].hasSymbol(symbolToSet) && satisfied[k]) {
                        possibleToSetj = false;
                    }
                }
                if(possibleToSetj) {
                    Karte ncards[] = new Karte[cards.length];
                    for(int r = 0; r < cards.length; ++r) {
                        ncards[r] = cards[r].getCopy();
                    }
                    ncards[filledCards].setSymbol(filledSymbols, symbolToSet);
                    boolean goodCard = true;
                    //Falls die aktuelle karte voll ist, wird überprüft, ob sie alle anderen karten befriedigt
                    if(filledSymbols == symbolsPerCard - 1) {
                        for(int l = 0; l < filledCards; ++l) {
                            if(!satisfied[l] && !cards[l].hasSymbol(symbolToSet)) {
                                goodCard = false;
                            }
                        }
                    }
                    //falls die karte gut war, wird weiter rumprobiert
                    if(goodCard) {
                        //Die neuen befriedigungen werden ermittelt
                        boolean nsatisfied[] = new boolean[satisfied.length];
                        System.arraycopy(satisfied, 0, nsatisfied, 0, satisfied.length);
                        for(int m = 0; m < filledCards; ++m) {
                            if(cards[m].hasSymbol(symbolToSet)) { //cards[m].getMaxSymbol() >= symbolToSet && wurde schon in hasSymbol berücksichtigt
                                nsatisfied[m] = true;
                            }
                        }
                        recGetMinSymbolCount2(ncards, symbolsPerCard, cardCount, filledCards, filledSymbols + 1, symbolCountSoFar, nsatisfied, doPrinting);
                        if(symbolCountSoFar >= bestSymbolCount && bestSymbolCount != -1) {
                            return;
                        }
                    }
                }
                //Difficult because of states
                /*
                double percincr = Math.pow(1.0 / symbolsPerCard, (filledCards - 1) * symbolsPerCard + filledSymbols + 1) * 100.0;
                percentDone += percincr;
                if(percincr > 0.001) {
                    System.out.println(percentDone + " percent done!");
                }
                */
            }
        }
    }
}

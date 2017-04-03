/**
 * Created by Simon on 04.01.2017.
 */
public class Main {
    private static Karte bestSet[];
    private static int bestSymbolCount = -1; //value indicates that no minimum has been found yet

    public static void main(String args[]) {
        //for(int i = 1; i < 18; ++i) {
            for(int i2 = 1; i2 < 11; ++i2) {
                System.out.println(i2 + " Karten, " + 2 + " Symbole: " + getMinSymbolCount(i2, 2));
                printBestSet();
                bestSymbolCount = -1;
            }
        //}
    }

    private static void printBestSet() {
        for(int i = 0; i < bestSet[0].getSymbolCount(); ++i) {
            StringBuilder s = new StringBuilder();
            for (Karte aBestSet : bestSet) {
                s.append(" ").append(aBestSet.getSymbol(i)).append(" ");
            }
            System.out.println(s);
        }
    }

    private static int getMinSymbolCount(int cards, int symbolsPerCard) {
        if(cards == 0) {
            return 0;
        }
        if(symbolsPerCard == 0) {
            return -1;
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
        boolean satisfied[] = new boolean[cards];
        return recGetMinSymbolCount(karten, symbolsPerCard, cards, 1, 0, symbolsPerCard, satisfied, 0);
    }

    /*
     * Sollte nur von getMinSymbolCount aufgerufen werden
     * Liefert die minimale Anzahl an unterschiedlichen Symbolen bei vorgegebenen Bedingungen
     * Parameter:
     * cards: Array an Karten, die bis hierhin generiert wurden
     * symbolsPerCard: Anzahl der Symbole, die eine voll ausgefüllte Karte trägt
     * cardCount: die Anzahl der Karten, die am Ende ausgefüllt werden sollen
     * filledCards: die Anzahl der bis jetzt komplett ausgefüllten Karten
     * filledSymbols: die Anzahl der ausgefüllten Symbole auf der aktuell bearbeiteten Karte
     * symbolCountSoFar: Die Anzahl der Symbole, die bis jetzt benutzt wurden (gleichbedeutend mit dem höchsten bis jetzt benutzten Symbol)
     * satisfied: gibt für jede Karte an, ob diese durch die aktuell zu füllende Karte schon "befriedigt" ist
     */
    private static int recGetMinSymbolCount(Karte cards[], int symbolsPerCard, int cardCount, int filledCards, int filledSymbols, int symbolCountSoFar, boolean satisfied[], double percentDone) {
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
                System.out.println("New best: Only " + symbolCountSoFar + " symbols!");
                printBestSet();
            }
            return symbolCountSoFar;
        }
        //der index der ersten karte, die noch nicht 'befriedigt' wurde
        int i = 0;
        while(satisfied[i]) {
            ++i;
        }
        if(i == filledCards) {
            //es wurden bereits alle befüllten karten befriedigt und die aktuelle karte muss mit neuen symbolen aufgefüllt werden, außer diese config ist schon zu kacke
            if(bestSymbolCount != -1 && symbolCountSoFar >= bestSymbolCount) {
                return -1;
            } else {
                //Kopieren des Kartenarrays
                Karte ncards[] = new Karte[cards.length];
                for(int r = 0; r < cards.length; ++r) {
                    ncards[r] = cards[r].getCopy();
                }
                //Die restlichen karten werden mit neuen symbolen befüllt
                int filledUpCards = 0;
                for(int i3 = filledSymbols; i3 < symbolsPerCard; ++i3) {
                    ncards[filledCards].setSymbol(i3, symbolCountSoFar + i3 - filledSymbols + 1);
                    ++filledUpCards;
                }
                return recGetMinSymbolCount(ncards, symbolsPerCard, cardCount, filledCards, symbolsPerCard, symbolCountSoFar + filledUpCards, satisfied, percentDone);
            }
        } else {
            int curmin = -1;
            for (int j = 0; j < symbolsPerCard; ++j) {
                boolean possibleToSetj = true;
                //Das symbol, das versucht wird, zu setzen
                int symbolToSet = cards[i].getSymbol(j);
                //Falls eine andere karte bereits befriedigt ist und das zu setzende symbol hat, gehts nicht
                for (int k = 0; k < filledCards; ++k) {
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
                        int possibleMinimum = recGetMinSymbolCount(ncards, symbolsPerCard, cardCount, filledCards, filledSymbols + 1, symbolCountSoFar, nsatisfied, percentDone);
                        if(possibleMinimum != -1) {
                            if(curmin == -1) {
                                curmin = possibleMinimum;
                            } else {
                                if (possibleMinimum < curmin) {
                                    curmin = possibleMinimum;
                                }
                            }
                            if(symbolCountSoFar == curmin) {
                                return curmin;
                            }
                        }
                    }
                }
                double percincr = Math.pow(1.0 / symbolsPerCard, (filledCards - 1) * symbolsPerCard + filledSymbols + 1) * 100.0;
                percentDone += percincr;
                if(percincr > 0.001) {
                    //System.out.println(percentDone + " percent done!");
                }
            }
            return curmin;
        }
    }
}

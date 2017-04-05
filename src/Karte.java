/**
 * Created by Simon on 04.01.2017.
 * Represents a card in the game Dobble
 */
class Karte {
    private int[] symbols;
    private boolean[] occurences;
    private int maxSymbol = 0;

    Karte(int newSize) {
        symbols = new int[newSize];
        occurences = new boolean[1];
    }

    //Annahme: alle Symbole sind größer als null
    Karte(int nsymbols[]) {
        symbols = new int[nsymbols.length];
        symbols = nsymbols;
        for (int symbol : symbols) {
            if (symbol > maxSymbol) {
                maxSymbol = symbol;
            }
        }
        occurences = new boolean[maxSymbol + 1];
        for (int symbol : symbols) {
            occurences[symbol] = true;
        }
    }

    Karte getCopy() {
        Karte nkarte = new Karte(symbols.length);
        System.arraycopy(symbols, 0, nkarte.symbols, 0, symbols.length);
        nkarte.maxSymbol = maxSymbol;
        nkarte.occurences = new boolean[maxSymbol + 1];
        System.arraycopy(occurences, 0, nkarte.occurences, 0, maxSymbol + 1);
        return nkarte;
    }

    void setSymbol(int pos, int symbol) {
        symbols[pos] = symbol;
        if(symbol > maxSymbol) {
            boolean newoccurences[] = new boolean[symbol + 1];
            System.arraycopy(occurences, 0, newoccurences, 0, maxSymbol + 1);
            maxSymbol = symbol;
            occurences = newoccurences;
        }
        occurences[symbol] = true;
    }

    /* Wird eigentlich nicht gebraucht
    public void setSymbols(int nsymbols[]) {
        symbols = nsymbols;
    }
    */

    int getSymbol(int index) {
        if(index > -1 && index < symbols.length) {
            return symbols[index];
        } else {
            return -1;
        }
    }

    int getSymbolCount() {
        return symbols.length;
    }

// --Commented out by Inspection START (05.04.2017 14:31):
//    private int getMaxSymbol() {
//        return maxSymbol;
//    }
// --Commented out by Inspection STOP (05.04.2017 14:31)

// --Commented out by Inspection START (05.04.2017 14:31):
//    //Prüft, ob eine Karte nur Symbole trägt, die größer als null sind (ob die Karte vollständig besetzt ist)
//    //und ob die Karte kein Symbol doppelt trägt
//    private boolean isValid() {
//        boolean hasOccured[] = new boolean[maxSymbol + 1];
//        for(int i = 0; i < symbols.length; ++i) {
//            if(symbols[i] <= 0) {
//                return false;
//            }
//            if(hasOccured[symbols[i]]) {
//                return false;
//            }
//            hasOccured[symbols[i]] = true;
//        }
//        return true;
//    }
// --Commented out by Inspection STOP (05.04.2017 14:31)

// --Commented out by Inspection START (05.04.2017 14:30):
//    // Gibt an, ob zwei Karten genau ein gemeinsames Symbol haben. Annahme: Beide Karten sind "Valid"
//    public boolean fitTogether(Karte card) {
//        //boolean occuredHere[] = new boolean[maxSymbol];
//        boolean occuredThere[] = new boolean[card.getMaxSymbol() + 1];
//        boolean haveCommon = false;
//        for(int i = 0; i < card.getSymbolCount(); ++i) {
//            occuredThere[card.getSymbol(i)] = true;
//        }
//        for(int i = 0; i < symbols.length; ++i) {
//            if(symbols[i] <= card.getMaxSymbol() && occuredThere[symbols[i]]) {
//                if(haveCommon) {
//                    return false;
//                } else {
//                    haveCommon = true;
//                }
//            }
//        }
//        return haveCommon;
//    }
// --Commented out by Inspection STOP (05.04.2017 14:30)

// --Commented out by Inspection START (05.04.2017 14:30):
//    //Prüft ein Kartendeck auf seine Gültigkeit
//    //nicht feddich
//    public static boolean isValidSet(Karte cards[]) {
//        for(int i = 0; i < cards.length; ++i) {
//            if(!cards[i].isValid()) {
//                return false;
//            }
//        }
//        return true;
//    }
// --Commented out by Inspection STOP (05.04.2017 14:30)

    boolean hasSymbol(int symbol) {
        return symbol <= maxSymbol && occurences[symbol];
    }
}

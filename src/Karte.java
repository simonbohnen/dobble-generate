/**
 * Created by Simon on 04.01.2017.
 */
public class Karte {
    public int symbols[];
    public boolean occurences[];
    public int maxSymbol = 0;

    public Karte(int newSize) {
        symbols = new int[newSize];
    }

    //Annahme: alle Symbole sind größer als null
    public Karte(int nsymbols[]) {
        symbols = new int[nsymbols.length];
        symbols = nsymbols;
        for(int i = 0; i < symbols.length; ++i) {
            if(symbols[i] > maxSymbol) {
                maxSymbol = symbols[i];
            }
        }
        occurences = new boolean[maxSymbol + 1];
        for(int i = 0; i < symbols.length; ++i) {
            occurences[symbols[i]] = true;
        }
    }

    public Karte getCopy() {
        Karte nkarte = new Karte(symbols.length);
        for(int i = 0; i < symbols.length; ++i) {
            nkarte.symbols[i] = symbols[i];
        }
        nkarte.maxSymbol = maxSymbol;
        nkarte.occurences = new boolean[maxSymbol + 1];
        for(int j = 1; j < maxSymbol + 1; ++j) {
            nkarte.occurences[j] = occurences[j];
        }
        return nkarte;
    }

    public void setSymbol(int pos, int symbol) {
        symbols[pos] = symbol;
        if(symbol > maxSymbol) {
            boolean newoccurences[] = new boolean[symbol + symbol - maxSymbol];
            for(int i = 1; i < maxSymbol + 1; ++i) {
                newoccurences[i] = occurences[i];
            }
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

    public int getSymbol(int index) {
        if(index > -1 && index < symbols.length) {
            return symbols[index];
        } else {
            return -1;
        }
    }

    public int getSymbolCount() {
        return symbols.length;
    }

    public int getMaxSymbol() {
        return maxSymbol;
    }

    //Prüft, ob eine Karte nur Symbole trägt, die größer als null sind (ob die Karte vollständig besetzt ist)
    //und ob die Karte kein Symbol doppelt trägt
    private boolean isValid() {
        boolean hasOccured[] = new boolean[maxSymbol + 1];
        for(int i = 0; i < symbols.length; ++i) {
            if(symbols[i] <= 0) {
                return false;
            }
            if(hasOccured[symbols[i]]) {
                return false;
            }
            hasOccured[symbols[i]] = true;
        }
        return true;
    }

    // Gibt an, ob zwei Karten genau ein gemeinsames Symbol haben. Annahme: Beide Karten sind "Valid"
    public boolean fitTogether(Karte card) {
        //boolean occuredHere[] = new boolean[maxSymbol];
        boolean occuredThere[] = new boolean[card.getMaxSymbol() + 1];
        boolean haveCommon = false;
        for(int i = 0; i < card.getSymbolCount(); ++i) {
            occuredThere[card.getSymbol(i)] = true;
        }
        for(int i = 0; i < symbols.length; ++i) {
            if(symbols[i] <= card.getMaxSymbol() && occuredThere[symbols[i]]) {
                if(haveCommon) {
                    return false;
                } else {
                    haveCommon = true;
                }
            }
        }
        return haveCommon;
    }

    //Prüft ein Kartendeck auf seine Gültigkeit
    //nicht feddich
    public static boolean isValidSet(Karte cards[]) {
        for(int i = 0; i < cards.length; ++i) {
            if(!cards[i].isValid()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasSymbol(int symbol) {
        if(symbol > maxSymbol) {
            return false;
        }
        return occurences[symbol];
    }
}

/**
 * Created by Simon on 06.04.2017.
 * Represents a state during the execution of recGetMinSymbolCount
 */
class GenState {
    //indicates which state should be executed first
    final double score;
    final Karte[] cards; //should eventually be transformed into a CardSet
    final int symbolsPerCard;
    final int cardCount;
    final int filledCards;
    final int filledSymbols;
    final int symbolCountSoFar;
    final boolean[] satisfied;

    GenState(Karte ncards[], int nsymbolsPerCard, int ncardCount, int nfilledCards, int nfilledSymbols, int nsymbolCountSoFar, boolean nsatisfied[]) {
        cards = ncards;
        symbolsPerCard = nsymbolsPerCard;
        cardCount = ncardCount;
        filledCards = nfilledCards;
        filledSymbols = nfilledSymbols;
        symbolCountSoFar = nsymbolCountSoFar;
        satisfied = nsatisfied;
        score = (filledCards * symbolsPerCard + filledSymbols) / (float) symbolCountSoFar;
    }
}

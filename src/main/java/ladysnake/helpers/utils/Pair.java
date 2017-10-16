package ladysnake.helpers.utils;

/**A class that holds two pieces of data, held together like they share a tight bond
 * @param <F> being the type of the first element of the pair
 * @param <L> being the type of the last element of the pair
 */
@SuppressWarnings("WeakerAccess")
public class Pair<F, L>{
    protected F _first;
    protected L _last;

    public Pair(F f, L l){
        this.first(f);
        this.last(l);
    }

    public Pair first(F f){ this._first = f; return this; }
    public F first(){ return this._first; }

    public Pair last(L l){ this._last = l; return this; }
    public L last(){ return this._last; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        return _first.equals(pair._first) && _last.equals(pair._last);
    }

    @Override
    public int hashCode() {
        int result = _first.hashCode();
        result = 31 * result + _last.hashCode();
        return result;
    }

}

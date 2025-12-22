import java.math.BigInteger;

public class Range implements Comparable<Range> {

    IntString start;
    IntString end;

    public Range(IntString start, IntString end) {
        this.start = start;
        this.end = end;
    }

    public BigInteger size() {
        return end.subtract(start).add(BigInteger.ONE);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", start, end);
    }

    public Range unite(Range other) {
        /**
         * If other U this != null, returns null and update this to become the union.
         * Otherwise, return other.
         */
        if (this.compareTo(other) > 0) {
            Range noMatch = other.unite(this);
            if (noMatch != null) // nothing happened, ranges are disjoints.
                return other;
            this.start = other.start;
            this.end = other.end;
            return null;
        }

        if (!other.start.isInRange(start, end))
            return other;

        if (this.end.compareTo(other.end) < 0)
            this.end = other.end;
        return null;
    }

    @Override
    public int compareTo(Range arg0) {

        int compareStarts = start.compareTo(arg0.start);
        return compareStarts == 0 ? end.compareTo(arg0.end) : compareStarts;
    }

}

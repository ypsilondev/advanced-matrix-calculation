package tech.ypsilon.matrix;

import java.util.Objects;

public class FieldNumber implements Comparable<FieldNumber> {

    private int value;
    private final int mod;

    public FieldNumber(int value) {
        this(value, Main.MOD);
    }

    public FieldNumber(int value, int mod) {
        while (value < 0) {
            value += mod;
        }
        this.value = value % mod;
        this.mod = mod;
    }

    public FieldNumber add(FieldNumber nbr) {
        if (this.mod == nbr.mod) {
            return new FieldNumber(this.value + nbr.value, this.mod);
        } else {
            throw new IllegalArgumentException("Modulo not the same");
        }
    }

    public FieldNumber multiply(FieldNumber nbr) {
        if (this.mod == nbr.mod) {
            return new FieldNumber(this.value * nbr.value, this.mod);
        } else {
            throw new IllegalArgumentException("Modulo not the same");
        }
    }

    public FieldNumber getAdditionInverse() {
        for (int i = 1; i < this.mod; i++) {
            if ((this.value + i) % this.mod == 0) {
                return new FieldNumber(i, mod);
            }
        }
        return new FieldNumber(0);
    }

    public FieldNumber getMultiplicativeInverse() {
        for (int i = 1; i < this.mod; i++) {
            if ((this.value * i) % this.mod == 1) {
                return new FieldNumber(i, mod);
            }
        }
        return null;
    }

    public boolean isZero() {
        return value % mod == 0;
    }

    public boolean isOne() {
        return value % mod == 1;
    }

    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int compareTo(FieldNumber o) {
        return o.value - this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldNumber that = (FieldNumber) o;
        return value == that.value && mod == that.mod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, mod);
    }

    public FieldNumber multiply(int i) {
        return new FieldNumber(this.value * i, this.mod);
    }

}

package tech.ypsilon.matrix;

public class FieldNumber implements Comparable<FieldNumber>{
    
    private int value;
    private final int mod; 

    public FieldNumber(int value){
        this(value, Main.MOD);
    }

    public FieldNumber(int value, int mod){
        this.value = value % mod;
        this.mod = mod;
    }
    
    public FieldNumber add(FieldNumber nbr){
        if (this.mod == nbr.mod) {
            return new FieldNumber(this.value + nbr.value, mod);
        } else {
            throw new IllegalArgumentException("Modulo not the same");
        }
    }

    public FieldNumber multiply(FieldNumber nbr){
        if(this.mod == nbr.mod){
            return new FieldNumber(this.value * nbr.value, mod);
        }else{
            throw new IllegalArgumentException("Modulo not the same");
        }
    }

    public FieldNumber getAdditionInverse(){
        for(int i = 1; i < this.mod; i++){
            if((this.value + i) % this.mod == 0){
                return new FieldNumber(i, mod);
            }
        }
        return new FieldNumber(0);
    }

    public FieldNumber getMultiplicalInverse(){
        for(int i = 1; i < this.mod; i++){
            if((this.value * i) % this.mod == 1){
                return new FieldNumber(i, mod);
            }
        }
        return null;
    }

    public boolean isZero() {
        return value % mod == 0;
    }

    public String toString(){
        return Integer.toString(value);
    }

    @Override
    public int compareTo(FieldNumber o) {
        return o.value - this.value;
    }
}

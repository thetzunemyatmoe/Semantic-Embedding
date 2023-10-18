import java.util.Arrays;

public class Vector {
    private double[] doubElements;

    public Vector(double[] _elements) {
        doubElements = _elements;
    }

    public double getElementatIndex(int _index) {
        try {
            return doubElements[_index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }

    }

    public void setElementatIndex(double _value, int _index) {
        try {
            doubElements[_index] = _value;
        } catch (ArrayIndexOutOfBoundsException e) {
            doubElements[doubElements.length - 1] = _value;
        }
    }

    public double[] getAllElements() {
        return doubElements;
    }

    public int getVectorSize() {
        return doubElements.length;
    }

    public Vector reSize(int _size) {
        double[] copy_array;
        if (_size == getVectorSize() || _size <= 0) {
            copy_array = new double[getVectorSize()];
            System.arraycopy(doubElements,0,copy_array,0,getVectorSize());
            return new Vector(copy_array);
        } else {
            /*
            to be checked
             */
            if (_size < getVectorSize()) {
                double[] new_array = new double[_size];
                System.arraycopy(doubElements,0,new_array,0,_size);
                return new Vector(new_array);
            } else {
                double[] new_array = new double[_size];
                System.arraycopy(doubElements, 0, new_array, 0, getVectorSize());
                for(int i = getVectorSize(); i < new_array.length; i++) {
                    new_array[i] = -1.0;
                }
                return new Vector(new_array);
            }
        }
    }

    public Vector add(Vector _v) {
        if (_v.getVectorSize() > getVectorSize()) {
            reSize(_v.getVectorSize());
        } else {
            _v = _v.reSize(getVectorSize());

        }
        double[] result = new double[_v.getVectorSize()];

        System.out.println(result.length);
        for (int i = 0; i < result.length; i++) {
            System.out.println(_v.getElementatIndex(i));
            System.out.println(getElementatIndex(i));
            result[i] = _v.getElementatIndex(i) + getElementatIndex(i);
        }

        return new Vector(result);
    }

    public Vector subtraction(Vector _v) {
        if (_v.getVectorSize() > getVectorSize()) {
            reSize(_v.getVectorSize());
        } else {
            _v = _v.reSize(getVectorSize());

        }
        double[] result = new double[_v.getVectorSize()];

        System.out.println(result.length);
        for (int i = 0; i < result.length; i++) {
            result[i] = getElementatIndex(i) - _v.getElementatIndex(i);
        }

        return new Vector(result);

    }

    public double dotProduct(Vector _v) {
        double dotproduct = 0;
        if (_v.getVectorSize() > getVectorSize()) {
            reSize(_v.getVectorSize());
        } else {
            _v = _v.reSize(getVectorSize());

        }
        for (int i = 0; i < _v.getVectorSize(); i++) {
            dotproduct += getElementatIndex(i) * _v.getElementatIndex(i);
        }
        return dotproduct;
    }

    public double cosineSimilarity(Vector _v) {
        double result = 0;
        double vec1_result = 0;
        double vec2_result = 0;
        if (_v.getVectorSize() > getVectorSize()) {
            reSize(_v.getVectorSize());
        } else {
            _v = _v.reSize(getVectorSize());

        }
        for (int i = 0; i < _v.getVectorSize(); i++) {
            vec1_result += getElementatIndex(i) * getElementatIndex(i);
            vec2_result += _v.getElementatIndex(i) * _v.getElementatIndex(i);
        }
        result = dotProduct(_v) / (Math.sqrt(vec1_result) * Math.sqrt(vec2_result));
        return result;
    }

    @Override
    public boolean equals(Object _obj) {
        Vector v = (Vector) _obj;
        boolean boolEquals = true;
        if(getVectorSize() == v.getVectorSize()){
            for (int i = 0; i < getVectorSize(); i++) {
                if(getElementatIndex(i)!= v.getElementatIndex(i)){
                    boolEquals = false;
                    break;
                }
            }
        }else{
            boolEquals = false;
        }
     return boolEquals;
    }

    @Override
    public String toString() {
        StringBuilder mySB = new StringBuilder();
        for (int i = 0; i < this.getVectorSize(); i++) {
            mySB.append(String.format("%.5f", doubElements[i])).append(",");
        }
        mySB.delete(mySB.length() - 1, mySB.length());
        return mySB.toString();
    }
}

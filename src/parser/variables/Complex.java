package parser.variables;

import lexer.TokenType;

public class Complex
{
    private double real;
    private double imag;

    public Complex(double real, double imag)
    {
        this.real = real;
        this.imag = imag;
    }

    public Complex(Complex other)
    {
        this.real = other.real;
        this.imag = other.imag;
    }

    public double getReal()
    {
        return real;
    }

    public void setReal(double real)
    {
        this.real = real;
    }

    public double getImag()
    {
        return imag;
    }

    public void setImag(double imag)
    {
        this.imag = imag;
    }

    public Complex add(Complex other)
    {
        return new Complex(this.real + other.real,
                this.imag + other.imag);
    }

    public Complex add(double other)
    {
        return new Complex(this.real + other, this.imag);
    }

    public Complex subtract(Complex other)
    {
        return new Complex(this.real - other.real,
                this.imag - other.imag);
    }

    public Complex subtractComplexFromInteger(double other)
    {
        return new Complex(other - this.real, -this.imag);
    }

    public Complex subtract(double other)
    {
        return new Complex(this.real - other, this.imag);
    }

    public Complex multiply(Complex other)
    {
        return new Complex(this.real * other.real - this.imag * other.imag,
                this.imag * other.real + this.real * other.imag);
    }

    public Complex multiply(double other)
    {
        return new Complex(this.real * other,this.imag * other);
    }

    public Complex divide(Complex other)
    {
        return new Complex((this.real * other.real + this.imag * other.imag)/(other.real*other.real + other.imag * other.imag),
                (this.imag * other.real - this.real * other.imag)/(other.real*other.real + other.imag * other.imag));
    }

    public Complex divideIntegerByComplex(double other)
    {
        return new Complex((this.real * other)/other*other,
            (this.imag * other)/other*other);
    }

    public Complex divide(double other)
    {
        return new Complex(this.real / other,this.imag / other);
    }

    public Complex conjugate()
    {
        Complex c = new Complex(this);
        c.imag = -c.imag;
        return c;
    }

    public Complex inverse()
    {
        this.real = -this.real;
        this.imag = -this.imag;
        return this;
    }

    public Complex power(double power)
    {
        double theta = StrictMath.atan2(this.imag, this.real);
        double modulusPow = StrictMath.pow(this.modulus(), power);
        double cos = StrictMath.cos(power * theta);
        double sin = StrictMath.sin(power * theta);
        return new Complex(modulusPow * cos, modulusPow * sin);
    }

    public double modulus()
    {
        return Math.sqrt(this.real*this.real + this.imag * this.imag);
    }

    public double getField(TokenType field) {
        switch (field) {
            case COMPLEX_REAL_PART:
                return getReal();
            case COMPLEX_IMAGINARY_PART:
                return getImag();
        }
        return -1;
    }

    public void setField(TokenType field, double value)
    {
        switch (field) {
            case COMPLEX_REAL_PART:
                setReal(value);
                break;
            case COMPLEX_IMAGINARY_PART:
                setImag(value);
                break;
        }
    }

    public boolean equals(Complex other)
    {
        return (this.real == other.real) && (this.imag == other.imag);

    }

    @Override
    public String toString()
    {
        if(this.imag == 0)
            return Double.toString(this.real);
        if(this.real == 0)
            return this.imag + "* i ";
        if(this.imag > 0)
            return this.real + " + " + this.imag + " * i ";
        return this.real + " - " + (-this.imag) + " * i ";
    }
}

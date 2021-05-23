package parser.variables;

import lexer.TokenType;
import parser.Value;

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

    public Complex add(Integer other)
    {
        return new Complex(this.real + other, this.imag);
    }

    public Complex subtract(Complex other)
    {
        return new Complex(this.real - other.real,
                this.imag - other.imag);
    }

    public Complex subtract(Integer other)
    {
        return new Complex(this.real - other, this.imag);
    }

    public Complex multiply(Complex other)
    {
        return new Complex(this.real * other.real - this.imag * other.imag,
                this.imag * other.real + this.real * other.imag);
    }

    public Complex multiply(Integer other)
    {
        return new Complex(this.real * other,this.imag * other);
    }

    public Complex divide(Complex other)
    {
        return new Complex((this.real * other.real + this.imag * other.imag)/other.real*other.real + other.imag * other.imag,
                this.imag * other.real + this.real * other.imag);
    }

    public Complex divide(Integer other)
    {
        return new Complex(this.real / other,this.imag / other);
    }

    public Complex conjugate(Complex complex)
    {
        Complex c = new Complex(complex);
        c.imag = -c.imag;
        return c;
    }

    public double modulus(Complex complex)
    {
        return Math.sqrt(complex.real*complex.real + complex.imag * complex.imag);
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

    public void setField(TokenType field, int value)
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

    public int compare(Complex other)
    {
        if((this.real == other.real) && (this.imag == other.imag))
            return 0;
        if(this.real < other.real || (this.real == other.real && this.imag < other.imag))
            return 1;
        else
            return -1;
    }
}

package br.com.welissontiago.service;

import br.com.welissontiago.exceptions.UnsupportMathOperationException;
import org.springframework.stereotype.Service;

@Service
public class MathService {

    public MathService() {}

    public Double mathSum(String numberOne, String numberTwo) throws Exception{
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportMathOperationException("Please set a numeric value");
        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    public Double mathSub(String numberOne, String numberTwo) throws Exception{
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportMathOperationException("Please set a numeric value");
        return convertToDouble(numberOne) - convertToDouble(numberTwo);
    }

    public Double mathMult(String numberOne, String numberTwo) throws Exception{
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportMathOperationException("Please set a numeric value");
        return convertToDouble(numberOne) * convertToDouble(numberTwo);
    }

    public Double mathDiv(String numberOne, String numberTwo) throws Exception{
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportMathOperationException("Please set a numeric value");
        return convertToDouble(numberOne) / convertToDouble(numberTwo);
    }

    public Double mathMean(String numberOne, String numberTwo) throws Exception{
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportMathOperationException("Please set a numeric value");
        return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
    }

    public Double squareRoot(String number) throws Exception{
        if(!isNumeric(number) ) throw new UnsupportMathOperationException("Please set a numeric value");
        return Math.sqrt(convertToDouble(number));
    }

    private Double convertToDouble(String strNumber) throws IllegalArgumentException{
        if(strNumber == null || strNumber.isEmpty()) throw new UnsupportMathOperationException("Please set a numeric value");
        String number = strNumber.replace(",",".");
        return Double.parseDouble(number);
    }

    private boolean isNumeric(String strNumber) {
        if(strNumber == null || strNumber.isEmpty()) return false;
        String number = strNumber.replace(",",".");
        return (number.matches("[+-]?[0-9]*\\.?[0-9]+"));
    }

}

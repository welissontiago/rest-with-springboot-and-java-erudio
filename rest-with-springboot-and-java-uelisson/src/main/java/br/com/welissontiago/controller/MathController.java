package br.com.welissontiago.controller;

import br.com.welissontiago.exceptions.UnsupportMathOperationException;
import br.com.welissontiago.service.MathService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {

    MathService mathService = new MathService();

    @RequestMapping("sum/{numberOne}/{numberTwo}")
    public Double sum(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) throws Exception {
        return mathService.mathSum(numberOne,numberTwo);
    }

    @RequestMapping("subtraction/{numberOne}/{numberTwo}")
    public Double sub(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) throws Exception {
        return mathService.mathSub(numberOne,numberTwo);
    }

    @RequestMapping("multiplication/{numberOne}/{numberTwo}")
    public Double mult(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) throws Exception {
        return mathService.mathMult(numberOne,numberTwo);
    }

    @RequestMapping("division/{numberOne}/{numberTwo}")
    public Double div(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) throws Exception {
        return mathService.mathDiv(numberOne,numberTwo);
    }

    @RequestMapping("mean/{numberOne}/{numberTwo}")
    public Double mean(@PathVariable("numberOne") String numberOne, @PathVariable("numberTwo") String numberTwo) throws Exception {
        return mathService.mathMean(numberOne,numberTwo);
    }

    @RequestMapping("squareRoot/{number}")
    public Double Root(@PathVariable("number") String number) throws Exception {
        return mathService.squareRoot(number);
    }
}

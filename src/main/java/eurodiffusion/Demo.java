package eurodiffusion;

import eurodiffusion.controller.Grid;
import eurodiffusion.model.Problem;
import eurodiffusion.model.Result;
import eurodiffusion.parser.Parser;
import eurodiffusion.parser.ProblemFileParser;

import java.util.List;

public class Demo {

    public static final String INPUT_FILE_PATH = "C:\\Work\\MIPZ_lab1\\src\\main\\resources\\input.txt";

    public static void main(String[] args) {
        Parser<List<Problem>> parser = new ProblemFileParser(INPUT_FILE_PATH);
        for (var problem : parser.parse()) {
            Grid grid = new Grid(problem.getCountries());
            Result result = grid.simulateDiffusion();
            printResults(problem.getId(), result);
        }
    }

    private static void printResults(Integer problemId, Result result) {
        System.out.println(problemId);
        System.out.print(result);
    }

}

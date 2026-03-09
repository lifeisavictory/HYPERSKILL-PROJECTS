import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;


class TestClue {
    boolean cond;
    int quantity;
    boolean showOutputs;

    TestClue(boolean cond, int quantity, boolean showOutputs) {
        this.cond = cond;
        this.quantity = quantity;
        this.showOutputs = showOutputs;
    }
}

public class CoffeeMachineTest extends StageTest<TestClue> {
    @Override
    public List<TestCase<TestClue>> generate() {
        return List.of(
                new TestCase<TestClue>().setAttach(new TestClue(true, 0, true)).setInput("300\n65\n111\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 2, true)).setInput("600\n153\n100\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 2, true)).setInput("1400\n150\n100\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 2, true)).setInput("1400\n1500\n45\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(false, 2, true)).setInput("599\n250\n200\n10"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 867, true)).setInput("345640\n43423\n23234\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(false, 1548, true)).setInput("345640\n434230\n23234\n19246"),

                new TestCase<TestClue>().setAttach(new TestClue(false, 172, true)).setInput("34564\n43423\n23234\n19246"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 0, false)).setInput("399\n112\n111\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 3, false)).setInput("2400\n249\n100\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 1, false)).setInput("1400\n1500\n44\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(false, 2, false)).setInput("500\n250\n200\n10"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 171, false)).setInput("34564\n43423\n23234\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(true, 1547, false)).setInput("345640\n434230\n23234\n1"),

                new TestCase<TestClue>().setAttach(new TestClue(false, 868, false)).setInput("345640\n43423\n23234\n19246")
        );
    }

    @Override
    public CheckResult check(String reply, TestClue clue) {
        String[] lines = reply.trim().split(":");

        if (lines.length <= 1) {
            return CheckResult.wrong("Looks like you didn't print anything!");
        } else if (lines.length <= 4) {
            return CheckResult.wrong("Looks like you didn't print the final result!");
        }

        String userOutput = lines[lines.length - 1].trim();
        String userOutputLowerCase = userOutput.toLowerCase();
        boolean ans = clue.cond;
        int quantity = clue.quantity;
        if (ans) {
            if (quantity > 0) {
                boolean isCorrect = userOutputLowerCase.contains(Integer.toString(quantity)) && userOutputLowerCase.contains("yes");

                if (isCorrect) {
                    return CheckResult.correct();
                } else {
                    String correctOutput = "Yes, I can make that amount of coffee" + " (and even " + quantity + " more than that)";

                    if (clue.showOutputs) {
                        return CheckResult.wrong("Your output:\n" + userOutput + "\nCorrect output:\n" + correctOutput);
                    } else {
                        return CheckResult.wrong("");
                    }
                }
            }

            String correctOutput = "Yes, I can make that amount of coffee";

            if (userOutputLowerCase.contains(correctOutput.toLowerCase())) {
                return CheckResult.correct();
            } else {
                if (clue.showOutputs) {
                    return CheckResult.wrong("Your output:\n" + userOutput + "\nCorrect output:\n" + correctOutput);
                } else {
                    return CheckResult.wrong("");
                }
            }

        } else {
            boolean isCorrect = userOutputLowerCase.contains("no") && userOutputLowerCase.contains(Integer.toString(quantity));

            if (isCorrect) {
                return CheckResult.correct();
            } else {
                String correctOutput = "No, I can make only " + quantity + " cup(s) of coffee";

                if (clue.showOutputs) {
                    return CheckResult.wrong("Your output:\n" + userOutput + "\nCorrect output:\n" + correctOutput);
                } else {
                    return CheckResult.wrong("");
                }
            }
        }
    }
}

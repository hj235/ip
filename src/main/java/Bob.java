import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

public class Bob {
    private static final String line = "____________________________________________________________";
    private static final String lineIndent = "    ";
    private static final String indentation = "     ";
    private static TaskList taskList = new TaskList();
    private static final String[] greeting = { "Hello! I'm Bob", "What can I do for you?" };
    private static final String[] farewell = { " Bye. Hope to see you again soon!" };
    private static final String savedTasksFilename = "savedTasks.txt";

    private static void prettyPrint(String[] texts) {
        String separator = Bob.lineIndent + Bob.line;
        System.out.println(separator);
        for (String text: texts) {
            System.out.print(Bob.indentation);
            System.out.println(text);
        }
        System.out.println(separator);
    }

    private static void loadFile() {
        Scanner scanner;
        try {
            scanner = new Scanner(new File(Bob.savedTasksFilename));
            int i = 1;
            while (scanner.hasNextLine()) {
                String text = scanner.nextLine();
                String[] input = text.split(" \\| ");
                if (input[0].equals("T")) {
                    Bob.taskList.todo(input[2]);
                } else if (input[0].equals("D")) {
                    Bob.taskList.deadline(input[2], input[3]);
                } else if (input[0].equals("E")) {
                    Bob.taskList.event((input[2]), input[3], input[4]);
                }

                if (input[1].equals("1")) {
                    Bob.taskList.mark(i);
                }
                i++;
            }

            Bob.prettyPrint(new String[] {"Saved tasks have been loaded."});
        } catch (FileNotFoundException e){
            prettyPrint(new String[] {"OOPS! There was a problem loading the saved tasks."});
            return;
        }

        while (scanner.hasNextLine()) {

        }
    }

    private static void saveFile() {
        FileWriter writer;
        try {
            writer = new FileWriter(Bob.savedTasksFilename);
            writer.write(Bob.taskList.getSaveFormat());
            writer.close();
        } catch (IOException e) {
            prettyPrint(new String[]{"OOPS! There was a problem saving the file."});
            return;
        }
    }

    private static void listCommands() {
        String[] desc = { "Here are the tasks in your list:" };
        String[] tasks = Bob.taskList.describeTasks();
        String[] texts = new String[tasks.length + desc.length];

        for (int i = 0; i < desc.length; i++) { // Copy desc lines into texts array to be printed
            texts[i] = desc[i];
        }
        for (int i = 0; i < tasks.length; i++) { // Copy tasks lines into texts array to be printed
            texts[i + desc.length] = String.format("%d.", i+1) + tasks[i];
        }

        Bob.prettyPrint(texts);
    }

    private static void markTask(int idx) {
        Bob.taskList.mark(idx);
        Bob.prettyPrint(new String[] { "Nice! I've marked this task as done:", Bob.taskList.describeTask(idx) });
    }

    private static void unmarkTask(int idx) {
        Bob.taskList.unmark(idx);
        Bob.prettyPrint(new String[] { "OK, I've marked this task as not done yet:", Bob.taskList.describeTask(idx) });
    }

    private static void deleteTask(String input) {
        String[] inputs = Bob.splitInput(input, new String[] {"delete"});
        int idx;
        try {
            idx = Integer.parseInt(inputs[0]);
        } catch(NumberFormatException e) {
            throw new TaskIndexException(inputs[0]);
        }
        if (idx <= 0 || idx > Bob.taskList.getSize()) {
            throw new TaskIndexException(inputs[0]);
        }
        Task deleted = Bob.taskList.delete(idx);
        Bob.prettyPrint(new String[] {"Noted. I've removed this task:",
                deleted.toString(),
                String.format("Now you have %d tasks in the list.", Bob.taskList.getSize())});
    }

    private static String[] splitInput(String input, String[] splits) {
        String[] result = new String[splits.length];
        int[] splitIdxs = new int[splits.length + 1];
        splitIdxs[splits.length] = input.length() + 1;

        for (int i = 0; i < splits.length; i++) {
            int splitIdx = input.indexOf(splits[i]);
            if (splitIdx < 0 || input.length() < splitIdx + splits[i].length() + 2) {
                throw new MissingParamException(splits[i]);
            }
            splitIdxs[i] = splitIdx;
        }

        for (int i = 0; i < splits.length; i++) {
            String split = splits[i];
            int splitIdx = input.indexOf(split);
            String text = input.substring(splitIdx + split.length() + 1, splitIdxs[i+1] - 1);
            result[i] = text;
        }

        return result;
    }

    private static void todo(String input) {
        String[] inputs = Bob.splitInput(input, new String[] { "todo" });
        ToDo todo = Bob.taskList.todo(inputs[0]);
        Bob.prettyPrint(new String[] { "Got it. I've added this task:",
                " " + todo.toString(),
                String.format("Now you have %d tasks in the list.", Bob.taskList.getSize()) });
    }

    private static void deadline(String input) {
        String[] inputs = Bob.splitInput(input, new String[] { "deadline", "/by" });
        Deadline deadline = Bob.taskList.deadline(inputs[0], inputs[1]);
        Bob.prettyPrint(new String[] { "Got it. I've added this task:",
                " " + deadline.toString(),
                String.format("Now you have %d tasks in the list.", Bob.taskList.getSize()) });
    }

    private static void event(String input) {
        String[] inputs = Bob.splitInput(input, new String[] { "event", "/from", "/to" });
        Event event = Bob.taskList.event(inputs[0], inputs[1], inputs[2]);
        Bob.prettyPrint(new String[] { "Got it. I've added this task:",
                " " + event.toString(),
                String.format("Now you have %d tasks in the list.", Bob.taskList.getSize()) });
    }

    public static void main(String[] args) {
        Bob.prettyPrint(Bob.greeting);
        Bob.loadFile();
        String input = "";
        String[] arguments;
        Scanner scanner = new Scanner(System.in);

        outerLoop:
        while(true) {
            input = scanner.nextLine();
            arguments = input.split(" ");
            try {
                switch(arguments[0]) {
                    case("bye"):
                        if (arguments.length > 1) throw new ExtraParamException(input.substring(4));
                        Bob.prettyPrint(new String[]{"Saving your task list..."});
                        saveFile();
                        Bob.prettyPrint(Bob.farewell);
                        break outerLoop;
                    case("list"):
                        if (arguments.length > 1) throw new ExtraParamException(input.substring(5));
                        Bob.listCommands();
                        continue;
                    case("mark"): {
                        String[] inputs = Bob.splitInput(input, new String[] {"mark"});
                        int idx;
                        try {
                            idx = Integer.parseInt(inputs[0]);
                        } catch(NumberFormatException e) {
                            throw new TaskIndexException(inputs[0]);
                        }
                        if (idx <= 0 || idx > Bob.taskList.getSize()) {
                            throw new TaskIndexException(inputs[0]);
                        }
                        Bob.markTask(idx);
                        continue;
                    }
                    case("unmark"): {
                        String[] inputs = Bob.splitInput(input, new String[] {"unmark"});
                        int idx;
                        try {
                            idx = Integer.parseInt(inputs[0]);
                        } catch(NumberFormatException e) {
                            throw new TaskIndexException(inputs[0]);
                        }
                        if (idx <= 0 || idx > Bob.taskList.getSize()) {
                            throw new TaskIndexException(inputs[0]);
                        }
                        Bob.unmarkTask(idx);
                        continue;
                    }
                    case("todo"):
                        Bob.todo(input);
                        continue;
                    case("deadline"): {
                        Bob.deadline(input);
                        continue;
                    }
                    case("event"): {
                        Bob.event(input);
                        continue;
                    }
                    case("delete"):
                        Bob.deleteTask(input);
                        continue;
                    default:
                        throw new UnknownCommandException(arguments[0]);
                }
            } catch(MissingParamException | ExtraParamException | UnknownCommandException | TaskIndexException e) {
                Bob.prettyPrint(e.getLines());
            }
        }
    }
}

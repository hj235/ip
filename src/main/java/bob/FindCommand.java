package bob;

public class FindCommand extends Command {
    protected static String[] params = new String[] {
            "find"
    };
    protected static int paramCount = 1;
    protected static String identifier = "find";
    private final String date;

    public FindCommand(String date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        String[] tasksFound = tasks.findTasksOn(this.date);
        String[] toPrint = new String[tasksFound.length + 2];
        toPrint[0] = String.format("Here are the tasks occuring on %s:", this.date);
        toPrint[tasksFound.length + 1] = String.format("Number of tasks found: %d", tasksFound.length);
        System.arraycopy(tasksFound, 0, toPrint, 1, tasksFound.length);

        Printer.prettyPrint(toPrint);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FindCommand temp) {
            return this.date.equals(temp.date);
        }
        return false;
    }
}
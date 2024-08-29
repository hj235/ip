import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasklist;

    public TaskList() {
        this.tasklist = new ArrayList<>();
    }

    public ToDo todo(String name) {
        ToDo todo = new ToDo(name);
        this.tasklist.add(todo);
        return todo;
    }

    public Deadline deadline(String name, String by) {
        Deadline deadline = new Deadline(name, by);
        this.tasklist.add(deadline);
        return deadline;
    }

    public Event event(String name, String from, String to) {
        Event event = new Event(name, from, to);
        this.tasklist.add(event);
        return event;
    }

    public void mark(int idx) {
        if (idx < 1 || idx > this.tasklist.size()) {
            return;
        }

        this.tasklist.get(idx-1).complete();
    }

    public void unmark(int idx) {
        if (idx < 1 || idx > this.tasklist.size()) {
            return;
        }

        this.tasklist.get(idx-1).uncomplete();
    }

    public Task delete(int idx) {
        if (idx < 1 || idx > this.tasklist.size()) {
            return null;
        }

        Task deleted = this.tasklist.get(idx-1);
        this.tasklist.remove(idx-1);
        return deleted;
    }

    public int getSize() {
        return this.tasklist.size();
    }

    public String describeTask(int idx) {
        if (idx < 1 || idx > this.tasklist.size()) {
            return "Error finding task!";
        }

        return this.tasklist.get(idx-1).toString();
    }

    public String[] describeTasks() {
        ArrayList<String> results = new ArrayList<>();
        this.tasklist.forEach((task) -> results.add(task.toString()));
        return results.toArray(new String[0]);
    }

    public String getSaveFormat() {
        String res = "";
        for (int i = 0; i < this.tasklist.size(); i++) {
            res += this.tasklist.get(i).getSaveFormat() + "\n";
        }
        return res;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < this.tasklist.size(); i++) {
            str += String.format("%d. ", i + 1) + this.tasklist.get(i) + "\n";
        }
        return str;
    }
}

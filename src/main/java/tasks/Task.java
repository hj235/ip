package tasks;

public abstract class Task {
    private String name;
    private boolean completed = false;

    public Task(String name) {
        this.name = name;
    }

    public void complete() {
        this.completed = true;
    }

    public void uncomplete() {
        this.completed = false;
    }

    public String getSaveFormat() {
        String s = this.completed ? "1 | " : "0 | ";
        return s + this.name;
    }

    public boolean isDuring(String date) {
        return false;
    }

    public boolean hasInName(String text) {
        return this.name.contains(text);
    }

    @Override
    public String toString() {
        String completedBox = this.completed ? "[X] " : "[ ] ";
        return completedBox + this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task temp) {
            return this.completed == temp.completed && this.name == temp.name;
        }
        return false;
    }
}

public class Event extends Task {
    private String from;
    private String to;

    public Event(String name, String from, String to) {
        super(name);

        this.from = from;
        this.to = to;
    }

    @Override
    public String getSaveFormat() {
        return "E | " + super.getSaveFormat() + " | " + this.from + " | " + this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + String.format(" (from: %s to: %s)", this.from, this.to);
    }
}

public enum Chronotype {
    OWL("Сова"),
    LARK("Жаворонок"),
    DOVE("Голубь");

    private final String title;

    Chronotype(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

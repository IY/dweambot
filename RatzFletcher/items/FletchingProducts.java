package items;

public enum FletchingProducts {
    LOGS("Logs"), OAK_LOGS("Oak logs"), WILLOW_LOGS("Willow logs"), MAPLE_LOGS("Maple logs"), YEW_LOGS("Yew logs"), MAGIC_LOGS("Magic logs");
    private final String name;

    FletchingProducts(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}

package items;

public enum FletchingResources {
    SHAFT("Arrow shaft", 1, 14), JAVELIN("Javelin shafts", 3, 15), SHORTBOW("Shortbow", 5, 16), LONGBOW("Longbow", 10, 17), STOCK("Crossbow stock", 9, 18),
    OAK_SHAFT("Arrow shaft", 15, 14), OAK_SHORTBOW("Oak shortbow", 20, 15), OAK_LONGBOW("Oak longbow", 25, 16), OAK_STOCK("Crossbow stock", 24, 17), OAK_SHIELD("Oak shield", 27, 18),
    WILLOW_SHAFT("Arrow shaft", 30, 14), WILLOW_SHORTBOW("Willow shortbow", 35, 15), WILLOW_LONGBOW("Willow longbow", 40, 16), WILLOW_STOCK("Crossbow stock", 39, 17), WILLOW_SHIELD("Willow shield", 42, 18),
    MAPLE_SHAFT("Arrow shaft", 45, 14), MAPLE_SHORTBOW("Maple shortbow", 50, 15), MAPLE_LONGBOW("Maple longbow", 55, 16), MAPLE_STOCK("Crossbow stock", 54, 17), MAPLE_SHIELD("Maple shield", 57, 18),
    YEW_SHAFT("Arrow shaft", 60, 14), YEW_SHORTBOW("Yew shortbow", 65, 15), YEW_LONGBOW("Yew longbow", 70, 16), YEW_STOCK("Crossbow stock", 69, 17), YEW_SHIELD("Yew shield", 72, 18);

    private final String name;
    private final int level;

    FletchingResources(final String name, final int level, final int widgetSelection) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}

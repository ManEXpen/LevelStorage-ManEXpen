package manexpen.levelstorage.api;

public enum EnumKey {

    CHANGE_REPEL_MODE(0), RAY_SHOOT(1), TELEPORT(2);

    public final int keyType;

    private EnumKey(final int keyType) {
        this.keyType = keyType;
    }

    public static EnumKey getKeyByID(int keyType) {
        return values()[keyType];
    }


    @Override
    public String toString() {
        return this.name();
    }
}

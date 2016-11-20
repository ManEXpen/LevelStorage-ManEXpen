package manexpen.levelstorage.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLocation {
    private int dimId;
    private int x;
    private int y;
    private int z;

    /**
     * Initializes a new instance of BlockLocation
     *
     * @param dimId Dimension ID
     * @param x     X Coordinate
     * @param y     Y Coordinate
     * @param z     Z Coordinate
     */
    public BlockLocation(int dimId, int x, int y, int z) {
        this.dimId = dimId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Less picky version of the above constructor <br />
     * Initializes a new instance of BlockLocation <br />
     * Sets Dimension ID to Integer.MIN_VALUE.
     *
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     */
    public BlockLocation(int x, int y, int z) {
        this.dimId = Integer.MIN_VALUE;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object other) {
        boolean eq = true;
        eq = eq && other instanceof BlockLocation;
        if (eq) {
            eq = eq && ((BlockLocation) other).dimId == this.dimId;
            eq = eq && ((BlockLocation) other).x == this.x;
            eq = eq && ((BlockLocation) other).y == this.y;
            eq = eq && ((BlockLocation) other).z == this.z;
        }
        return eq;
    }

    /**
     * Initializes empty instance of BlockLocation (all values are 0s)
     */
    public BlockLocation() {
        this.dimId = 0;
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }


    /**
     * Self-descriptive
     *
     * @param other BlockLocation for comparison
     * @return Amount of space between two points, or Integer.MAX_VALUE if
     * another dimension
     */
    public int getDistance(BlockLocation other) {
        if (this.dimId != other.dimId)
            return Integer.MAX_VALUE;
        int xDistance = Math.abs(this.x - other.x);
        int yDistance = Math.abs(this.y - other.y);
        int zDistance = Math.abs(this.z - other.z);

        return xDistance + yDistance + zDistance;
    }

    public BlockLocation copy() {
        return new BlockLocation(this.dimId, this.x, this.y, this.z);
    }

    public BlockLocation move(ForgeDirection dir, int space) {
        BlockLocation ret = this.copy();
        ret.x += dir.offsetX * space;
        ret.y += dir.offsetY * space;
        ret.z += dir.offsetZ * space;
        return ret;
    }

    public static final String BLOCK_LOCATION_NBT = "blockLocation";
    private static final String DIM_ID_NBT = "dimId";
    private static final String X_NBT = "xCoord";
    private static final String Y_NBT = "yCoord";
    private static final String Z_NBT = "zCoord";


    @Override
    public String toString() {
        return "(" + dimId + ":" + x + "," + y + "," + z + ")";
    }


    /**
     * Returns whether or not DimensionId is valid
     *
     * @param dimId Dimension id
     */
    public static boolean isDimIdValid(int dimId) {
        Integer[] ids = DimensionManager.getIDs();
        for (int id : ids) {
            if (id == dimId)
                return true;
        }
        return false;
    }

    /**
     * Gets TileEntity
     *
     * @return TileEntity of block on given coordinates
     */
    public TileEntity getTileEntity() {
        if (!isDimIdValid(this.dimId))
            return null;
        return DimensionManager.getWorld(this.dimId).getTileEntity(this.x,
                this.y, this.z);
    }

    // Getters & setters ahead
    public int getDimId() {
        return this.dimId;
    }

    public void setDimId(int dimId) {
        this.dimId = dimId;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    // End of getters and setters
}

package hu.oe.nik.szfmv.model.Classes;

import hu.oe.nik.szfmv.model.Interfaces.ICollidable;

public class Cyclist extends Dynamic implements ICollidable {

    /**
     *
     * @param x coordinate
     * @param y coordinate
     * @param imageFileName the name of the imagefile used for this type
     */
    public Cyclist(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     *
     * @param x coordinate
     * @param y coordinate
     * @param imageFileName the name of the imagefile used for this type
     * @param m11 transformation
     * @param m12 transformation
     * @param m21 transformation
     * @param m22 transformation
     */
    public Cyclist(int x, int y, String imageFileName, double m11, double m12, double m21, double m22) {
        super(x, y, imageFileName, m11, m12, m21, m22);

        PhysicsModel physicsModel = new PhysicsModel();
        physicsModel.setDamage(0);
        physicsModel.setDamagelimit(100);
        physicsModel.setIsdead(false);
        physicsModel.setFatal(false);
        physicsModel.setWeight(100);
        physicsModel.setWidth(51);
        physicsModel.setWeight(126);
    }
}

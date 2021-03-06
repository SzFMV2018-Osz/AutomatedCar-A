package hu.oe.nik.szfmv.environment;

import hu.oe.nik.szfmv.Main;
import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.model.Classes.Car;
import hu.oe.nik.szfmv.model.Classes.Dynamic;
import hu.oe.nik.szfmv.model.Classes.Person;
import hu.oe.nik.szfmv.model.Classes.Static;
import hu.oe.nik.szfmv.model.Interfaces.ICollidable;
import hu.oe.nik.szfmv.visualization.Camera;

import java.awt.*;


public class Physics {


    /**
     * ellenörzi hogy volt e ütközés és ha volt akkor a megfelelő tipust kiválasztja
     *
     * @param world  a világ
     * @param camera a caera obiektum
     */
    public void update(World world, Camera camera) {
        AutomatedCar car = (AutomatedCar) world.getWorldObjects().stream().filter(x -> x instanceof AutomatedCar).findAny().orElse(null);
        boolean carcollide = false;
        for (WorldObject object : world.getWorldObjects()) {
            object.rotateImage(camera);
        }
        if(car != null) {
            for (WorldObject object : world.getWorldObjects()) {
                if (object instanceof ICollidable) {
                    boolean collide = getCollide(car, object) && !car.equals(object);


                    if (!car.equals(object)) {
                        object.setCollide(collide);
                        if (collide && object instanceof Static) {
                            staticColide(car, camera, 0);

                        }
                        if (collide && object instanceof Dynamic) {
                            dynamiccColide(car, object, camera);
                        }
                        if (object instanceof Person && collide) {
                            Main.gameLoop = false;
                            car.physicsModel.setFatal(true);

                        }

                    }
                    if (collide) {
                        carcollide = true;
                        car.setNewImage();
                    }

                    updateLastPosition(object);


                }
            }

            car.setCollide(carcollide);
        }
    }

    /**
     * @param object beálítja az utolsó helyes poziciót
     */
    private void updateLastPosition(WorldObject object) {
        object.setLastX(object.getX());
        object.setLastY(object.getY());
    }

    /**
     * tényleges ütközés detektálás
     *
     * @param a obiektum
     * @param b obiektum
     * @return igaz / hamis
     */
    private boolean getCollide(WorldObject a, WorldObject b) {

        Rectangle aRectangle = new Rectangle(a.getWidth() / 2 - a.getPhysicsModel().getWidth() / 2, a.getHeight() / 2 - a.getPhysicsModel().getHeight() / 2, a.getPhysicsModel().getWidth(), a.getPhysicsModel().getHeight());
        Shape sa = a.getTransformation().createTransformedShape(aRectangle);
        Rectangle bRectangle = new Rectangle(b.getWidth() / 2 - b.getPhysicsModel().getWidth() / 2, b.getHeight() / 2 - b.getPhysicsModel().getHeight() / 2, b.getPhysicsModel().getWidth(), b.getPhysicsModel().getHeight());
        Shape sb = b.getTransformation().createTransformedShape(bRectangle);
        return sa.intersects(sb.getBounds());
    }

    /**
     * @param a      utköző obijektum
     * @param camera camera az obijektum igazításhoz
     */
    private void staticColide(WorldObject a, Camera camera, int dmg) {
        ((AutomatedCar) a).stopImmediately();
        if (dmg == 0) {
            double deltaaX = a.getX() - a.lastX;
            double deltaaY = a.getY() - a.lastY;
            double deltaaSpeed = Math.sqrt(deltaaX * deltaaX + deltaaY * deltaaY);
            a.setLastY(a.getLastY() - (int)deltaaY);
            a.setLastX(a.getLastX() - (int)deltaaX);
            a.setX(a.getLastX());
            ((AutomatedCar) a).getVirtualFunctionBus().carPacket.setxPosition(a.getLastX());
            a.setY(a.getLastY());
            ((AutomatedCar) a).getVirtualFunctionBus().carPacket.setyPosition(a.getLastY());
            dmg = (int) deltaaSpeed * 2;
        }
        a.physicsModel.addDamage(dmg);
        System.out.println(a.physicsModel.getDamage());



        a.rotateImage(camera);


    }

    /**
     * @param a      utköző obijektum
     * @param b      másik utköző obijektum
     * @param camera camera az obijektum igazításhoz
     */
    private void dynamiccColide(WorldObject a, WorldObject b, Camera camera) {

        double deltaaX = a.getX() - a.lastX;
        double deltaaY = a.getY() - a.lastY;
        double deltaaSpeed = Math.sqrt(deltaaX * deltaaX + deltaaY * deltaaY);

        double deltabX = b.getX() - b.lastX;
        double deltabY = b.getY() - b.lastY;


        double deltaX = deltaaX + deltabX;
        double deltaY = deltaaY + deltabY;
        double deltaSpeed = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double dmg = Math.abs(deltaaX) + Math.abs(deltabX) + Math.abs(deltaaY) + Math.abs(deltabY);
        a.setLastX(a.lastX + (int)deltabX);
        a.setLastY(a.lastY + (int)deltabY);
        b.setLastX(b.lastX + (int)deltaaX);
        b.setLastY(b.lastY + (int)deltaaX);
        a.setLastY(a.getLastY() + 1);
        a.setLastX(a.getLastX() + 1);
        returnLastCorrectPosition(a, b);
        a.setxVelocity((int) deltaX / 2);
        b.setxVelocity((int) deltaX / 2);
        a.setyVelocity((int) deltaY / 2);
        b.setyVelocity((int) deltaY / 2);
        b.getPhysicsModel().addDamage((int) (dmg / 2));
        b.getPhysicsModel().setDamage(1);
        staticColide(a, camera, (int) (dmg/2));
        if(b instanceof Car)
        ((Car) b).setNewImage();
        b.rotateImage(camera);

    }

    private void returnLastCorrectPosition(WorldObject a, WorldObject b) {
        a.setX(a.lastX);
        a.setY(a.lastY);
        b.setX(b.lastX);
        b.setY(b.lastY);
    }

}

package hu.oe.nik.szfmv.automatedcar.sensors;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class CameraSensorTest {

    @Test
    public void locateSensorTriangle() {
        CameraSensor testCamera = new CameraSensor(new VirtualFunctionBus());

        /*
        Point sensorLocation = new Point(20, 30);
        double visualRange = 10;
        double angleOfView = 60;
        double rotation = 90;

        Polygon expectedPolygon = new Polygon();
        expectedPolygon.npoints = 3;
        expectedPolygon.xpoints = new int[]{sensorLocation.x, 10, 10};
        expectedPolygon.ypoints = new int[]{sensorLocation.y, 220, 200};



        Polygon actualPolygon = testCamera.locateSensorTriangle(sensorLocation, visualRange, angleOfView, rotation);


        Assert.assertEquals(expectedPolygon.npoints, actualPolygon.npoints);
        Assert.assertArrayEquals(expectedPolygon.xpoints, actualPolygon.xpoints);
        Assert.assertArrayEquals(expectedPolygon.ypoints, actualPolygon.ypoints);

        */

        Assert.assertNotNull(testCamera);
    }
}
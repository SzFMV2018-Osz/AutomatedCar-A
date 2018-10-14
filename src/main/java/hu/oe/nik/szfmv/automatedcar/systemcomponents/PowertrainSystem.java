package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.packets.powertrain.PowertrainPacket;
import hu.oe.nik.szfmv.common.DynamicMoving;
import hu.oe.nik.szfmv.common.Resistences;
import hu.oe.nik.szfmv.common.exceptions.NegativeNumberException;

import java.awt.*;

/**
 * Powertrain system is responsible for the movement of the car.
 */
public class PowertrainSystem extends SystemComponent {
    public static final int MAX_RPM = 6000;
    public static final int MIN_RPM = 750;

    private static final double GEAR_RATIOS = 1.3;
    private static final int PERCENTAGE_DIVISOR = 100;
    private static final int SAMPLE_WEIGHT = 1000;
    private static final int ENGINE_BRAKE_TORQUE = 70;
    private static final double MAX_BRAKE_DECELERATION = 25;

    private DynamicMoving dynamicMoving;

    private double speed;
    private int currentRPM;
    private int actualRPM;

    /**
     * Creates a powertrain system that connects the Virtual Function Bus
     *
     * @param virtualFunctionBus {@link VirtualFunctionBus} used to connect {@link SystemComponent}s
     */
    public PowertrainSystem(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);

        this.virtualFunctionBus.powertrainPacket = new PowertrainPacket();
        this.dynamicMoving = new DynamicMoving();

        this.currentRPM = MIN_RPM;
        this.actualRPM = this.currentRPM;
    }

    /**
     * Calculate the magnitude of the given vector
     *
     * @param vector given vector
     * @return the magnitude
     */
    private static double calculateVectorMagnitude(Point vector) {
        return Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2));
    }

    @Override
    public void loop() {
        int gasPedal = virtualFunctionBus.readOnlyInputPositionPacket.getGaspedalPosition();
        speed = 0;
        //TODO write this
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * Calculate the actual rpm of the engine
     *
     * @param gasPedalPosition position of the gaspedal
     * @return the actual rpm
     * @throws NegativeNumberException the input value must be a non-negative number
     */
    public int calculateActualRpm(int gasPedalPosition) throws NegativeNumberException {
        if (gasPedalPosition < 0) {
            throw new NegativeNumberException("The position of the gas pedal must be a non-negative number");
        }
        if (gasPedalPosition == 0) {
            int actual = MIN_RPM;
            this.virtualFunctionBus.powertrainPacket.setRpm(actual);
            return actual;
        } else {
            double multiplier = ((double) (MAX_RPM - MIN_RPM) / PERCENTAGE_DIVISOR);
            int actual = (int) ((gasPedalPosition * multiplier) + this.currentRPM);
            this.virtualFunctionBus.powertrainPacket.setRpm(actual);
            return actual;
        }
    }

    /**
     * Gets the magnitude of the car's velocity vector
     *
     * @return the magnitude
     */
    private double getVelocityVectorMagnitude() {
        return PowertrainSystem.calculateVectorMagnitude(this.dynamicMoving.getVector());
    }

    /**
     * Gets the magnitude of the air resistance
     *
     * @return the magnitude
     */
    private double getAirResistanceMagnitude() {
        return PowertrainSystem.calculateVectorMagnitude(Resistences.calculateAirResistance(
                this.dynamicMoving.getVector()));
    }

    /**
     * Gets the magnitude of the rolling resistance
     *
     * @return the magnitude
     */
    private double getRollingResistanceMagnitude() {
        return PowertrainSystem.calculateVectorMagnitude(Resistences.calulateRollingResistance(
                this.dynamicMoving.getVector()));
    }

    /**
     * Calculate the difference between the actual and the increased speed
     *
     * @return the speed delta
     */
    private double calculateSpeedDifference() {
        double speedDelta;
        int brakePedalPosition = this.virtualFunctionBus.readOnlyInputPositionPacket.getBreakpedalPosition();

        if (this.actualRPM > this.currentRPM) {
            speedDelta = (this.getVelocityVectorMagnitude() * this.actualRPM * GEAR_RATIOS) /
                    (this.getAirResistanceMagnitude() * SAMPLE_WEIGHT *
                            this.getRollingResistanceMagnitude());
        } else if (brakePedalPosition > 0) {
            speedDelta = -1 * this.getVelocityVectorMagnitude() * ((MAX_BRAKE_DECELERATION /
                    (double) PERCENTAGE_DIVISOR) * brakePedalPosition);
        } else {
            speedDelta = (-1 * this.getVelocityVectorMagnitude() * (double) ENGINE_BRAKE_TORQUE *
                    this.getAirResistanceMagnitude() * this.getRollingResistanceMagnitude()) / PERCENTAGE_DIVISOR;
        }

        return speedDelta;
    }

    /**
     * Change the current speed by the speed delta
     *
     * @param speedDelta The difference between the old and the new speed
     */
    private void updateChanges(double speedDelta) {
        this.speed += speedDelta;
        this.currentRPM = this.actualRPM;

        this.dynamicMoving.calculateNewVector(speedDelta);
        this.virtualFunctionBus.powertrainPacket.setSpeed(this.speed);
    }
}


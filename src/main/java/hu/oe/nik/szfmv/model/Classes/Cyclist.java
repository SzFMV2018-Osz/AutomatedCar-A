package hu.oe.nik.szfmv.model.Classes;

import hu.oe.nik.szfmv.model.Interfaces.Not_Playable;

public class Cyclist extends Dinamic implements Not_Playable {
    public Cyclist(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    public Cyclist(int x, int y, String imageFileName, double m11, double m12, double m21, double m22) {
        super(x, y, imageFileName, m11, m12, m21, m22);
    }
}
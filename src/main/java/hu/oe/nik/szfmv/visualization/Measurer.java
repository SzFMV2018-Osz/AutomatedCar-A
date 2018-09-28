package hu.oe.nik.szfmv.visualization;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Measurer extends JPanel {
	Dashboard parent;
	
	public Measurer(Dashboard pt) {
		setSize(130,130);
		parent = pt;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(0x888888));
		g.fillOval(-30, -30, 200, 200);
		g.setColor(Color.BLACK);
		g.fillOval(3, 3, 125, 125);
		g.setColor(Color.WHITE);
		g.fillOval(6, 6, 119, 119);
		g.setColor(Color.RED);
		g.fillOval(65, 65, 5, 5);
		g.setColor(Color.BLACK);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 10));

		for (int i = 110; i <= 120 + 110; i++) {
			int x = 58 + (int) (48 * Math.sin(i * Math.PI / 90));
			int y = 65 - (int) (48 * Math.cos(i * Math.PI / 90));
			if ((i-1) % 10 == 0) {
				g.drawString(Integer.toString(i - 111), x, y);
			}
		}

		int tx, ty;
		tx = 58 + (int) (48 * Math.sin(parent.power * Math.PI / 90));
		ty = 65 - (int) (48 * Math.cos(parent.power * Math.PI / 90));
		g.setColor(Color.RED);
		g.drawLine(67, 67, tx, ty);
	}
}

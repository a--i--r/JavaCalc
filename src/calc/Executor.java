package calc;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

// 実行クラス
public class Executor {

	public static void main(String[] args) {

		// 起動
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				Toolkit tk = Toolkit.getDefaultToolkit();
				Image img = tk.getImage("icon-50.png");

				CalcFrame calc = new CalcFrame();
				calc.setIconImage(img);
				calc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				calc.setVisible(true);
			}
		});
	}

}

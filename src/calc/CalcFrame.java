package calc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

// 電卓GUI用クラス
public class CalcFrame extends JFrame implements ActionListener, KeyListener{

		// 定数
		public static final int	MAX_INPUT_LENGTH = 20;
		public static final int INPUT_MODE = 0;
		public static final int RESULT_MODE = 1;
		public static final int ERROR_MODE = 2;
		public static final int BUTTON_NUM = 24;

		Font f12  = new Font("SansSerif", 0, 12);

		JPanel 			panelMain 	= new JPanel();
		int				dispMode	= INPUT_MODE;
		boolean			clearOnNextDigit,  percent;
		BigDecimal		lastNumber;
		String			lastOp		= "";

		JLabel			jlblOutput	= new JLabel("0");
		JButton			jButtons[];
		String			strButtons[] = {"0","1","2","3","4","5","6","7","8","9","+/-",".","=","/","*","-","+","sqrt","1/x","%","BS","CE","C"};

		// GUI 設定
		public CalcFrame() {

			panelMain.setBackground(Color.gray);
			this.setSize(new Dimension(350,245));
			this.setTitle("Java電卓");

			// 結果表示用ラベル配置
			Border margin = new EmptyBorder(0, 10, 0, 10);
			jlblOutput.setBorder(margin);
			jlblOutput.setHorizontalAlignment(JLabel.RIGHT);
			jlblOutput.setBackground(Color.WHITE);
			jlblOutput.setOpaque(true);

			this.getContentPane().add(jlblOutput, BorderLayout.NORTH);

			// ボタン類
			jButtons = new JButton[ BUTTON_NUM - 1 ];

			int i = 0;
			for (String button: strButtons) {
				jButtons[ i ] = new JButton(strButtons[ i ]);
				i++;
			}

			// ボタンの色
			for (i=0;i < jButtons.length;i++) {
				jButtons[ i ].setFont(f12);
				if (i < 10) {
					jButtons[ i ].setForeground(Color.blue);
				}
				else {
					jButtons[ i ].setForeground(Color.red);
				}
			}

			panelMain.add(jButtons[ 20 ]);
			panelMain.add(new JLabel(""));	// 空欄
			panelMain.add(new JLabel(""));	// 空欄
			panelMain.add(jButtons[ 21 ]);
			panelMain.add(jButtons[ 22 ]);
			for (i=7;i<=9;i++) {
				panelMain.add(jButtons[ i ]);
			}
			panelMain.add(jButtons[ 13 ]);	// /
			panelMain.add(jButtons[ 17 ]);	// sqrt

			// 4-6
			for (i=4;i<=6;i++) {
				panelMain.add(jButtons[ i ]);
			}
			panelMain.add(jButtons[ 14 ]);	// *
			panelMain.add(jButtons[ 18 ]);	// 1/x

			// 1-3
			for (i=1;i<=3;i++) {
				panelMain.add(jButtons[ i ]);
			}
			panelMain.add(jButtons[ 15 ]);	// -
			panelMain.add(jButtons[ 19 ]);	// %

			panelMain.add(jButtons[ 0 ]);		// 0
			panelMain.add(jButtons[ 10 ]);	// "+/-"
			panelMain.add(jButtons[ 11 ]);	// .
			panelMain.add(jButtons[ 16 ]);	// +
			panelMain.add(jButtons[ 12 ]);	// =
			panelMain.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			panelMain.setLayout(new GridLayout(5,5,5,5));

			this.getContentPane().add(panelMain, BorderLayout.SOUTH);
			this.requestFocus();

			// アクションリスナー有効化
			for (JButton btn: jButtons) {
				btn.addActionListener(this);
				btn.setFocusable(false);
			}
			// キーリスナー追加
			this.addKeyListener(this);
			// 表示クリア
			this.clearAll();

			// window listener 追加
			addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent evt) {
					System.exit(0);
				}
			});

		}

		// 小数点以下スケールトリマー
		public static BigDecimal bigDecimalTrim(BigDecimal n) {
			try {
				while (true) {
					if (n.scale() <= 1) { return n;}
					n = n.setScale(n.scale()-1);
				}
			} catch (ArithmeticException e) {
				;
			}
			return n;
		}

		// 結果表示
		private void display(String s){
			jlblOutput.setText(s);
		}

		// 結果内容取得
		private String getDisplay() {
			return jlblOutput.getText();
		}

		// C
		private void clearAll() {
			this.display("0");
			lastOp = "0";
			lastNumber = BigDecimal.valueOf(0);
			dispMode = INPUT_MODE;
			clearOnNextDigit = true;
		}

		// CE
		private void clearExisting(){
			this.display("0");
			clearOnNextDigit = true;
			dispMode = INPUT_MODE;
		}

		// 結果取得（数値）
		private BigDecimal getNumberInDisplay()	{
			String input = this.getDisplay();
			return BigDecimal.valueOf(Double.valueOf(input));
		}

		// 入力値追加
		private void addDigitToDisplay(int d) {
			if (this.clearOnNextDigit) {
				this.display("");
			}
			String input = this.getDisplay();
			if (input.indexOf("0") == 0) {
				input = input.substring(1);
			}
			if ((!input.equals("0") || d > 0) && input.length() < MAX_INPUT_LENGTH) {
				this.display(input + d);
			}
			this.dispMode = INPUT_MODE;
			this.clearOnNextDigit = false;
		}

		// 結果表示
		void displayResult(BigDecimal result){
			this.display(result.toPlainString());
			this.lastNumber = result;
			this.dispMode = RESULT_MODE;
			this.clearOnNextDigit = true;
		}

		// エラー表示
		void displayError(String errorMessage){
			this.display(errorMessage);
			this.lastNumber = BigDecimal.valueOf(0);
			this.dispMode = ERROR_MODE;
			this.clearOnNextDigit = true;
		}

		// 符号表示
		private void processSignChange() {
			if (this.dispMode == INPUT_MODE) {
				String input = this.getDisplay();
				if (input.length() > 0 && !input.equals("0")) {
					if (input.indexOf("-") == 0) {
						this.display(input.substring(1));
					}
					else {
						this.display("-" + input);
					}
				}
			}
		}

		// 小数点追加
		private void addDicimalPoint() {
			this.dispMode = INPUT_MODE;

			if (this.clearOnNextDigit) {
				this.display("");
			}
			String input = this.getDisplay();
			if (input.indexOf(".") < 0) {
				this.display(new String(input + "."));
			}
		}

		// 四則演算
		private void processOperator(String op) {
			if (this.dispMode != ERROR_MODE) {
				BigDecimal dispNumber = this.getNumberInDisplay();
				if (!this.lastOp.equals("0")) {
					try {
						BigDecimal result = this.processLastOperator();
						this.displayResult(result);
						this.lastNumber = result;
					}
					catch (Exception e) {
						;
					}
				}
				else {
					this.lastNumber = dispNumber;
				}
				this.clearOnNextDigit = true;
				this.lastOp = op;
			}
		}
		private BigDecimal processLastOperator() throws DivideByZeroException {
			BigDecimal result = BigDecimal.valueOf(0);
			BigDecimal dispNumber = this.getNumberInDisplay();

			if (this.lastOp.equals("/")) {
				if (dispNumber.equals(new BigDecimal("0"))) {
					throw new DivideByZeroException();
				}
				result = this.lastNumber.divide(dispNumber, MAX_INPUT_LENGTH-2, RoundingMode.HALF_UP);
			}
			else if (this.lastOp.equals("*")) {
				result = this.lastNumber.multiply(dispNumber);
			}
			else if (this.lastOp.equals("-")) {
				result = this.lastNumber.subtract(dispNumber);
			}
			else if (this.lastOp.equals("+")) {
				result = this.lastNumber.add(dispNumber);
			}
			return bigDecimalTrim(result);
		}

		// =
		private void processEquals() {
			BigDecimal result = BigDecimal.valueOf(0);

			if (this.dispMode != ERROR_MODE) {
				try {
					result = this.processLastOperator();
					this.displayResult(result);
				}
				catch (Exception e) {
					this.displayError("Cannot divide by zero.");
				}
				this.lastOp = "0";
			}
		}

		// sqrt
		private void processSqrt() {
			if (this.dispMode != ERROR_MODE) {
				try {
					if (this.getDisplay().indexOf("-") == 0) {
						this.displayError("Invalid input for sqrt.");
					}
					BigDecimal result = BigDecimal.valueOf( Math.sqrt(this.getNumberInDisplay().doubleValue()) );
					this.displayResult(result);
				}
				catch (Exception e) {
					displayError("Invalid input for sqrt.");
				}
			}
		}

		// 逆数
		private void processReciprocal() {
			if (this.dispMode != ERROR_MODE) {
				try {
					if (this.getNumberInDisplay().equals("0")) {
						throw new DivideByZeroException();
					}
					BigDecimal result = BigDecimal.valueOf(1 / this.getNumberInDisplay().doubleValue());
					this.displayResult(result);
				}
				catch (Exception e) {
					displayError("Cannot divide by zero.");
				}
			}
		}

		// ％
		private void processPercent() {
			if (this.dispMode != ERROR_MODE) {
				try {
					BigDecimal result = BigDecimal.valueOf( this.getNumberInDisplay().doubleValue() / 100 );
					this.displayResult(result);
				}
				catch (Exception e) {
					displayError("Invalid input for %.");
				}
			}
		}

		// backspace
		private void processBackspace() {
			if (this.dispMode != ERROR_MODE) {
				try {
					String nextDisplay = this.getDisplay().substring(0, this.getDisplay().length() - 1);
					this.display(nextDisplay);

					if (this.getDisplay().length() < 1) {
						this.display("0");
					}
				}
				catch (Exception e) {
					displayError("Invalid input for Backspace.");
				}
			}
		}

		// コマンド実行
		private void actionCommand(int i) {
			switch (i) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				this.addDigitToDisplay(i);
				break;
			case 10:	// +/-
				this.processSignChange();
				break;
			case 11:	// .
				this.addDicimalPoint();
				break;
			case 12:	// =
				this.processEquals();
				break;
			case 13:
				this.processOperator("/");
				break;
			case 14:
				this.processOperator("*");
				break;
			case 15:
				this.processOperator("-");
				break;
			case 16:
				this.processOperator("+");
				break;
			case 17:	// sqrt
				this.processSqrt();
				break;
			case 18:	// 1/x
				this.processReciprocal();
				break;
			case 19:	// %
				this.processPercent();
				break;
			case 20:	// backspace
				this.processBackspace();
				break;
			case 21:	// CE
				this.clearExisting();
				break;
			case 22:	// C
				this.clearAll();
				break;
			}
		}

		// ボタンアクション
		@Override
		public void actionPerformed(ActionEvent e) {

			// 押されたかチェック
			for (int i=0;i < jButtons.length;i++) {
				if (e.getSource() == jButtons[ i ]) {
					this.actionCommand(i);
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			;
		}

		// キー押下時
		@Override
		public void keyPressed(KeyEvent e) {
			char ch = e.getKeyChar();

			// ENTER キー
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				ch = "=".toCharArray()[0];
			}
			// BackSpace キー
			else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				this.actionCommand(20);
				return;
			}

			for (int i=0;i < strButtons.length;i++) {
				if (strButtons[i].equals(String.valueOf(ch))) {
					this.actionCommand(i);
				}
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			;
		}
}

package bandittest;

import java.io.IOException;

public class GnuplotTest {

	public static void main(String[] args) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec("C:\\Program Files\\gnuplot\\bin\\wgnuplot");
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}


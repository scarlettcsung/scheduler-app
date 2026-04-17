package test;

import Main.Main;
import junit.framework.TestCase;

public class testMain extends TestCase {

    public void testMainRunsAndCoversLoadingAndSaving() throws Exception {

        Main mainApp = new Main();

        mainApp.main(new String[]{});

        // this does not seem to do anything for coverage but it should close the window the test calls
        /*
        for (Frame frame : Frame.getFrames()) {
            frame.dispatchEvent(
                new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
            );
        }
		*/
        Thread.sleep(300);
    }
}
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

  public static void main(String[] args) {
    GLProfile glprofile = GLProfile.getDefault();
    GLCapabilities glcapabilities = new GLCapabilities(glprofile);
    final GLCanvas glcanvas = new GLCanvas(glcapabilities);

    final Frame frame = new Frame("Lab 1");
    frame.add(glcanvas);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowevent) {
        frame.remove(glcanvas);
        frame.dispose();
        System.exit(0);
      }
    });

    frame.setSize(1500, 1000);
    frame.setVisible(true);

    long firstSceneAnimationTime = 3 * 1000;
    long secondSceneAnimationTime = 2 * 1000;
    long secondSceneAnimationDelay = 2 * 1000;

    Draw1 draw1 = new Draw1(firstSceneAnimationTime);
    glcanvas.addGLEventListener(draw1);

    FPSAnimator animator = new FPSAnimator(glcanvas, 65, true);
    animator.start();

    try {
      Thread.sleep(firstSceneAnimationTime + 2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    glcanvas.removeGLEventListener(draw1);
    glcanvas.addGLEventListener(new Draw2(secondSceneAnimationTime, secondSceneAnimationDelay));
  }
}
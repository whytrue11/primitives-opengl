import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.Timer;
import java.util.TimerTask;


public class Draw1 implements GLEventListener {
  private final double ROTATE_SPEED = 1.5;

  private final double FINAL_TORUS_SCALE = 0.3;
  private final float CUBE_SIDE = 1;
  private final long TORUS_SCALE_TIME;

  private double torusScale = 1;
  private double rotateAngle = 0;

  private GL2 gl;
  private GLUT glut;

  public Draw1(long animationTime) {
    TORUS_SCALE_TIME = animationTime;
  }

  @Override
  public void init(GLAutoDrawable glAutoDrawable) {
    glut = new GLUT();
    gl = glAutoDrawable.getGL().getGL2();
    gl.glEnable(GL2.GL_DEPTH_TEST);

    Timer timer = new Timer();
    double torusScaleDifference = (torusScale - FINAL_TORUS_SCALE) / TORUS_SCALE_TIME * 5;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (Double.compare(torusScale - torusScaleDifference / 2, FINAL_TORUS_SCALE) <= 0) {
          timer.cancel();
          //System.out.println(torusScale + " " + FINAL_TORUS_SCALE);
        }
        torusScale -= torusScaleDifference;
      }
    }, 0, 4);
  }

  @Override
  public void dispose(GLAutoDrawable glAutoDrawable) {
  }

  @Override
  public void display(GLAutoDrawable glAutoDrawable) {
    //цвет фона
    gl.glClearColor(0, 0, 0, 0);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    displayCube();
    displayTorus();

    rotateAngle += 0.1 * ROTATE_SPEED;
  }

  private void displayTorus() {
    gl.glLoadIdentity();
    gl.glPushMatrix();

    gl.glTranslated( 0, 0, -4.0);
    gl.glScaled(torusScale, torusScale, torusScale);
    gl.glRotated(rotateAngle, 1, 1, 1);
    gl.glColor3d(1, 1, 0);

    double tubeRadius = CUBE_SIDE / 6;
    double pathRadius = CUBE_SIDE / 2 - tubeRadius;
    glut.glutWireTorus(tubeRadius, pathRadius, 8, 32);

    gl.glPopMatrix();
  }

  private void displayCube() {
    gl.glLoadIdentity();
    gl.glPushMatrix();

    //matrix transformation
    gl.glTranslated( 0, 0, -4.0);
    gl.glRotated(rotateAngle, 1, 1, 1);
    gl.glColor3d(0, 1, 0);

    //figure
    glut.glutWireCube(CUBE_SIDE);

    //end
    gl.glPopMatrix();
  }

  @Override
  public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
    gl.glViewport(0, 0, width, height);
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();

    GLU glu = new GLU();
    glu.gluPerspective( 45.0f, (float) width / height, 1.0, 20.0 );

    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glLoadIdentity();
  }
}

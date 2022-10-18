import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.Timer;
import java.util.TimerTask;


public class Draw2 implements GLEventListener {

  private final double ROTATE_SPEED = 1.5;

  private final float SIZE = 1; //relative size
  private final double SCENE_SCALE = -6;
  private final long SPHERE_MOVE_DELAY;
  private final long SPHERE_MOVE_TIME;

  private double sphereMoveY = 0;
  private double sphereRadius = Math.sqrt(3) * SIZE / 2;

  private double coneRadius = SIZE / 2;
  private double rotateAngle = 0;


  private GL2 gl;
  private GLUT glut;

  long startTime;

  public Draw2(long animationTime, long animationDelay) {
    startTime = System.currentTimeMillis();

    SPHERE_MOVE_TIME = animationTime;
    SPHERE_MOVE_DELAY = animationDelay;
  }

  @Override
  public void init(GLAutoDrawable glAutoDrawable) {
    glut = new GLUT();
    gl = glAutoDrawable.getGL().getGL2();
    gl.glEnable(GL2.GL_DEPTH_TEST);

    Timer timer = new Timer();
    double sphereMoveDifference = 2 * SIZE / SPHERE_MOVE_TIME;
    double sphereRadiusDifference = (sphereRadius - coneRadius) / SPHERE_MOVE_TIME;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            if (sphereMoveY >= 2 * SIZE) {
              timer.cancel();
            }
            sphereRadius -= sphereRadiusDifference;
            sphereMoveY += sphereMoveDifference;
          }
        }, 10, 1);
      }
    }, SPHERE_MOVE_DELAY);
  }

  @Override
  public void dispose(GLAutoDrawable glAutoDrawable) {
  }

  @Override
  public void display(GLAutoDrawable glAutoDrawable) {
    //цвет фона
    gl.glClearColor(0, 0, 0, 0);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

    displaySphere();
    displayCone();
    displayCube();

    rotateAngle += 0.1 * ROTATE_SPEED;
  }

  private void displayCone() {
    gl.glLoadIdentity();
    gl.glPushMatrix();

    double h = SIZE * 2;
    gl.glTranslated( 0, SCENE_SCALE + (-SCENE_SCALE) * 0.8, SCENE_SCALE);
    gl.glRotated(rotateAngle, 0, 1, 0);

    gl.glTranslated( 0, 2 * SIZE, 0);
    //rotate top down
    gl.glRotated(180, 0, 1, 1);
    gl.glRotated(180, 0, 1, 0);

    gl.glColor3d(0, 0.9, 1);

    glut.glutWireCone(coneRadius, h, 20, 10);

    gl.glPopMatrix();
  }

  private void displaySphere() {
    gl.glLoadIdentity();
    gl.glPushMatrix();

    //matrix transformation
    gl.glTranslated( 0, SCENE_SCALE + (-SCENE_SCALE) * 0.8, SCENE_SCALE);
    gl.glRotated(rotateAngle, 0, 1, 0);
    gl.glColor3d(0, 0.4, 1);

    gl.glTranslated( 0, sphereMoveY, 0);

    //figure
    glut.glutWireSphere(sphereRadius, 20, 10);

    //end
    gl.glPopMatrix();
  }

  private void displayCube() {
    gl.glLoadIdentity();
    gl.glPushMatrix();

    //matrix transformation
    gl.glTranslated( 0, SCENE_SCALE + (-SCENE_SCALE) * 0.8, SCENE_SCALE);
    gl.glRotated(rotateAngle, 0, 1, 0);
    gl.glColor3d(0, 1, 0);

    //figure
    glut.glutWireCube(SIZE);

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

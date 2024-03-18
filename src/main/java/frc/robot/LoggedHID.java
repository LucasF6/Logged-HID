package frc.robot;

import java.util.HashSet;
import java.util.Set;

import org.littletonrobotics.junction.inputs.LoggedDriverStation;
import org.littletonrobotics.junction.inputs.LoggedDriverStation.JoystickInputs;

public class LoggedHID {
  private static Set<LoggedHID> m_loggedHIDs = new HashSet<>();

  private final int m_port;
  private boolean[] m_buttonsPressed;
  private boolean[] m_buttonsReleased;

  private JoystickInputs m_inputs;

  public LoggedHID(int port) {
    m_port = port;

    for (LoggedHID hid : m_loggedHIDs) {
      if (hid.m_port == port) {
        throw new IllegalArgumentException("LoggedHID with port " + m_port + " already instantiated!");
      }
    }

    m_loggedHIDs.add(this);

    m_inputs = LoggedDriverStation.getJoystickData(m_port);
    m_buttonsPressed = new boolean[m_inputs.buttonCount];
    m_buttonsReleased = new boolean[m_inputs.buttonCount];
  }

  public static void run() {
    for (LoggedHID hid : m_loggedHIDs) {
      hid.periodic();
    }
  }

  public void periodic() {
    m_inputs = LoggedDriverStation.getJoystickData(m_port);

    if (m_inputs.buttonCount != m_buttonsPressed.length) {
      m_buttonsPressed = new boolean[m_inputs.buttonCount];
      m_buttonsReleased = new boolean[m_inputs.buttonCount];
    }
  }

  public boolean getRawButton(int button) {
    if (button <= 0 || button > m_inputs.buttonCount) {
      return false;
    }
    return m_inputs.buttonValues / Math.pow(2, button - 1) % 2 == 1;
  }

  public boolean getRawButtonPressed(int button) {
    if (button <= 0 || button > m_buttonsPressed.length) {
      return false;
    }
    boolean prev = m_buttonsPressed[button - 1];
    m_buttonsPressed[button - 1] = m_inputs.buttonValues / Math.pow(2, button - 1) % 2 == 1;
    return !prev && m_buttonsPressed[button - 1];
  }

   public boolean getRawButtonReleased(int button) {
    if (button <= 0 || button > m_buttonsReleased.length) {
      return false;
    }
    boolean prev = m_buttonsReleased[button - 1];
    m_buttonsReleased[button - 1] = m_inputs.buttonValues / Math.pow(2, button - 1) % 2 == 1;
    return !prev && m_buttonsReleased[button - 1];
  }

  public double getRawAxis(int axis) {
    if (axis < 0 || axis >= m_inputs.axisTypes.length) {
      return 0;
    }
    return m_inputs.axisValues[axis];
  }

}

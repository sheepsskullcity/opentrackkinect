package org.opentrackkinect;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

public class Main {
	
	static Skeleton skeletons[] = new Skeleton[6];
	static float X, Y, Z;
	static Kinect myKinect;
    static JLabel label1 = new JLabel("x : " + "0");
    static JLabel label2 = new JLabel("y : " + "0");
    static JLabel label3 = new JLabel("z : " + "0");
    static JLabel xraw  = new JLabel("xraw : ");
    static JLabel yraw  = new JLabel("yraw : ");
    static JLabel zraw  = new JLabel("zraw : ");
    static float MX = 100;
    static float MY = 100;
    static float MZ = 100;
    static float DX = 0;
    static float DY = 0;
    static float DZ = 0;
    static final int M_MIN = 0;
    static final int M_MAX = 200;
    static int MX_INIT = 100;
    static int MY_INIT = 100;
    static int MZ_INIT = 100;
    static int DX_INIT = 0;
    static int DY_INIT = 0;
    static int DZ_INIT = 0;
    static final int D_MIN = -300;
    static final int D_MAX = 300;
    static final String addr = "localhost";
    static final int port = 4242;
    static final String fname = "config";
	
	
    public static void main(String[] args) {   	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
    	
    	loadConfig();
    	
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame();
        
        JButton button = new JButton();
        button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveConfig();
			}});
        button.setText("SAVE");
        
        JSlider sliderX = new JSlider(JSlider.HORIZONTAL,
        		M_MIN, M_MAX, MX_INIT);
        sliderX.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        int i = (int)source.getValue();
			        if (i == 0) {
			        	MX = 1;
			        } else {
			        	MX = (float) i;
			        }
			    }
			}});
        
        JSlider sliderY = new JSlider(JSlider.HORIZONTAL,
        		M_MIN, M_MAX, MY_INIT);
        sliderY.addChangeListener(new ChangeListener(){

        	@Override
        	public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        int i = (int)source.getValue();
			        if (i == 0) {
			        	MY = 1;
			        } else {
			        	MY = (float) i;
			        }
			    }
        	}});
        
        JSlider sliderZ = new JSlider(JSlider.HORIZONTAL,
                M_MIN, M_MAX, MZ_INIT);
        sliderZ.addChangeListener(new ChangeListener(){

        	@Override
        	public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        int i = (int)source.getValue();
			        if (i == 0) {
			        	MZ = 1;
			        } else {
			        	MZ = (float) i;
			        }
			    }
        	}});
        
        JSlider sliderDX = new JSlider(JSlider.HORIZONTAL,
                D_MIN, D_MAX, DX_INIT);
        sliderDX.addChangeListener(new ChangeListener(){

        	@Override
        	public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			    	DX = (float) source.getValue();
			    }
        	}});
        
        JSlider sliderDY = new JSlider(JSlider.HORIZONTAL,
                D_MIN, D_MAX, DY_INIT);
        sliderDY.addChangeListener(new ChangeListener(){

        	@Override
        	public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			    	DY = (float) source.getValue();
			    }
        	}});
        
        JSlider sliderDZ = new JSlider(JSlider.HORIZONTAL,
                D_MIN, D_MAX, DZ_INIT);
        sliderDZ.addChangeListener(new ChangeListener(){

        	@Override
        	public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			    	DZ = (float) source.getValue();
			    }
        	}});

        sliderX.setMajorTickSpacing(50);
        sliderX.setMinorTickSpacing(25);
        sliderX.setPaintTicks(true);
        sliderX.setPaintLabels(true);
        sliderY.setMajorTickSpacing(50);
        sliderY.setMinorTickSpacing(25);
        sliderY.setPaintTicks(true);
        sliderY.setPaintLabels(true);
        sliderZ.setMajorTickSpacing(50);
        sliderZ.setMinorTickSpacing(25);
        sliderZ.setPaintTicks(true);
        sliderZ.setPaintLabels(true);
        sliderDX.setMajorTickSpacing(50);
        sliderDX.setMinorTickSpacing(25);
        sliderDX.setPaintTicks(true);
        sliderDX.setPaintLabels(true);
        sliderDY.setMajorTickSpacing(50);
        sliderDY.setMinorTickSpacing(25);
        sliderDY.setPaintTicks(true);
        sliderDY.setPaintLabels(true);
        sliderDZ.setMajorTickSpacing(50);
        sliderDZ.setMinorTickSpacing(25);
        sliderDZ.setPaintTicks(true);
        sliderDZ.setPaintLabels(true);
        
        JFrame.setDefaultLookAndFeelDecorated(true);
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setMinimumSize(new Dimension(600,600));
        f.add(label1);
        f.add(label2);
        f.add(label3);
        
        
        JLabel lxscale  = new JLabel("xscale");
        JLabel lyscale  = new JLabel("yscale");
        JLabel lzscale  = new JLabel("zscale");
        JLabel lxdelta  = new JLabel("xdelta");
        JLabel lydelta  = new JLabel("ydelta");
        JLabel lzdelta  = new JLabel("zdelta");
        
        f.add(lxscale);
        f.add(sliderX);
        f.add(lyscale);
        f.add(sliderY);
        f.add(lzscale);
        f.add(sliderZ);
        f.add(lxdelta);
        f.add(sliderDX);
        f.add(lydelta);
        f.add(sliderDY);
        f.add(lzdelta);
        f.add(sliderDZ);
        f.add(button);
        f.add(xraw);
        f.add(yraw);
        f.add(zraw);
        f.setVisible(true);
        
        label1.setFont(new Font("Serif", Font.PLAIN, 24));
        label2.setFont(new Font("Serif", Font.PLAIN, 24));
        label3.setFont(new Font("Serif", Font.PLAIN, 24));
        
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lxscale.setAlignmentX(Component.CENTER_ALIGNMENT);
        lyscale.setAlignmentX(Component.CENTER_ALIGNMENT);
        lzscale.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lxdelta.setAlignmentX(Component.CENTER_ALIGNMENT);
        lydelta.setAlignmentX(Component.CENTER_ALIGNMENT);
        lzdelta.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        xraw.setAlignmentX(Component.CENTER_ALIGNMENT);
        yraw.setAlignmentX(Component.CENTER_ALIGNMENT);
        zraw.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        f.setTitle("Intitializing Kinect...");
		myKinect = new Kinect();
		if(!myKinect.start(J4KSDK.DEPTH | J4KSDK.SKELETON))
		{
			f.setTitle("Kinect not detected");
			JOptionPane.showMessageDialog(f, "There was no Kinect sensor detected in your system.");
		} else {
			f.setTitle("Kinect demo");
		}
    }

	public static class Kinect extends J4KSDK {
		
		public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions,float[] orientations, byte[] joint_status) {
			if (skeletons == null) return;
			for(int i=0;i<this.getMaxNumberOfSkeletons();i++) {
			skeletons[i]=Skeleton.getSkeleton(i, skeleton_tracked, positions,orientations,joint_status,this);
			if (skeletons[i] != null
					&& skeletons[i].isTracked()) {
				X = skeletons[i].get3DJointX(Skeleton.HEAD);
				Y = skeletons[i].get3DJointY(Skeleton.HEAD);
				Z = skeletons[i].get3DJointZ(Skeleton.HEAD);
				float TX = (X * MX) + ((DX / 100) * MX);
				float TY = (Y * MY) + ((DY / 100) * MY);
				float TZ = (Z * MZ) + ((DZ / 100) * MZ);
				
				ByteBuffer b = ByteBuffer.allocate(48);
				b.order(ByteOrder.LITTLE_ENDIAN);
		        b.putDouble((double) TX);
		        b.putDouble((double) TY);
		        b.putDouble((double) TZ);
		        b.putDouble(0.0);
		        b.putDouble(0.0);
		        b.putDouble(0.0);
		        
		        label1.setText("x : " + String.format("%.3f", TX));
		        label2.setText("y : " + String.format("%.3f", TY));
		        label3.setText("z : " + String.format("%.3f", TZ));
		        
		        xraw.setText("xraw : " + String.format("%.3f", X));
		        yraw.setText("yraw : " + String.format("%.3f", Y));
		        zraw.setText("zraw : " + String.format("%.3f", Z));
		        
		        try {
					DatagramPacket packet = new DatagramPacket(b.array(), b.capacity(),
							InetAddress.getByName(addr), port);
					DatagramSocket dsocket = new DatagramSocket();
					dsocket.send(packet);
					dsocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			}
		}
		
		@Override
		public void onColorFrameEvent(byte[] color_frame) {}
		
		@Override
		public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv) {}


	}
	
	public static void loadConfig() {
    	File f = new File(fname);
    	if (f.exists()) {
    		FileInputStream fis = null;
    		byte[] barray;
    		try {
				fis = new FileInputStream (f);
				barray = new byte [24];
				fis.read(barray, 0, 24);
				ByteBuffer b = ByteBuffer.allocate(24);
				b.put(barray);
				b.rewind();
				MX = b.getFloat();
				MY = b.getFloat();
				MZ = b.getFloat();
				DX = b.getFloat();
				DY = b.getFloat();
				DZ = b.getFloat();
			    MX_INIT = (int) MX;
			    MY_INIT = (int) MY;
			    MZ_INIT = (int) MZ;
			    DX_INIT = (int) DX;
			    DY_INIT = (int) DY;
			    DZ_INIT = (int) DZ;
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    	}
	}

	public static void saveConfig() {
		File f = new File(fname);
		ByteBuffer b = ByteBuffer.allocate(24);
        b.putFloat(MX);
        b.putFloat(MY);
        b.putFloat(MZ);
        b.putFloat(DX);
        b.putFloat(DY);
        b.putFloat(DZ);
        FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(b.array());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
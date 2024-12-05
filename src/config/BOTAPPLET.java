/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

/**
 *
 * @author Admin
 */
public class BOTAPPLET {
     public static final byte[] AID_APPLET = {
        (byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44, (byte)0x55, (byte)0x00, (byte)0x00
    };
    
    
    public final static byte CLA =(byte)0xA0;
    
    public final static byte INS_SETUP = (byte) 0x2A;
	//INS-PIN
    public final static byte INS_CREATE_PIN = (byte) 0x40;
    public final static byte INS_VERIFY_PIN = (byte) 0x42;
    public final static byte INS_CHANGE_PIN = (byte) 0x44;
    public final static byte INS_UNBLOCK_PIN = (byte) 0x46;
    
    public final static byte INS_CHANGE_INFORMATION = (byte) 0x52;
    public final static byte INS_CREATE_INFORMATION = (byte)0x50;
    public final static byte INS_OUT_INFORMATION = (byte)0x51;
    
    public final static byte OUT_ID = (byte)0x01;
    public final static byte OUT_NAME = (byte)0x02;
    public final static byte OUT_DATE = (byte)0x03;
    public final static byte OUT_PHONE = (byte)0x04;
    
    public final static byte INS_CREATE_IMAGE = (byte)0x53;
    public final static byte INS_CREATE_SIZEIMAGE = (byte)0x54;//countanh
    public final static byte INS_OUT_SIZEIMAGE = (byte)0x55;
    public final static byte INS_OUT_IMAGE = (byte)0x56;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bot_card;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class ConnectJavaCard {
    byte [] data;
    public String strID;
    public String strName;
    public String strDate;
    public String strAddress;
    public String strNumberPlate;
    public String message;
  public String connectapplet() {
      String kq="";
  try {
   // Display the list of terminals
   TerminalFactory factory = TerminalFactory.getDefault();
   List<CardTerminal> terminals = factory.terminals().list();
   System.out.println("Terminals: " + terminals);

   // Use the first terminal
   CardTerminal terminal = terminals.get(0);

   // Connect wit hthe card
   Card card = terminal.connect("*");
   System.out.println("card: " + card);
   CardChannel channel = card.getBasicChannel();

   // Send Select Applet command
   byte[] aid = {(byte) 0x11,0x22,0x33,0x44,0x55,0x01};
   ResponseAPDU answer = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
   kq = answer.toString();
    data = answer.getData();
   System.out.println("answer::::: " + answer.toString());
     return kq;
   } catch(Exception e) {
   System.out.println("Error::::: " + e.toString());
    }
  return kq;
 }
  
public boolean disconnectCard(){
    try {
         TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();
        System.out.println("Terminals: " + terminals);

   // Use the first terminal
         CardTerminal terminal = terminals.get(0);

   // Connect wit hthe card
         Card card = terminal.connect("*");
         card.disconnect(true);
         return true;
    } catch (Exception e) {
        System.out.println("ERROR::::::::"+e);
    }
    return false;
}

   public void setUp(){
        
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,config.BOTAPPLET.INS_SETUP,0x00,0x00));
            
        }
        catch(Exception ex){
            //return "Error";
        }
    
    }
  
public boolean createPIN(String pin){
        
        byte[] pinbyte =  pin.getBytes();
        byte len = (byte) pinbyte.length;
        
        byte[] send = new byte[len+1];
        send[0] = len;
        for(int i =1;i<send.length;i++){
            send[i] = pinbyte[i-1];
        }
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xA0,config.BOTAPPLET.INS_CREATE_PIN,0x00,0x03,send));
            
            message = answer.toString();
            switch (((message.split("="))[1]).toUpperCase()) {
                case config.STATUS.SW_NO_ERROR:
                    return true;
                case config.STATUS.SW_INVALID_PARAMETER:
                    JOptionPane.showMessageDialog(null, "Mã PIN không hợp lệ");
                    return false;
                case config.STATUS.SW_WRONG_LENGTH:
                    JOptionPane.showMessageDialog(null, "Độ dài mã PIN không chính xác");
                    return false;
                default:
                    return false;
            }
            
        }
        catch(Exception ex){
            return false;
        }
    }
 public boolean verifyPin(String pin){
        connectapplet();
        byte[] pinbyte =  pin.getBytes();
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xA0,config.BOTAPPLET.INS_VERIFY_PIN,0x00,0x00,pinbyte));
            message = Integer.toHexString(answer.getSW());
            switch (message.toUpperCase()) {
                case config.STATUS.SW_NO_ERROR:
                    return true;
                case config.STATUS.SW_AUTH_FAILED:
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai mã PIN");
                    return false;
                case config.STATUS.SW_IDENTITY_BLOCKED:
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai quá 3 lần !!!Thẻ đã bị khoá");
                    return false;
                case config.STATUS.SW_INVALID_PARAMETER:
                    JOptionPane.showMessageDialog(null, "Độ dài PIN chưa hợp lệ");
                    return false;
                default:
                    return false;
            }
            
        }
        catch(Exception ex){
            return false;
        }
    }
 
 public boolean ChangePIN(String oldPin,String newPin){
        connectapplet();
        byte[] oldPinByte =  oldPin.getBytes();
        byte oldPinLen = (byte) oldPinByte.length;
        
        byte[] newPinByte =  newPin.getBytes();
        byte newPinLen = (byte) newPinByte.length;
        
        byte[] send = new byte[newPinLen+oldPinLen+2];
        int offSet = 0;
        send[offSet] = oldPinLen;
        offSet+=1;
        System.arraycopy(oldPinByte, 0, send, offSet, oldPinLen);
        offSet+=oldPinLen;
        send[offSet] = newPinLen;
        offSet+=1;
        System.arraycopy(newPinByte, 0, send, offSet, newPinLen);
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xA0,config.BOTAPPLET.INS_CHANGE_PIN,0x00,0x00,send));
            
            message = answer.toString();
            switch (((message.split("="))[1]).toUpperCase()) {
                case config.STATUS.SW_NO_ERROR:
                    JOptionPane.showMessageDialog(null, "Cập nhật PIN thành công!");
                    return true;
                case config.STATUS.SW_AUTH_FAILED:
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai PIN");
                    return false;
                case config.STATUS.SW_IDENTITY_BLOCKED:
                    JOptionPane.showMessageDialog(null, "Bạn đã nhập sai quá 3 lần !!!Thẻ đã bị khoá");
                    return false;
                default:
                    return false;
            }
        }
        catch(Exception ex){
            return false;
        }
    }
 
  public boolean UnblockPin(byte [] aid){
        try{
            
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            
            CardTerminal terminal = terminals.get(0);
            
            Card card = terminal.connect("*");
            
            CardChannel channel = card.getBasicChannel();
            
            ResponseAPDU selectBlockcard = channel.transmit(new CommandAPDU(0x00,0xA4,0x00,0x00,aid));
            
            String check = Integer.toHexString(selectBlockcard.getSW());
            
            if(check.equals(config.STATUS.SW_NO_ERROR)){
                CardChannel channel2 = card.getBasicChannel();
            
            ResponseAPDU unblockCard = channel2.transmit(new CommandAPDU(config.BOTAPPLET.CLA,config.BOTAPPLET.INS_UNBLOCK_PIN,0x00,0x00));
                message = unblockCard.toString();
                switch (((message.split("="))[1]).toUpperCase()) {
                    case config.STATUS.SW_NO_ERROR:
                        JOptionPane.showMessageDialog(null, "Mở khoá thẻ thành công");
                        return true;
                    case config.STATUS.SW_OPERATION_NOT_ALLOWED:
                        JOptionPane.showMessageDialog(null, "Thẻ không bị khoá vui lòng kiểm tra lại!");
                        return false;
                    default:
                        return false;
                }
            }
            else{
                return false;
            }
        }
        catch(Exception ex){
            return false;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bot_card;

import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
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
    
    // Properties
    byte [] data;
    byte[] image;
    public String strID;
    public String strName;
    public String strDate;
    public String strAddress;
    public String strNumberPlate;
    public String message;
    public boolean isSetup = false;
    public boolean isSetData = false;
    
    // Khai bao doi tuong ket noi applet
    TerminalFactory factory;
    List<CardTerminal> terminals;
    CardTerminal terminal;
    Card card;
    CardChannel channel;
  public String connectapplet() {
      String kq="";
  try {
   // Display the list of terminals
   factory = TerminalFactory.getDefault();
   terminals = factory.terminals().list();
   System.out.println("Terminals: " + terminals);

   // Use the first terminal
   terminal = terminals.get(0);

   // Connect wit hthe card
   card = terminal.connect("*");
   System.out.println("card: " + card);
   channel = card.getBasicChannel();

   // Send Select Applet command
   byte[] aid = {(byte) 0x11,0x22,0x33,0x44,0x55,0x01};
   ResponseAPDU answer = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
   kq = answer.toString();
   data = answer.getData();
   System.out.println("answer::::: " + answer.toString());
   return kq;
   } catch(CardException e) {
   System.out.println("Error::::: " + e.toString());
   }
  return kq;
 }
  
public boolean disconnectCard(){
    try {
        factory = TerminalFactory.getDefault();
        terminals = factory.terminals().list();
        System.out.println("Terminals: " + terminals);

   // Use the first terminal
        terminal = terminals.get(0);

   // Connect wit hthe card
        card = terminal.connect("*");
        card.disconnect(true);
        return true;
    } catch (CardException e) {
        System.out.println("ERROR::::::::"+e);
    }
    return false;
}

   public void setUp(){
        
        try{
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            
            terminal = terminals.get(0);
            
            card = terminal.connect("*");
            
            channel = card.getBasicChannel();
            
            ResponseAPDU answer = channel.transmit(new CommandAPDU(0xB0,config.BOTAPPLET.INS_SETUP,0x00,0x00));
            
            isSetup = true;
        }
        catch(CardException ex){
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
            
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            
            terminal = terminals.get(0);
            
            card = terminal.connect("*");
            
            channel = card.getBasicChannel();
            
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
            
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            
            terminal = terminals.get(0);
            
            card = terminal.connect("*");
            
            channel = card.getBasicChannel();
            
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
            
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            
            terminal = terminals.get(0);
            
            card = terminal.connect("*");
            
            channel = card.getBasicChannel();
            
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
            
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            
            terminal = terminals.get(0);
            
            card = terminal.connect("*");
            
            channel = card.getBasicChannel();
            
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
  
    public ResponseAPDU sendRequest(CommandAPDU commandAPDU){
        ResponseAPDU respond;
        String kq = connectapplet();
        if(!kq.contains("SW=9000")) {
            System.out.println("Connection error");
        } else {
            System.out.println("Connect to card");
            if (!isSetup) {
                System.out.println("You need run set up");
                setUp();
                System.out.println("Set up done!");
            }
            
            try {
                factory = TerminalFactory.getDefault();
                terminals = factory.terminals().list();
                terminal = terminals.get(0);
                card = terminal.connect("*");
                channel = card.getBasicChannel();

                respond = channel.transmit(commandAPDU);
                return respond;
            } catch (CardException e) {

            }
            
        }
        return null;
    }
    
    public boolean checkStatus() {
        ResponseAPDU respond;
        String kq = connectapplet();
        if(!kq.contains("SW=9000")) {
            System.out.println("Connection error");
            return false;
        } else {
            System.out.println("Connect to card");
            if (!isSetup) {
                System.out.println("You need run set up");
                setUp();
                return true;
            } else {
                return true;
            }
        }
    }
    
    public void sendChunks(List<byte[]> chunks) throws CardException {
        
        String kq = connectapplet();
        if(!kq.contains("SW=9000")) {
            System.out.println("Connection error");
        } else {
            System.out.println("Connect to card");
            if (!isSetup) {
                System.out.println("You need run set up");
                setUp();
                System.out.println("Set up done!");
            }
            
            try {
                factory = TerminalFactory.getDefault();
                terminals = factory.terminals().list();
                terminal = terminals.get(0);
                card = terminal.connect("*");
                channel = card.getBasicChannel();
        
                for (byte[] chunk : chunks) {
                    CommandAPDU commandAPDU = new CommandAPDU(0x00, config.BOTAPPLET.INS_CREATE_IMAGE, 0x00, 0x00, chunk);
                    ResponseAPDU responseAPDU = channel.transmit(commandAPDU);
                    if (responseAPDU.getSW() != 0x9000) {
                        throw new CardException("Error sending chunk: " + Integer.toHexString(responseAPDU.getSW()));
                    }
                }
            } catch(CardException e) {
            
            }
        }
    }
    
}

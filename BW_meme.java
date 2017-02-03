import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BW_meme {
  static public void main(String args[]) throws Exception {
    try {
      int width = 900, height = 541;

      // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
      // into integer pixels
      BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

      Graphics2D ig2 = bi.createGraphics();

      RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      ig2.setRenderingHints(rh);
      ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      ig2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      
      BufferedImage background = null;
      try {
        background = ImageIO.read(new File("hintergrund r.png"));
      } catch (IOException e) {}

      BufferedImage bwlogo = null;
      try {
        bwlogo = ImageIO.read(new File("BW logo.png"));
      } catch (IOException e) {}

      ig2.drawImage(background, width/2, 0, null);

      ig2.drawImage(background, width/2 + 1, 0, -background.getWidth(null) + 1, background.getHeight(null), null);

      ig2.drawImage(bwlogo, width/2 - bwlogo.getWidth(null)/2, height - bwlogo.getHeight(null), null);

      int rectWidth = (int)((double)(height - bwlogo.getHeight(null)));
      int rectHeight = (int)((double)(height - bwlogo.getHeight(null)) * 0.8);
      int rectX = width/2 - rectWidth/2;
      int rectY =height/2 - rectHeight/2 - bwlogo.getHeight(null)/2;
      ig2.setStroke(new BasicStroke(5));
      ig2.drawRect(rectX, rectY, rectWidth, rectHeight);

      int triHeigth = 20;
      int triX = width/2;
      int triY = height/2 + rectHeight/2 - bwlogo.getHeight(null)/2;
      int triXpoints[] = {triX, triX - triHeigth, triX + triHeigth};
      int triYpoints[] = {triY + triHeigth, triY, triY};
      Polygon tri = new Polygon(triXpoints ,triYpoints ,3);
      ig2.fillPolygon(tri);
      
      //*************
      //Text handling
      //*************
      int maxCharsPerRow = 20;
      System.out.println("Args: " + args.length);
      String[] messageIn = {"just pass","the text as","arguments","line by line."};

      if (args.length != 0) {
      	messageIn = args;
      }
      /*
      //Separate into Words:
      String[] words = messageIn.split(" ");

      List<String> sectionsIn = new ArrayList<>();

      for (int i = 0; i<words.length; i++) {
        if (words[i] == words[i].toUpperCase()) {
            sectionsIn.add(words[i]);
            sectionsIn.add("");
        } else {
            if (sectionsIn.size() == 0) {
                sectionsIn.add(words[i]);
            } else {
                if (sectionsIn.get(sectionsIn.size()-1) != "") {
                    sectionsIn.set(sectionsIn.size()-1, sectionsIn.get(sectionsIn.size()-1) + " ");
                }
                sectionsIn.set(sectionsIn.size()-1, sectionsIn.get(sectionsIn.size()-1) + words[i]);
            }
        }
        System.out.println(words[i]);
      }


      for (int i = 0; i < sectionsIn.size(); i++) {
          System.out.println(sectionsIn.get(i));
          if (sectionsIn.get(i).length() > 20) {
            String[] temp = sectionsIn.get(i).split(" ");
            int sentence = sectionsIn.get(i).length() - temp.length - 1;
            int sum = 0;
            for (int j = 0; j < temp.length; j++) {
                sum = sum + temp[i].length();
                if (sum >= sentence/2) {
                    break;
                }
            }
            
              
          }
      }

      */
      //create Fonts
      Font[] fonts = new Font[messageIn.length];
      for (int i = 0; i < messageIn.length; i++) {
      	fonts[i] = Font.decode("Arial Narrow-Bold");
      }
      Rectangle2D[] bounds = new Rectangle2D[messageIn.length];

      //caluclate available space:
      int spaceWidth = (int) (rectWidth * 0.9);
      int spaceHeight = (int) (rectHeight * 0.95);
      System.out.println("rectWidth: " + rectWidth + " rectHeight: " + rectHeight);
      System.out.println("spaceWidth: " + spaceWidth + " spaceHeight: " + spaceHeight);



      //get font dimensions
      int actualHeight = 0;
      System.out.println("Calculated Font Dimensions:");
      for (int i = 0; i < messageIn.length; i++) {
      	messageIn[i] = messageIn[i].toUpperCase();
      	bounds[i] = ig2.getFontMetrics(fonts[i]).getStringBounds(messageIn[i], ig2);
      	fonts[i] = fonts[i].deriveFont((float)(fonts[i].getSize2D() * spaceWidth/bounds[i].getWidth()));
      	System.out.println(ig2.getFontMetrics(fonts[i]).getAscent());
      	actualHeight = actualHeight + ig2.getFontMetrics(fonts[i]).getAscent();
      }
      //Does it fit?
      if (actualHeight > spaceHeight) {
      	System.out.println("Scheisse " + actualHeight + " ist zu viel!");
      }

      int spacing = (spaceHeight - actualHeight) / messageIn.length;
      System.out.println("spacing: " + spacing);
      int posX = rectX + (rectWidth - spaceWidth) / 2 ;
      int posY = rectY; //+ (rectHeight - spaceHeight) / 2 ;
      ig2.setFont(fonts[0]);
      ig2.setPaint(Color.white);
      ig2.drawString(messageIn[0], posX, posY + ig2.getFontMetrics(fonts[0]).getAscent() + spacing);
      posY = posY + spacing + ig2.getFontMetrics(fonts[0]).getAscent();
      for (int i = 1; i < messageIn.length; i++) {
      	ig2.setFont(fonts[i]);
      	ig2.setPaint(Color.white);
      	ig2.drawString(messageIn[i], posX, posY + ig2.getFontMetrics(fonts[i]).getAscent());
      	posY = posY + spacing + ig2.getFontMetrics(fonts[i]).getAscent();
      }


      /*
      String message = messageIn[0].toUpperCase();
      Font font = new Font("Arial Narrow", Font.BOLD, 100);
      ig2.setFont(font);
      FontMetrics fontMetrics = ig2.getFontMetrics();
      int stringWidth = fontMetrics.stringWidth(message);
      int stringHeight = fontMetrics.getAscent();
      ig2.setPaint(Color.white);
      ig2.drawString(message, (width - stringWidth) / 2, height / 2 + stringHeight / 4);
      */
      String filename = messageIn[0];
      for (int i = 1; i < messageIn.length; i++) {
      	filename = filename + " " + messageIn[i];
      }
      ImageIO.write(bi, "PNG", new File("BW/" + filename + ".png"));
      //ImageIO.write(bi, "JPEG", new File("yourImageName.JPG"));
      //ImageIO.write(bi, "gif", new File("yourImageName.GIF"));
      
    } catch (IOException ie) {
      ie.printStackTrace();
    }

  }
  private void drawBWFrame(BufferedImage logo, BufferedImage background, double percent) {



  	return;
  }



}
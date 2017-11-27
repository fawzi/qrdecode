package eu.kitabi.qrdecode;

import java.io.FileInputStream;
import scala.collection.JavaConverters._
import com.google.zxing.DecodeHintType

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

object QrDecoder {

  def readQRCode(filePath: String): String = {
    val binaryBitmap: BinaryBitmap = new BinaryBitmap(new HybridBinarizer(
      new BufferedImageLuminanceSource(
        ImageIO.read(new FileInputStream(filePath)))));
    val qrCodeResult : Result = new QRCodeReader().decode(binaryBitmap, Map(DecodeHintType.TRY_HARDER -> true).asJava);
    return qrCodeResult.getText();
  }

  def main(args: Array[String]): Unit = {
    if (args.length == 1) {
      val filePath = args(0)
      println(readQRCode(filePath))
    } else {
      println(s"no file path of file to decode given! ${args.mkString("[",",","]")}")
    }
  }
}

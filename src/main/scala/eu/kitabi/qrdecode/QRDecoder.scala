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
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File
import scala.util.control.NonFatal

object QrDecoder {

  def readQRCode(filePath: File): String = {
    val binaryBitmap: BinaryBitmap = new BinaryBitmap(new HybridBinarizer(
      new BufferedImageLuminanceSource(
        ImageIO.read(new FileInputStream(filePath)))));
    val qrCodeResult : Result = new QRCodeReader().decode(binaryBitmap, Map(DecodeHintType.TRY_HARDER -> true).asJava);
    return qrCodeResult.getText();
  }

  val doc = """ qrdecoder [--help] [--out-dir <outdir>] [--in-dir <indir>] [--dry-run] [--strict] [<file1> ...]"""

  def main(args: Array[String]): Unit = {
    var outDir: Option[String] = None
    var dryRun: Boolean = false
    var inDir: Option[String] = None
    var inFiles: Seq[String] = Seq()
    var strict: Boolean = false
    var ii: Int = 0
    while (ii < args.length) {
      val arg = args(ii)
      ii += 1
      arg match {
        case "--help" =>
          println(doc)
          return ()
        case "--out-dir" =>
          if (ii >= args.length) {
            println(s"Missing out directory after --out-dir\n$doc")
            return ()
          }
          outDir = Some(args(ii))
          ii += 1
        case "--in-dir" =>
          if (ii >= args.length) {
            println(s"Missing out directory after --out-dir\n$doc")
            return ()
          }
          outDir = Some(args(ii))
          ii += 1
        case "--dry-run" =>
          dryRun = true
        case "--strict" =>
          strict = true
        case f =>
          inFiles = inFiles :+ f
      }
    }
    val outD = outDir match {
      case None =>
        if (!dryRun) {
          println(s"No out directory given, assuming --dry-run")
          dryRun = true
        }
        "."
      case Some(d) => d
    }

    var lastId: Option[String] = None
    def workOnFile(el: File): Unit = {
      if (el.isFile()) {
        var hasId: Boolean = false
        try {
          lastId = Some(readQRCode(el))
          hasId = true
        } catch {
          case NonFatal(e) => ()
        }
        val targetPath = Paths.get(outD,lastId.getOrElse("").stripPrefix("http://").stripPrefix("https://"), el.getName())
        if (hasId)
          println(s"${el.getName()} +> ${targetPath}")
        else if (strict)
          println(s"${el.getName()} -")
        else
          println(s"${el.getName()} -> ${targetPath}")
        if (!dryRun && (hasId || !strict)) {
          if (!Files.exists(targetPath.getParent()))
            Files.createDirectories(targetPath.getParent())
          Files.move(el.toPath(), targetPath)
        }
      }
    }

    for (d <- inDir) {
      val dd = new File(d)
      for (el <- dd.listFiles()) {
        workOnFile(el)
      }
    }
    lastId = None
    for (f <- inFiles) {
      val ff = new File(f)
      workOnFile(ff)
    }
  }
}

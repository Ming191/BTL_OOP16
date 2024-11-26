package org.library.btl_oop16_library.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import org.library.btl_oop16_library.Model.Book;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

public class ZXingAPI {

    public static Image toQRCode(Book currentBook, int width, int height) {
        try {
            BitMatrix bitMatrix = generateQRCode(currentBook.getPreviewURL(), width, height);
            return createImageFromBitMatrix(bitMatrix);

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BitMatrix generateQRCode(String text, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        return qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
    }

    private static Image createImageFromBitMatrix(BitMatrix bitMatrix) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            return new Image(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

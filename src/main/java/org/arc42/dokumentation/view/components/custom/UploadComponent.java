package org.arc42.dokumentation.view.components.custom;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.IOUtils;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.DesignDecisionDAO;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.util.data.NotificationType;

public class UploadComponent extends VerticalLayout {

  // --Commented out by Inspection (17.04.22, 17:54):private Upload bild;
  // --Commented out by Inspection (17.04.22, 17:54):private Upload uxf;

  private ImageDTO imageDTO;
  // --Commented out by Inspection (17.04.22, 17:54):private Image image;
  // --Commented out by Inspection (17.04.22, 17:54):private File file;
  private Div output;
  private final VerticalLayout imageLayout;
  private Span text;
  private final Upload uploadImage;
  private final Upload uploadUxf;

  public UploadComponent(ARC42DAOAbstract dao, String url) {

    // String actualPath = System.getProperty("user.dir");
    // File child = new File(actualPath);
    // File child = new File("src/main/resources/uploads/image.png");
    // String filePath = child.getParentFile().getPath();

    // String filePath = child.getParentFile().getPath() + "/uploads/";
    output = new Div();

    Button save = new Button("Speichern");
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button cancel = new Button("Löschen");
    cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

    HorizontalLayout horizontalLayout = new HorizontalLayout();

    MemoryBuffer bufferImage = new MemoryBuffer();
    uploadImage = new Upload(bufferImage);
    uploadImage.setAcceptedFileTypes("image/png", ".png");
    uploadImage.setMaxFiles(1);

    UploadI18N i18n = new UploadI18N();
    uploadImage.setI18n(i18n);
    i18n.setAddFiles(new UploadI18N.AddFiles());
    i18n.setDropFiles(new UploadI18N.DropFiles());
    i18n.setError(new UploadI18N.Error());
    i18n.getAddFiles().setOne("Bild hochladen ...");
    i18n.getDropFiles().setOne("Bild hierhin ziehen");
    i18n.getError()
        .setIncorrectFileType(
            "Die Datei hat das falsche Format. Bitte ein Bild mit dem Format .png hochladen");
    H4 title = new H4("Bild hochladen");
    title.getStyle().set("margin-top", "0");
    Paragraph hint = new Paragraph("Akzeptierte Bildformate: PNG (.png)");
    hint.getStyle().set("color", "var(--lumo-secondary-text-color)");
    imageLayout = new VerticalLayout();
    imageLayout.setPadding(false);
    imageLayout.setSpacing(false);
    imageLayout.add(title, hint, uploadImage);

    MemoryBuffer bufferUxf = new MemoryBuffer();
    uploadUxf = new Upload(bufferUxf);
    uploadUxf.setAcceptedFileTypes("application/octet-stream", ".uxf");
    uploadUxf.setMaxFiles(1);
    UploadI18N i18n1 = new UploadI18N();
    uploadUxf.setI18n(i18n1);
    i18n1.setAddFiles(new UploadI18N.AddFiles());
    i18n1.setDropFiles(new UploadI18N.DropFiles());
    i18n1.setError(new UploadI18N.Error());
    i18n1.getAddFiles().setOne("uxf Datei hochladen ...");
    i18n1.getDropFiles().setOne("uxf Datei hierhin ziehen");
    i18n1
        .getError()
        .setIncorrectFileType(
            "Die Datei hat das falsche Format. Bitte eine Datei mit dem Format .uxf hochladen");
    H4 title1 = new H4("Uxf-Datei hochladen");
    title1.getStyle().set("margin-top", "0");
    Paragraph hint1 = new Paragraph("Akzeptierte Dateifornate: UXF (.uxf)");
    hint1.getStyle().set("color", "var(--lumo-secondary-text-color)");
    VerticalLayout uxfLayout = new VerticalLayout();
    uxfLayout.setPadding(false);
    uxfLayout.setSpacing(false);
    uxfLayout.add(title1, hint1, uploadUxf);

    if (dao.getActualArcId(url) != null) {
      imageDTO = (ImageDTO) dao.findById(url);
      if (imageDTO != null) {
        if (this.imageDTO.showImage()) {
          InputStream targetStream = new ByteArrayInputStream(imageDTO.getBildStream());
          showOutput(
              imageDTO.getBildMimeType(),
              createComponent(imageDTO.getBildMimeType(), imageDTO.getBildName(), targetStream));
          text = new Span("Aktuell wird folgende .uxf Datei verwendet: " + imageDTO.getUxfName());
          uxfLayout.add(text);
        }
      } else {
        text = new Span("Noch nichts hochgeladen");
        uxfLayout.add(text);
      }
    }

    uploadImage.addSucceededListener(
        event -> {
          if (imageDTO == null) {
            imageDTO = new ImageDTO();
          }
          output.setVisible(true);
          InputStream fileData = bufferImage.getInputStream();
          imageDTO.setBildMimeType(event.getMIMEType());
          imageDTO.setBildName(event.getFileName());
          try {
            imageDTO.setBildPath(fileData.readAllBytes());
          } catch (IOException e) {
            e.printStackTrace();
          }
          // File file = new File(filePath + event.getFileName());
          try {
            // FileUtils.copyInputStreamToFile(fileData, file);
          } catch (Exception e) {
            e.printStackTrace();
          }
          Component component =
              createComponent(
                  event.getMIMEType(), event.getFileName(), bufferImage.getInputStream());
          showOutput(event.getFileName(), component);
          new NotificationWindow(
              "Sie haben die Bild-Datei erfolgreich hochgeladen",
              NotificationType.MEDIUM,
              NotificationType.SUCCESS);
        });

    uploadUxf.addSucceededListener(
        event -> {
          if (imageDTO == null) {
            imageDTO = new ImageDTO();
          }
          InputStream fileData = bufferUxf.getInputStream();
          imageDTO.setUxfMimeType(event.getMIMEType());
          imageDTO.setUxfName(event.getFileName());
          try {
            imageDTO.setUxfPath(fileData.readAllBytes());
          } catch (IOException e) {
            e.printStackTrace();
          }
          // File file = new File(filePath + event.getFileName());
          try {
            //  FileUtils.copyInputStreamToFile(fileData, file);
          } catch (Exception e) {
            e.printStackTrace();
          }

          uxfLayout.remove(text);
          text =
              new Span(
                  "Sie haben folgende .uxf Datei erfolgreich hochgeladen: " + event.getFileName());
          uxfLayout.add(text);
          new NotificationWindow(
              "Sie haben die .uxf-Datei erfolgreich hochgeladen!", 5000, "success");
        });

    HorizontalLayout uploadLayout = new HorizontalLayout();
    uploadLayout.add(imageLayout, uxfLayout);
    uploadImage.setMaxFileSize(500 * 1024);
    horizontalLayout.add(save, cancel);
    add(uploadLayout, horizontalLayout);

    save.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (imageDTO != null
                  && imageDTO.getBildName() != null
                  && imageDTO.getUxfName() != null) {
                imageDTO = (ImageDTO) dao.save(imageDTO);
                // File file = new File("src/main/resources/uploads/test.uxf");
                try {
                  // FileUtils.copyInputStreamToFile(
                  //     new ByteArrayInputStream(imageDTO.getUxfStream()), file);
                } catch (Exception e) {
                  e.printStackTrace();
                }
                DesignDecisionDAO dao1 = DesignDecisionDAO.getInstance();
                // List<UMLComponent> decisions =
                //    DesignDecisionParser.parseFile(file.getAbsolutePath());
                // decisions.stream()
                //     .filter(x -> x instanceof ExistenceDecision)
                //     .forEach(x -> dao1.save(imageDTO, (ExistenceDecision) x));
                new NotificationWindow("Erfolgreich gespeichert!", 5000, "success");
                clearFileList();
                // file.deleteOnExit();
              } else {
                new NotificationWindow(
                    "Bitte laden Sie beide Dateien hoch bevor Sie speichern!", 5000, "error");
              }
            });

    cancel.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (imageDTO != null) {
                if (dao.delete(imageDTO)) {
                  imageDTO = null;
                  updateComponent();
                  new NotificationWindow("Erfolgreich gelöscht!", 5000, "success");
                  clearFileList();
                }
              } else {
                new NotificationWindow(
                    "Keine Daten vorhanden! Es wurde nichts gelöscht.", 5000, "neutral");
              }
            });
  }

  private Component createComponent(String mimeType, String fileName, InputStream stream) {
    if (mimeType.startsWith("application/octet-stream")) {
      return null;
    } else if (mimeType.startsWith("image")) {
      Image image = new Image();
      try {

        byte[] bytes = IOUtils.toByteArray(stream);
        image
            .getElement()
            .setAttribute(
                "src", new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));
        try (ImageInputStream in =
            ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
          final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
          if (readers.hasNext()) {
            ImageReader reader = readers.next();
            try {
              reader.setInput(in);
              image.setWidth(reader.getWidth(0) / 2 + "px");
              image.setHeight(reader.getHeight(0) / 2 + "px");
            } finally {
              reader.dispose();
            }
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

      return image;
    }

    Div content = new Div();
    String text =
        String.format(
            "Mime type: '%s'\nSHA-256 hash: '%s'",
            mimeType, Arrays.toString(MessageDigestUtil.sha256(stream.toString())));
    content.setText(text);
    return content;
  }

  private void showOutput(String text, Component content) {
    imageLayout.remove(output);
    output = new Div();
    HtmlComponent p = new HtmlComponent(Tag.P);
    p.getElement().setText(text);

    output.add(p, content);
    imageLayout.add(output);
  }

  private void updateComponent() {
    output.setVisible(false);
    text.setVisible(false);
  }

  private void clearFileList() {
    uploadImage.clearFileList();
    uploadUxf.clearFileList();
  }
}

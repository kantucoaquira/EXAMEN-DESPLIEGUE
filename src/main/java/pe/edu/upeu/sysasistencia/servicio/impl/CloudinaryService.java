package pe.edu.upeu.sysasistencia.servicio.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Sube una imagen a Cloudinary
     * @param file Archivo a subir
     * @param folder Carpeta en Cloudinary (ej: "personas", "estudiantes")
     * @return URL de la imagen subida
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        try {
            // Validar que sea una imagen
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("El archivo debe ser una imagen");
            }

            // Generar nombre único
            String publicId = folder + "/" + UUID.randomUUID();

            // Subir a Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", publicId,
                            "folder", folder,
                            "resource_type", "image",
                            "transformation", new Object[]{
                                    ObjectUtils.asMap("width", 500, "height", 500, "crop", "limit")
                            }
                    )
            );

            String url = (String) uploadResult.get("secure_url");
            log.info("Imagen subida exitosamente: {}", url);
            return url;

        } catch (IOException e) {
            log.error("Error al subir imagen a Cloudinary: {}", e.getMessage());
            throw new IOException("Error al subir la imagen: " + e.getMessage());
        }
    }

    /**
     * Elimina una imagen de Cloudinary
     * @param imageUrl URL de la imagen a eliminar
     */
    public void deleteImage(String imageUrl) {
        try {
            // Extraer public_id de la URL
            String publicId = extractPublicIdFromUrl(imageUrl);

            if (publicId != null) {
                Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Imagen eliminada: {} - Resultado: {}", publicId, result.get("result"));
            }
        } catch (Exception e) {
            log.error("Error al eliminar imagen: {}", e.getMessage());
        }
    }

    /**
     * Extrae el public_id de una URL de Cloudinary
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary.com")) {
            return null;
        }

        try {
            // URL típica: https://res.cloudinary.com/cloud-name/image/upload/v123456/folder/filename.jpg
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String pathAfterUpload = parts[1];
                // Remover versión y extensión
                String publicId = pathAfterUpload.substring(pathAfterUpload.indexOf("/") + 1);
                publicId = publicId.substring(0, publicId.lastIndexOf("."));
                return publicId;
            }
        } catch (Exception e) {
            log.error("Error al extraer public_id: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Actualiza una imagen (elimina la anterior y sube la nueva)
     */
    public String updateImage(MultipartFile newFile, String oldImageUrl, String folder) throws IOException {
        // Eliminar imagen anterior si existe
        if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
            deleteImage(oldImageUrl);
        }

        // Subir nueva imagen
        return uploadImage(newFile, folder);
    }
}
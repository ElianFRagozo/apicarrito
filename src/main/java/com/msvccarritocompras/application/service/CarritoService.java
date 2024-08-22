package com.msvccarritocompras.application.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msvccarritocompras.application.dto.CarritoRequest;
import com.msvccarritocompras.application.dto.CarritoResponse;
import com.msvccarritocompras.application.dto.reports.DescuentoTotalDto;
import com.msvccarritocompras.application.dto.reports.ProductoMostrar;
import com.msvccarritocompras.application.repository.CarritoRepository;
import com.msvccarritocompras.domain.entity.Carrito;
import com.msvccarritocompras.domain.entity.Cupon;
import com.msvccarritocompras.domain.entity.Imagen;
import com.msvccarritocompras.domain.entity.ItemCarrito;
import com.msvccarritocompras.domain.enums.TipoDescuento;
import com.msvccarritocompras.domain.exceptions.CarritoNotFoundException;
import com.msvccarritocompras.domain.exceptions.CuponExpiredException;
import com.msvccarritocompras.domain.exceptions.CuponNotFoundException;
import com.msvccarritocompras.infrastructure.api.producto.client.ProductoFeingClient;
import com.msvccarritocompras.infrastructure.api.producto.dto.ProductoDto;
import com.msvccarritocompras.infrastructure.api.producto.exceptions.ProductNotFoundException;
import com.msvccarritocompras.infrastructure.api.usuarios.client.UsuarioFeingClient;
import com.msvccarritocompras.infrastructure.api.usuarios.dto.UsuarioDto;
import com.msvccarritocompras.infrastructure.configuration.kafka.dto.KafkaMessageDto;
import com.msvccarritocompras.infrastructure.persistence.repository.CarritoPersistence;
import com.msvccarritocompras.infrastructure.persistence.repository.CuponPersistence;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarritoService implements CarritoRepository {

    private final CarritoPersistence carritoPersistence;
    private final ProductoFeingClient productoFeingClient;
    private final CuponPersistence cuponPersistence;
    private final UsuarioFeingClient usuarioFeingClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();



    @Value("${cloudinary.cloud-name}")
    String cloudName;
    @Value("${cloudinary.api-key}")
    String apiKey;
    @Value("${cloudinary.api-secret}")
    String apiSecret;
    @Value("${kafka.topic.name}")
    String topicName;



    @Transactional
    @Override
    public CarritoResponse crearCarrito(CarritoRequest carritoRequest) {
        BigDecimal totalConDescuento;
        String codigoCupon = carritoRequest.codigoCupon() != null ? carritoRequest.codigoCupon() : "no-cupon";
        Cupon cuponEncontrado = cuponPersistence.findCuponByCodigo(codigoCupon)
                .orElseThrow(()-> new CuponNotFoundException("cupon con cupon :"+codigoCupon+" no encontrado"));
        if (cuponEncontrado.getFechaExpiracion() != null && cuponEncontrado.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new CuponExpiredException("El cupón con código: " + codigoCupon + " ha expirado");
        }

        List<ItemCarrito> itemCarritoList = carritoRequest.itemcarritoList().stream()
                .map(itemCarritoRequest -> {
                    ProductoDto productoDto = productoFeingClient.getProductById(itemCarritoRequest.productoId());
                    if (productoDto==null){ throw new ProductNotFoundException(itemCarritoRequest.productoId());}
                    BigDecimal precioUnitarioItem = BigDecimal.valueOf(productoDto.getPrice() * itemCarritoRequest.cantidad())
                            .setScale(2, RoundingMode.HALF_UP);
                    return ItemCarrito.builder()
                            .productoId(productoDto.getId())
                            .cantidad(itemCarritoRequest.cantidad())
                            .precioUnitarioItem(precioUnitarioItem.floatValue())
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal totalCarrito = itemCarritoList.stream()
                .map(itemCarrito -> BigDecimal.valueOf(itemCarrito.getPrecioUnitarioItem()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

       if (cuponEncontrado.getTipoDescuento()== TipoDescuento.PORCENTAJE){
           BigDecimal descuento = BigDecimal.valueOf(cuponEncontrado.getCantidad() / 100.0);
            totalConDescuento = totalCarrito.multiply(BigDecimal.ONE.subtract(descuento))
                   .setScale(2, RoundingMode.HALF_UP);
       }
       else {
           BigDecimal descuento = BigDecimal.valueOf(cuponEncontrado.getCantidad()).setScale(2, RoundingMode.HALF_UP);
           totalConDescuento = totalCarrito.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
       }

        Carrito carrito = Carrito.builder()
                .usuarioId(carritoRequest.usuarioId())
                .total(totalConDescuento.floatValue())
                .cupon(cuponEncontrado)
                .itemCarritoList(itemCarritoList)
                .build();

        Carrito carritoGuardado = carritoPersistence.save(carrito);
        UsuarioDto usuarioDto= usuarioFeingClient.getAllUsuarioById(carritoRequest.usuarioId());

        List<String> listaUrl= generarPDFyCloudinary(itemCarritoList,carritoGuardado,usuarioDto);
        List<Imagen> listaImagenes=new ArrayList<>();

        List<String> urlImagesList= new ArrayList<>();

        List<CarritoResponse.ImagenFacturas> imagenFacturasList=new ArrayList<>();
        listaUrl.forEach(url -> {
            imagenFacturasList.add(new  CarritoResponse.ImagenFacturas(url));
            listaImagenes.add(Imagen.builder().urlImagen(url).build());
            urlImagesList.add(url);
        });
        carritoGuardado.setImagenList(listaImagenes);
        carritoPersistence.saveAndFlush(carritoGuardado);

        List<CarritoResponse.ItemCarritoResponse> itemCarritoResponseList = carritoGuardado.getItemCarritoList().stream()
                .map(itemCarrito -> {
                    ProductoDto productoDto = productoFeingClient.getProductById(itemCarrito.getProductoId());

                    return new CarritoResponse.ItemCarritoResponse(
                            itemCarrito.getItemCarritoId(),
                            itemCarrito.getCantidad(),
                            itemCarrito.getPrecioUnitarioItem(),
                            productoDto
                    );
                })
                .collect(Collectors.toList());



        KafkaMessageDto kafkaMessageDto= new KafkaMessageDto("erik.antony.mg@gmail.com",usuarioDto.getFirstName(),usuarioDto.getLastName(),urlImagesList);
        JsonNode jsonNodeMessageKafka= objectMapper.valueToTree(kafkaMessageDto);
        kafkaTemplate.send(topicName,jsonNodeMessageKafka);

        return new CarritoResponse(
                carritoGuardado.getCarritoId(),
                usuarioDto,
                carritoGuardado.getTotal(),
                carritoGuardado.getCupon().getCodigo(),
                itemCarritoResponseList,
                imagenFacturasList
        );

    }

    @Override
    @Transactional
    public CarritoResponse getCarrito(Long carritoId) {
        Carrito carritoBd = carritoPersistence.findById(carritoId)
                .orElseThrow(()-> new CarritoNotFoundException("carrito con el id:"+carritoId+" no encontrado"));

        List<CarritoResponse.ItemCarritoResponse> itemCarritoResponseList= carritoBd.getItemCarritoList().stream().map(itemCarrito -> {
                    ProductoDto productoDto = productoFeingClient.getProductById(itemCarrito.getProductoId());

                   return new CarritoResponse.ItemCarritoResponse(
                    itemCarrito.getItemCarritoId(),
                    itemCarrito.getCantidad(),
                    itemCarrito.getPrecioUnitarioItem(),
                            productoDto );
        }).toList();
        UsuarioDto usuarioDto= usuarioFeingClient.getAllUsuarioById(carritoBd.getUsuarioId());

        List<CarritoResponse.ImagenFacturas> imagenFacturasList=carritoBd.getImagenList().stream()
                .map(imagen -> new CarritoResponse.ImagenFacturas(imagen.getUrlImagen())).toList();

        return new CarritoResponse(
                carritoBd.getCarritoId(),
                usuarioDto,
                carritoBd.getTotal(),
                carritoBd.getCupon().getCodigo(),
                itemCarritoResponseList,
                imagenFacturasList
        );
    }

    @Override
    public CarritoResponse editCarrito(Long carritoId, CarritoRequest carritoRequest) {
        return null;
    }

    @Override
    public void deleteCarrito(Long carritoId) {

    }
    @Override
    public Page<Carrito> listCarritoPaginado(Long usuarioId, Pageable pageable) {
        if (usuarioId!=null){
                 usuarioFeingClient.getAllUsuarioById(usuarioId);
            return carritoPersistence.findAllByUsuarioId(usuarioId,pageable);
        }
        return carritoPersistence.findAll(pageable);
    }

    public List<String> generarPDFyCloudinary(List<ItemCarrito> itemCarritoList, Carrito carritoGuardado, UsuarioDto usuarioDto) {

        try {

            // Crear la lista de productos a mostrar en el reporte
            List<ProductoMostrar> tablaProductos =itemCarritoList.stream()
                    .map(item->{
                        ProductoDto productoDto = productoFeingClient.getProductById(item.getProductoId());
                         return  ProductoMostrar.builder()
                                 .productName(productoDto.getTitle())
                                 .precioUnitario(BigDecimal.valueOf(productoDto.getPrice()).setScale(2,RoundingMode.HALF_UP))
                                 .precioTotalUnidad(BigDecimal.valueOf(item.getPrecioUnitarioItem()).setScale(2,RoundingMode.HALF_UP))
                                 .cantidad(item.getCantidad())
                                 .build();
            }).toList();

            // Crear la tabla de descuento
            List<DescuentoTotalDto> tablaDescuento = new ArrayList<>();

            tablaDescuento.add(DescuentoTotalDto.builder()
                    .total(BigDecimal.valueOf(carritoGuardado.getTotal()).setScale(2,RoundingMode.HALF_UP))
                    .descuentoPorcentaje(carritoGuardado.getCupon().getCantidad())
                    .build());

            // Configuración del archivo y parámetros de JasperReports
            String filePath = "src/main/resources/templates/reports/factura.jrxml";
            JRBeanCollectionDataSource listaProductos = new JRBeanCollectionDataSource(tablaProductos);
            JRBeanCollectionDataSource tablaDescuento1 = new JRBeanCollectionDataSource(tablaDescuento);
            JRBeanCollectionDataSource tablaDescuento2 = new JRBeanCollectionDataSource(tablaDescuento);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("tablaProductos", listaProductos);
            parameters.put("tablaDescuento1", tablaDescuento1);
            parameters.put("tablaDescuento2", tablaDescuento2);
            parameters.put("numFactura", String.format("FCT%08d%d", carritoGuardado.getCarritoId(), Year.now().getValue()));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            parameters.put("fechaPago", LocalDateTime.now().format(formatter));
            parameters.put("nombreCompletoCliente", usuarioDto.getFirstName().concat(" ").concat(usuarioDto.getLastName()));
            parameters.put("direccionCliente", usuarioDto.getAddress().getCity());
            parameters.put("numeroTelefono", usuarioDto.getPhone());
            parameters.put("codigoCupon", carritoGuardado.getCupon().getCodigo());
            parameters.put("imageDir", this.getClass().getResource("/static/images/").toString());

            // Generación del reporte PDF
            JasperReport report = JasperCompileManager.compileReport(filePath);
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());

            // Subir el reporte a Cloudinary como imágenes
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret));

            // Convertir el JasperPrint a un PDF y luego a imágenes
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(print, pdfOutputStream);
            File tempPdfFile = File.createTempFile("reporte-", ".pdf");
            FileUtils.writeByteArrayToFile(tempPdfFile, pdfOutputStream.toByteArray());

            PDDocument document = PDDocument.load(tempPdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            List<String> imageUrls = new ArrayList<>();

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos);
                baos.flush();
                byte[] imageBytes = baos.toByteArray();
                baos.close();

                // Subir la imagen a Cloudinary
                Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "reportes-facturas"
                ));

                String url = (String) uploadResult.get("url");
                imageUrls.add(url);
                System.out.println("Imagen subida a Cloudinary. URL " + page + " :" + url);
            }

            document.close();
            tempPdfFile.delete();

            return imageUrls; // Lista de URLs de las imágenes subidas

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al generar el reporte: " + e.getMessage());
            return null;
        }
    }
}

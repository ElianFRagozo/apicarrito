## 2. Servicio de Carrito de Compras

Este servicio gestiona los carritos de compra de los usuarios.

### Endpoints:

#### Carrito
- `POST /carts` - Crear un nuevo carrito
- `GET /carts` - Lista de carrito Paginado de longitud 10
- `GET /carts?size=5&page=0` - Lista de carrito Paginado configurable
- `GET /carts?usuarioId` - Lista de carrito Paginado del usuario con su Id
- `GET /carts/{id}` - Obtener detalles de un carrito por su ID

#### Cupones
- `GET /cupones` - Lista de cupones Paginado de longitud 10
- `GET /cupones?size=5&page=0` - Lista de cupones Paginado personalizado
- `GET /cupones?codigo=P-2024` - Buscar cupon su codigo
- `GET /cupones?expirado=true` - Buscar cupon si esta expirado
- `GET /cupones?fecha=2024-12-10` - Buscar cupones que no expiraran antes de esta fecha
- `POST /cupones` - Crear un cupon
- `PUT /cupones/{id}` - Editar un cupon
- `DELETE /cupones/{id}` - Eliminar un cupon
- `GET /cupones/{id}` - Obtener cupon por su Id


### Atributos principales:

- Carrito: `carrito_id`, `usuario_id`, `carrito_items[]`, `total`, `cupón`
- Item del carrito: `id`, `producto_id`, `cantidad`, `precio_unitario`
- Cupón: `cupon_id`,`descripcion`,`código`, `cantidad`, `tipo_descuento` (porcentaje o monto fijo),`fecha_creacion`,`fecha_expiracion`,`is_expired`

### Swagger Link
- http://localhost:8085/swagger-ui/index.html
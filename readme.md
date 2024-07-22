## 2. Servicio de Carrito de Compras

Este servicio gestiona los carritos de compra de los usuarios.

### Endpoints:

- `POST /carts` - Crear un nuevo carrito
- `GET /carts/{id}` - Obtener detalles de un carrito
- `POST /carts/{id}/items` - Añadir un item al carrito
- `PUT /carts/{id}/items/{item_id}` - Actualizar cantidad de un item en el carrito
- `DELETE /carts/{id}/items/{item_id}` - Eliminar un item del carrito
- `GET /carts/{id}/total` - Calcular el total del carrito
- `POST /carts/{id}/apply-coupon` - Aplicar un cupón al carrito

### Atributos principales:

- Carrito: `id`, `usuario_id`, `items[]`, `total`, `cupón_aplicado`
- Item del carrito: `id`, `producto_id`, `cantidad`, `precio_unitario`
- Cupón: `código`, `descuento`, `tipo_descuento` (porcentaje o monto fijo)
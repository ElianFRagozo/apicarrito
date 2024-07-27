CREATE TABLE IF NOT EXISTS cupones (
                                        cupon_id SERIAL PRIMARY KEY,
                                        descripcion VARCHAR(255),
                                        codigo VARCHAR(255) UNIQUE,
                                        tipo_descuento VARCHAR(50),
                                        cantidad NUMERIC
    );

CREATE TABLE IF NOT EXISTS carritos (
                                        carrito_id  SERIAL PRIMARY KEY,
                                        usuario_id BIGINT,
                                        total NUMERIC,
                                        cupon_id BIGINT,
                                        FOREIGN KEY (cupon_id) REFERENCES cupones(cupon_id)
    );

CREATE TABLE IF NOT EXISTS items (
                                     item_carrito_id SERIAL PRIMARY KEY,
                                     producto_id BIGINT,
                                     cantidad INT,
                                     precio_unitario_item NUMERIC
);

CREATE TABLE IF NOT EXISTS carrito_items (
                                             carrito_id SERIAL,
                                             item_id SERIAL,
                                             FOREIGN KEY (carrito_id) REFERENCES carritos(carrito_id),
FOREIGN KEY (item_id) REFERENCES items(item_carrito_id )
);

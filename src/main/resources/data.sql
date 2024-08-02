INSERT INTO cupones ( codigo, descripcion, tipo_descuento, cantidad,fecha_creacion,fecha_expiracion,is_expired)
VALUES ( 'no-cupon', 'sin descuento', 'PORCENTAJE', 0,NOW(),null,false)
ON CONFLICT (codigo) DO NOTHING;
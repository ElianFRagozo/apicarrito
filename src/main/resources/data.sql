INSERT INTO cupones ( codigo, descripcion, tipo_descuento, cantidad)
VALUES ( 'no-cupon', 'sin descuento', 'PORCENTAJE', 0)
ON CONFLICT (cupon_id) DO NOTHING;
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Erick','Gonzalez','erickgozan@gmail.com','2021-10-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Lucia','Muñoz','miluz24@gmail.com','2021-10-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('John','Doe','john.doe@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Linus','Torvalds','linus.torvalds@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Jane','Herrera','jane.herrera@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Rasmus','Andrade','rasmus.andrade@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Richard','Perez','richard.perez@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Sergio','Vicente','sergiovicente@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Roberto','Palomo','roberto_palomo@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Teresa','Cuevas','teresa.cuevas@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Jazmin','Meza','jazmin.meza@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Arel','Alonso','arel_alonso@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Ana','Gonzalez','anagonzalez@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Leslie','Ochoa','lelie_ochoa@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Itzel','Fernandéz','itzel.fernandez@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('consuelo','Elizalde','consuelo_elizalde@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Ivan','Campos','ivan.campos@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Anastacia','Cruz','anastaciacruz@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Ariadna','Andrade','ariadnaandrade@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Miguel','Cuevas','miguel.cuevas@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Eduardo','Río','eduardo_rios@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Gerardo','Mendéz','gerardo.mendez@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Sofia','Hidalgo','sofiahidalgo@gmail.com','2020-04-04','');
INSERT INTO clientes(nombre,apellido,email,create_at,foto) VALUES ('Andrea','Juarez','andreajuarez@gmail.com','2020-04-04','');

/*tabla de productos*/
INSERT INTO productos(nombre,precio,create_at) VALUES ('Panasonic Pantalla LCD',25999,NOW());
INSERT INTO productos(nombre,precio,create_at) VALUES ('Sony Camara digital DSC-W3208',12398,NOW());
INSERT INTO productos(nombre,precio,create_at) VALUES ('Apple ipod shuffle',45999,NOW());
INSERT INTO productos(nombre,precio,create_at) VALUES ('Sony Notebook Z110',18999,NOW());
INSERT INTO productos(nombre,precio,create_at) VALUES ('Hewlett packard Multifuncional F2280',38475,NOW());
INSERT INTO productos(nombre,precio,create_at) VALUES ('Blanchi Bicicleta Aro 26',5999,NOW());
INSERT INTO productos(nombre,precio,create_at) VALUES ('Mica Comoda 5 cajones',3999,NOW());
INSERT INTO productos(nombre,precio,create_at) VALUES ('Samsung galaxy s20 plus',23999,NOW());

/*Creamos las facturas*/
INSERT INTO facturas(descripcion,observacion,cliente_id,create_at) VALUES ('Factura equipos de oficina',null,1,NOW());
INSERT INTO facturas_items(cantidad,factura_id,producto_id) VALUES (1,1,1);
INSERT INTO facturas_items(cantidad,factura_id,producto_id) VALUES (2,1,4);
INSERT INTO facturas_items(cantidad,factura_id,producto_id) VALUES (1,1,5);
INSERT INTO facturas_items(cantidad,factura_id,producto_id) VALUES (1,1,7);

INSERT INTO facturas(descripcion,observacion,cliente_id,create_at) VALUES ('Factura bicileta','Alguna nota importante',1,NOW());
INSERT INTO facturas_items(cantidad,factura_id,producto_id) VALUES (3,2,6);

/*Insertamos los usuarios*/
insert into users(username,password,enabled) values ('erickgozan','$2a$10$gFElETuePpr4Du1Jng.zROLltZsjpCeC3v2NhtRCSxBUHk50AcqmK',1);
insert into users(username,password,enabled) values('admin','$2a$10$HygjLHBo3X78XR1VHk2..O8eN0jSjM./m6Cl85i0mFMe5D7EiV4nG',1);

/*Insertamos los authorities*/
insert into authorities(user_id,authority)values(1,'ROLE_USER');
insert into authorities(user_id,authority)values(2,'ROLE_USER');
insert into authorities(user_id,authority)values(2,'ROLE_ADMIN');
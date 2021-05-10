create SCHEMA products
create table products.products(
	id serial primary key,
	name varchar(50),
	image text,
	price decimal,
	price_sale decimal,
	description text,
	quantity serial
)
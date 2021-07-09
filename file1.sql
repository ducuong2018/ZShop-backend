
create table products.products(
                                  id serial primary key,
                                  user_id serial,
                                  name varchar(50),
                                  image text,
                                  price decimal,
                                  price_sale decimal,
                                  description text,
                                  quantity serial,

                                  CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES accounts.user(id)
)

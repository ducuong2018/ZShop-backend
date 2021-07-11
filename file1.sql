create schema accounts;
create table accounts.user(
                              id serial primary key,
                              email varchar unique,
                              phone_number varchar unique,
                              password_crypt text,
                              profile jsonb,
                              created_at timestamp without time zone default CURRENT_TIMESTAMP,
                              updated_at timestamp without time zone default CURRENT_TIMESTAMP
);
create schema products
    create table products.products(
                                      id serial primary key,
                                      user_id serial,
                                      name varchar(50),
                                      image text,
                                      price decimal,
                                      price_sale decimal,
                                      description text,
                                      quantity serial,
                                      CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES accounts.user(id),
                                      created_at timestamp without time zone default CURRENT_TIMESTAMP,
                                      updated_at timestamp without time zone default CURRENT_TIMESTAMP
    )
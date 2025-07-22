DROP TABLE products IF EXISTS;

CREATE TABLE products (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(64) NOT NULL,
  description VARCHAR(256),
  brand VARCHAR(64),
  category VARCHAR(64),
  price DECIMAL(5,2) NOT NULL DEFAULT '0',
  currency VARCHAR(64),
  stock INT NOT NULL DEFAULT '0',
  ean VARCHAR(64) NOT NULL,
  color VARCHAR(64),
  size VARCHAR(64),
  availability VARCHAR(64)
);